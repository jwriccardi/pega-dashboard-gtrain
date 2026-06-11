package ai.pegasusgrowth.gtraindash.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.pegasusgrowth.gtraindash.R
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
    val oswaldFontFamily = FontFamily(
        Font(R.font.oswald_regular, FontWeight.Normal),
        Font(R.font.oswald_bold, FontWeight.Bold)
    )

    val southboundTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds) }
    val northboundTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds) }

    val sbMin1 = southboundTrains.getOrNull(0)?.toString() ?: "--"
    val sbMin2 = southboundTrains.getOrNull(1)?.toString() ?: "--"
    
    val nbMin1 = northboundTrains.getOrNull(0)?.toString() ?: "--"
    val nbMin2 = northboundTrains.getOrNull(1)?.toString() ?: "--"

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Left Column
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6CBE45)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        color = Color.White,
                        fontSize = 130.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = oswaldFontFamily
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "MYRTLE\nWILLOUGHBY",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = oswaldFontFamily,
                    lineHeight = 64.sp
                )
            }

            // Vertical Divider
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(0.8f)
                    .background(Color(0xFF2A2A2A))
            )

            // Right Column
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(start = 32.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Top Row: Church Ave
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHURCH\nAVE",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        fontFamily = oswaldFontFamily,
                        lineHeight = 64.sp
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = sbMin1, color = Color.White, fontSize = 96.sp, fontWeight = FontWeight.Normal, fontFamily = oswaldFontFamily)
                        Text(text = "MINS", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = sbMin2, color = Color.White, fontSize = 96.sp, fontWeight = FontWeight.Normal, fontFamily = oswaldFontFamily)
                        Text(text = "MINS", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFF2A2A2A)))
                Spacer(modifier = Modifier.height(32.dp))

                // Bottom Row: Court Square
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COURT\nSQUARE",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        fontFamily = oswaldFontFamily,
                        lineHeight = 64.sp
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = nbMin1, color = Color.White, fontSize = 96.sp, fontWeight = FontWeight.Normal, fontFamily = oswaldFontFamily)
                        Text(text = "MINS", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = nbMin2, color = Color.White, fontSize = 96.sp, fontWeight = FontWeight.Normal, fontFamily = oswaldFontFamily)
                        Text(text = "MIN", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                    }
                }
            }
        }
        
        if (isRefreshing) {
            Text(
                text = "Refreshing...",
                color = WarningOrange,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
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
        // Station details and the single large G Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .shadow(4.dp, CircleShape, spotColor = GTrainGreenGlow)
                    .clip(CircleShape)
                    .background(GTrainGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = "MYRTLE-WILLOUGHBY AVES",
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "IND Crosstown Line",
                    color = TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Live Clock (Center) - Enlarge for easy distance reading
        Text(
            text = formattedCurrentTime,
            color = GTrainGreen,
            fontSize = 42.sp,
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
        // Section Header - enlarged text sizes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = GTrainGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            
            Text(
                text = destination,
                color = TextSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            // Standard Column utilizing weights to distribute vertical space perfectly
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val takeTrains = trains.take(2)
                if (takeTrains.size == 1) {
                    ArrivalCard(
                        train = takeTrains[0],
                        currentTimeSeconds = currentTimeSeconds,
                        isNextTrain = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else if (takeTrains.size >= 2) {
                    ArrivalCard(
                        train = takeTrains[0],
                        currentTimeSeconds = currentTimeSeconds,
                        isNextTrain = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.4f) // Enormous "Hero" layout for first train
                    )
                    ArrivalCard(
                        train = takeTrains[1],
                        currentTimeSeconds = currentTimeSeconds,
                        isNextTrain = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Smaller layout for second train
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
    isNextTrain: Boolean,
    modifier: Modifier = Modifier
) {
    val minutes = train.getMinutesRemaining(currentTimeSeconds)
    
    // Ambient Pulse Concept Logic
    val pulseDuration = when {
        minutes <= 1 -> 600 // Fast heartbeat (kinetic energy)
        minutes <= 5 -> 1200 // Faster breathing
        else -> 2500 // Slow resting pulse
    }

    val infiniteTransition = rememberInfiniteTransition(label = "ambientPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = if (isNextTrain) 0.3f else 0.1f,
        targetValue = if (isNextTrain) 1.0f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(pulseDuration, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientPulseAlpha"
    )

    // Dynamic background that breathes with the pulse
    val cardBg = if (isNextTrain) {
        Color(0x2A1C2C1C).copy(alpha = 0.2f + (pulseAlpha * 0.4f))
    } else {
        CardBackground
    }

    val exactTimeFormatter = remember { SimpleDateFormat("h:mm a", Locale.US) }
    val formattedExactTime = remember(train.arrivalTime) {
        exactTimeFormatter.format(Date(train.arrivalTime * 1000L))
    }

    // Determine if the train terminates at an unusual short-turn destination
    val expectedDefaultDest = if (train.stopId.endsWith("N")) "Court Sq" else "Church Av"
    val isShortTurn = train.destination != expectedDefaultDest && train.destination.isNotBlank()

    Box(
        modifier = modifier
            .shadow(
                elevation = if (isNextTrain) (pulseAlpha * 16).dp else 0.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = GTrainGreenGlow,
                ambientColor = GTrainGreen
            )
            .clip(RoundedCornerShape(24.dp))
            .background(cardBg)
            .border(
                width = if (isNextTrain) 2.dp else 1.dp,
                color = GTrainGreen.copy(alpha = pulseAlpha * (if(isNextTrain) 0.8f else 0.3f)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Giant Countdown text
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                if (minutes <= 0) {
                    Text(
                        text = "Approaching",
                        color = GTrainGreenGlow,
                        fontSize = if (isNextTrain) 48.sp else 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                } else {
                    Text(
                        text = minutes.toString(),
                        color = if (isNextTrain) GTrainGreenGlow else TextPrimary,
                        fontSize = if (isNextTrain) 110.sp else 64.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = if (isNextTrain) 110.sp else 64.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "min",
                        color = TextSecondary,
                        fontSize = if (isNextTrain) 28.sp else 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = if (isNextTrain) 16.dp else 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isNextTrain) 12.dp else 4.dp))

            // Subtext: Scheduled time
            Text(
                text = "Scheduled at $formattedExactTime",
                color = TextSecondary,
                fontSize = if (isNextTrain) 20.sp else 15.sp,
                fontWeight = FontWeight.Medium
            )

            // Warning if short turn destination
            if (isShortTurn) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1FEE5350))
                        .border(1.dp, ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(if (isNextTrain) 16.dp else 12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Terminates at ${train.destination}",
                        color = ErrorRed,
                        fontSize = if (isNextTrain) 16.sp else 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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

            // Split view using cache - limited to 2 items with layout weights
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

fun Modifier.centerAt(xPx: Float, yPx: Float) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(0, 0) {
        placeable.placeRelative(
            x = (xPx - placeable.width / 2f).toInt(),
            y = (yPx - placeable.height / 2f).toInt()
        )
    }
}

fun getTrainPosition(d: Float, X_v: Float, Y_h: Float, Y_s: Float, R: Float, isNorthbound: Boolean): androidx.compose.ui.geometry.Offset {
    val d_curve_start = (Y_h - R) - Y_s
    val curve_length = (Math.PI.toFloat() * R) / 2f
    val d_horiz_start = d_curve_start + curve_length

    if (isNorthbound) {
        return when {
            d < 0 -> androidx.compose.ui.geometry.Offset(X_v, Y_s)
            d < d_curve_start -> {
                androidx.compose.ui.geometry.Offset(X_v, Y_s + d)
            }
            d < d_horiz_start -> {
                val d_on_curve = d - d_curve_start
                val angle = d_on_curve / R
                val cx = X_v - R
                val cy = Y_h - R
                androidx.compose.ui.geometry.Offset(
                    cx + R * kotlin.math.cos(angle.toDouble()).toFloat(),
                    cy + R * kotlin.math.sin(angle.toDouble()).toFloat()
                )
            }
            else -> {
                val d_on_horiz = d - d_horiz_start
                androidx.compose.ui.geometry.Offset(X_v - R - d_on_horiz, Y_h)
            }
        }
    } else {
        if (d < 0) return androidx.compose.ui.geometry.Offset(X_v, Y_s)
        return androidx.compose.ui.geometry.Offset(X_v, Y_s - d)
    }
}

fun getTrackPath(X_v: Float, Y_h: Float, R: Float): androidx.compose.ui.graphics.Path {
    return androidx.compose.ui.graphics.Path().apply {
        moveTo(0f, Y_h)
        lineTo(X_v - R, Y_h)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(X_v - 2*R, Y_h - 2*R, X_v, Y_h),
            startAngleDegrees = 90f,
            sweepAngleDegrees = -90f,
            forceMoveTo = false
        )
        lineTo(X_v, 0f)
    }
}

@Composable
fun TrackCanvas(
    northbound: List<TrainArrival>,
    southbound: List<TrainArrival>,
    currentTimeSeconds: Long
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val W = constraints.maxWidth.toFloat()
        val H = constraints.maxHeight.toFloat()
        val density = androidx.compose.ui.platform.LocalDensity.current
        
        val X_v = W * 0.65f
        val Y_h = H * 0.85f
        val R = H * 0.25f
        val Y_s = H * 0.45f
        
        val spacing = with(density) { 60.dp.toPx() }
        val X_v_nb = X_v + spacing/2
        val Y_h_nb = Y_h + spacing/2
        val R_nb = R + spacing/2

        val X_v_sb = X_v - spacing/2
        val Y_h_sb = Y_h - spacing/2
        val R_sb = R - spacing/2
        
        val maxNb = northbound.take(2).maxOfOrNull { it.getMinutesRemaining(currentTimeSeconds) } ?: 15L
        val maxSb = southbound.take(2).maxOfOrNull { it.getMinutesRemaining(currentTimeSeconds) } ?: 15L
        val maxMin = maxOf(10L, maxNb, maxSb).toFloat()
        
        val pixelsPerMinute = (H * 0.4f) / maxMin

        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val streetColor = Color(0xFF444444)
            val streetStrokeWidth = 2.dp.toPx()
            val dashEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(20f, 20f))

            // Marcy Ave (Vertical)
            drawLine(
                color = streetColor,
                start = androidx.compose.ui.geometry.Offset(X_v, 0f),
                end = androidx.compose.ui.geometry.Offset(X_v, H),
                strokeWidth = streetStrokeWidth,
                pathEffect = dashEffect
            )

            // Myrtle Ave (Horizontal)
            drawLine(
                color = streetColor,
                start = androidx.compose.ui.geometry.Offset(0f, Y_s),
                end = androidx.compose.ui.geometry.Offset(W, Y_s),
                strokeWidth = streetStrokeWidth,
                pathEffect = dashEffect
            )

            val pathNB = getTrackPath(X_v_nb, Y_h_nb, R_nb)
            val pathSB = getTrackPath(X_v_sb, Y_h_sb, R_sb)
            
            val stroke = androidx.compose.ui.graphics.drawscope.Stroke(width = 8.dp.toPx())
            val trackColor = Color(0xFF999999) // Lighter track lines

            drawPath(pathNB, color = trackColor, style = stroke)
            drawPath(pathSB, color = trackColor, style = stroke)
        }

        // Street Labels
        Text(
            text = "MARCY AVE",
            color = Color(0xFF555555),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.centerAt(X_v + with(density) { 50.dp.toPx() }, H * 0.1f)
        )
        Text(
            text = "MYRTLE AVE",
            color = Color(0xFF555555),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.centerAt(W * 0.2f, Y_s - with(density) { 20.dp.toPx() })
        )

        Box(modifier = Modifier.centerAt(X_v, Y_s)) {
            StationNode()
        }

        southbound.take(4).forEach { train ->
            val min = train.getMinutesRemaining(currentTimeSeconds)
            val d = min * pixelsPerMinute
            val pos = getTrainPosition(d, X_v_sb, Y_h_sb, Y_s, R_sb, false)
            Box(modifier = Modifier.centerAt(pos.x, pos.y)) {
                TrainNode(minutes = min, isNorthbound = false)
            }
        }

        northbound.take(4).forEach { train ->
            val min = train.getMinutesRemaining(currentTimeSeconds)
            val d = min * pixelsPerMinute
            val pos = getTrainPosition(d, X_v_nb, Y_h_nb, Y_s, R_nb, true)
            Box(modifier = Modifier.centerAt(pos.x, pos.y)) {
                TrainNode(minutes = min, isNorthbound = true)
            }
        }
    }
}

@Composable
fun StationNode(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(80.dp)
            .height(24.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(4.dp, Color(0xFF222222), RoundedCornerShape(12.dp))
    )
}

@Composable
fun TrainNode(minutes: Long, isNorthbound: Boolean) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .shadow(8.dp, CircleShape)
            .background(GTrainGreen, CircleShape)
            .border(4.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (minutes <= 0) "0" else minutes.toString(),
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )
    }
}
