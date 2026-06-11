package ai.pegasusgrowth.gtraindash.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.pegasusgrowth.gtraindash.data.TrainArrival
import ai.pegasusgrowth.gtraindash.data.TrainArrivals
import ai.pegasusgrowth.gtraindash.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Ticker that updates current time reference in seconds every 1 second
    var currentTimeSeconds by remember { mutableStateOf(System.currentTimeMillis() / 1000L) }
    DisposableEffect(Unit) {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                currentTimeSeconds = System.currentTimeMillis() / 1000L
            }
        }, 0L, 1000L)
        onDispose {
            timer.cancel()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        when (val state = uiState) {
            is DashboardUiState.Loading -> {
                LoadingStateScreen()
            }
            is DashboardUiState.Success -> {
                MainDashboardContent(
                    arrivals = state.arrivals,
                    isRefreshing = state.isRefreshing,
                    lastUpdatedEpoch = state.lastSuccessfulFetchEpochSeconds,
                    currentTimeSeconds = currentTimeSeconds,
                    onRefresh = { viewModel.refreshData() }
                )
            }
            is DashboardUiState.Error -> {
                ErrorStateScreen(
                    message = state.message,
                    cachedArrivals = state.cachedArrivals,
                    currentTimeSeconds = currentTimeSeconds,
                    onRefresh = { viewModel.refreshData() }
                )
            }
        }
    }
}

@Composable
fun MainDashboardContent(
    arrivals: TrainArrivals,
    isRefreshing: Boolean,
    lastUpdatedEpoch: Long,
    currentTimeSeconds: Long,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        HeaderBar(
            lastUpdatedEpoch = lastUpdatedEpoch,
            currentTimeSeconds = currentTimeSeconds,
            isRefreshing = isRefreshing,
            hasError = false,
            onRefresh = onRefresh
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Split view for Northbound and Southbound
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column - Northbound (Queens-bound / to Court Sq)
            DirectionColumn(
                title = "NORTHBOUND",
                destination = "To Court Sq",
                trains = arrivals.northbound,
                currentTimeSeconds = currentTimeSeconds,
                modifier = Modifier.weight(1f)
            )

            // Right Column - Southbound (Brooklyn-bound / to Church Ave)
            DirectionColumn(
                title = "SOUTHBOUND",
                destination = "To Church Av",
                trains = arrivals.southbound,
                currentTimeSeconds = currentTimeSeconds,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun HeaderBar(
    lastUpdatedEpoch: Long,
    currentTimeSeconds: Long,
    isRefreshing: Boolean,
    hasError: Boolean,
    onRefresh: () -> Unit
) {
    // Pulsating animation for the status dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Formatted current time (Large digital clock)
    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedCurrentTime = remember(currentTimeSeconds) {
        timeFormatter.format(Date(currentTimeSeconds * 1000L))
    }

    // Seconds elapsed since last update
    val secondsAgo = currentTimeSeconds - lastUpdatedEpoch
    val lastUpdateText = when {
        secondsAgo < 0 -> "Just now"
        secondsAgo < 10 -> "Just now"
        secondsAgo < 60 -> "${secondsAgo}s ago"
        else -> "${secondsAgo / 60}m ago"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderGlow, RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Station details
        Column {
            Text(
                text = "MYRTLE-WILLOUGHBY AVES",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "IND Crosstown Line",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Live Clock (Center)
        Text(
            text = formattedCurrentTime,
            color = GTrainGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace
        )

        // Status & Refresh (Right)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Status Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .alpha(if (isRefreshing) 1.0f else dotAlpha)
                        .clip(CircleShape)
                        .background(
                            if (hasError) ErrorRed 
                            else if (isRefreshing) WarningOrange 
                            else SuccessGreen
                        )
                )

                Text(
                    text = if (hasError) "OFFLINE" 
                           else if (isRefreshing) "REFRESHING..." 
                           else "LIVE ($lastUpdateText)",
                    color = if (hasError) ErrorRed else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Manual Refresh Icon
            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x1F222822))
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Feed",
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun DirectionColumn(
    title: String,
    destination: String,
    trains: List<TrainArrival>,
    currentTimeSeconds: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, Color(0x1F6CBE45), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = GTrainGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            
            Text(
                text = destination,
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color(0x1F6CBE45), thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

        if (trains.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = "No upcoming trains",
                        color = TextMuted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trains, key = { it.tripId }) { train ->
                    ArrivalCard(
                        train = train,
                        currentTimeSeconds = currentTimeSeconds,
                        isNextTrain = trains.firstOrNull() == train
                    )
                }
            }
        }
    }
}

@Composable
fun ArrivalCard(
    train: TrainArrival,
    currentTimeSeconds: Long,
    isNextTrain: Boolean
) {
    val minutes = train.getMinutesRemaining(currentTimeSeconds)
    
    // Animate background color if it's the next train to create a premium glow
    val borderAlpha = if (isNextTrain) {
        val infiniteTransition = rememberInfiniteTransition(label = "glow")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glowAlpha"
        )
        alpha
    } else {
        0.1f
    }

    val cardBg = if (isNextTrain) {
        Color(0x2A1C2C1C) // Slightly greener background for next train
    } else {
        CardBackground
    }

    val exactTimeFormatter = remember { SimpleDateFormat("h:mm a", Locale.US) }
    val formattedExactTime = remember(train.arrivalTime) {
        exactTimeFormatter.format(Date(train.arrivalTime * 1000L))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .border(1.dp, GTrainGreen.copy(alpha = borderAlpha), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // G Train Logo Circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .shadow(if (isNextTrain) 4.dp else 0.dp, CircleShape, spotColor = GTrainGreenGlow)
                    .clip(CircleShape)
                    .background(GTrainGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Destination and scheduled time
            Column {
                Text(
                    text = train.destination,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Scheduled at $formattedExactTime",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Countdown Timer
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (minutes <= 0) {
                Text(
                    text = "Approaching",
                    color = GTrainGreenGlow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            } else {
                Text(
                    text = minutes.toString(),
                    color = if (isNextTrain) GTrainGreenGlow else TextPrimary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "min",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}

@Composable
fun LoadingStateScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = GTrainGreen,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "FETCHING MTA ARRIVALS...",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun ErrorStateScreen(
    message: String,
    cachedArrivals: TrainArrivals?,
    currentTimeSeconds: Long,
    onRefresh: () -> Unit
) {
    if (cachedArrivals != null && (cachedArrivals.northbound.isNotEmpty() || cachedArrivals.southbound.isNotEmpty())) {
        // We have cached data! Show it with an error header bar.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Error Header
            HeaderBar(
                lastUpdatedEpoch = cachedArrivals.lastUpdatedEpochSeconds,
                currentTimeSeconds = currentTimeSeconds,
                isRefreshing = false,
                hasError = true,
                onRefresh = onRefresh
            )

            // Alert banner underneath header
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x1FEE5350))
                    .border(1.dp, ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Connection lost. Displaying cached schedule data: $message",
                    color = ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Split view using cache
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DirectionColumn(
                    title = "NORTHBOUND (CACHED)",
                    destination = "To Court Sq",
                    trains = cachedArrivals.northbound,
                    currentTimeSeconds = currentTimeSeconds,
                    modifier = Modifier.weight(1f)
                )

                DirectionColumn(
                    title = "SOUTHBOUND (CACHED)",
                    destination = "To Church Av",
                    trains = cachedArrivals.southbound,
                    currentTimeSeconds = currentTimeSeconds,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    } else {
        // No cached data, show full-screen error
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                modifier = Modifier
                    .width(480.dp)
                    .border(1.dp, ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0x1FEE5350)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "CONNECTION ERROR",
                        color = ErrorRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = message,
                        color = TextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onRefresh,
                        colors = ButtonDefaults.buttonColors(containerColor = GTrainGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retry Connection", color = Color.White)
                    }
                }
            }
        }
    }
}
