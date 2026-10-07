package ai.pegasusgrowth.gtraindash.data

import android.util.Log
import com.google.transit.realtime.GtfsRealtime
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

data class TrainArrival(
    val tripId: String,
    val routeId: String,
    val stopId: String,
    val destination: String,
    val arrivalTime: Long // Unix timestamp in seconds
) {
    fun getMinutesRemaining(currentTimeSeconds: Long): Long {
        val seconds = arrivalTime - currentTimeSeconds
        return if (seconds < 0) 0 else seconds / 60
    }
}

data class TrainArrivals(
    val northbound: List<TrainArrival>,
    val southbound: List<TrainArrival>,
    val lastUpdatedEpochSeconds: Long,
    val errorMessage: String? = null
)

class MtaDataService {
    private val client = getUnsafeOkHttpClient()

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    companion object {
        // G-train specific open GTFS-realtime endpoint (keyless)
        private const val FEED_URL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-g"
        private const val TARGET_STOP_N = "G32N" // Myrtle-Willoughby Avs Northbound
        private const val TARGET_STOP_S = "G32S" // Myrtle-Willoughby Avs Southbound
        private const val TAG = "MtaDataService"
    }

    /**
     * Fetches real-time arrivals from the MTA GTFS feed. No API key required.
     */
    fun fetchArrivals(): TrainArrivals {
        val request = Request.Builder()
            .url(FEED_URL)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected HTTP code: ${response.code}")
                }

                val body = response.body ?: throw IOException("Empty response body")
                val feed = GtfsRealtime.FeedMessage.parseFrom(body.byteStream())
                
                val currentTime = System.currentTimeMillis() / 1000L
                val arrivals = mutableListOf<TrainArrival>()

                for (entity in feed.entityList) {
                    if (entity.hasTripUpdate()) {
                        val tripUpdate = entity.tripUpdate
                        val routeId = tripUpdate.trip.routeId
                        
                        for (stopTimeUpdate in tripUpdate.stopTimeUpdateList) {
                            val stopId = stopTimeUpdate.stopId
                            if (stopId == TARGET_STOP_N || stopId == TARGET_STOP_S) {
                                val arrivalTime = if (stopTimeUpdate.hasArrival() && stopTimeUpdate.arrival.hasTime()) {
                                    stopTimeUpdate.arrival.time
                                } else if (stopTimeUpdate.hasDeparture() && stopTimeUpdate.departure.hasTime()) {
                                    stopTimeUpdate.departure.time
                                } else {
                                    0L
                                }

                                // If arrival time is in the past by more than a minute, skip it
                                if (arrivalTime > 0L && arrivalTime >= currentTime - 60L) {
                                    val lastStopId = tripUpdate.stopTimeUpdateList.lastOrNull()?.stopId ?: stopId
                                    val destination = getDestinationName(lastStopId, stopId)
                                    arrivals.add(
                                        TrainArrival(
                                            tripId = tripUpdate.trip.tripId,
                                            routeId = routeId,
                                            stopId = stopId,
                                            destination = destination,
                                            arrivalTime = arrivalTime
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Split into northbound and southbound, filter out duplicates (same trip ID), and sort by arrival time
                val northboundList = arrivals.filter { it.stopId == TARGET_STOP_N }
                    .distinctBy { it.tripId }
                    .sortedBy { it.arrivalTime }

                val southboundList = arrivals.filter { it.stopId == TARGET_STOP_S }
                    .distinctBy { it.tripId }
                    .sortedBy { it.arrivalTime }

                return TrainArrivals(
                    northbound = northboundList,
                    southbound = southboundList,
                    lastUpdatedEpochSeconds = currentTime
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching MTA data", e)
            return TrainArrivals(
                northbound = emptyList(),
                southbound = emptyList(),
                lastUpdatedEpochSeconds = System.currentTimeMillis() / 1000L,
                errorMessage = e.message ?: "Unknown error"
            )
        }
    }

    private fun getDestinationName(lastStopId: String, stopId: String): String {
        val cleanLastStopId = lastStopId.take(3)
        return when (cleanLastStopId) {
            "G22" -> "Court Sq"
            "G33" -> "Bedford-Nostrand"
            "F27" -> "Church Av"
            "F35" -> "Coney Island"
            else -> {
                if (stopId.endsWith("N")) "Court Sq" else "Church Av"
            }
        }
    }
}
