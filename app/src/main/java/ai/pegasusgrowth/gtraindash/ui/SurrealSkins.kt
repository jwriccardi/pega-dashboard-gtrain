package ai.pegasusgrowth.gtraindash.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import kotlin.math.roundToInt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.pegasusgrowth.gtraindash.data.TrainArrivals
import java.text.SimpleDateFormat
import java.util.*

// -------------------------------------------------------------------------------------
// SKIN 5: MAGRITTE (Ceci n'est pas un train)
// -------------------------------------------------------------------------------------
@Composable
fun MagritteSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF5E9EC9), Color(0xFF8FBEDC), Color(0xFFCADFE9))))
            .clickable { onCycleSkin() }
    ) {
        // Sun G
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = (-100).dp, y = 46.dp)
                .size(130.dp)
                .shadow(70.dp, ambientColor = Color(0x998CEE5F), spotColor = Color(0x998CEE5F), shape = CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFF8CD96A), Color(0xFF57A234)), center = Offset(0.35f, 0.3f)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("G", fontSize = 84.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Small sun cloud
        val smallCloudDx by rememberInfiniteTransition().animateFloat(
            initialValue = 0f, targetValue = 44f,
            animationSpec = infiniteRepeatable(animation = tween(10500, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
        )
        val smallCloudDy by rememberInfiniteTransition().animateFloat(
            initialValue = 0f, targetValue = -10f,
            animationSpec = infiniteRepeatable(animation = tween(10500, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
        )
        Box(modifier = Modifier.align(Alignment.TopCenter).offset(x = (-130).dp, y = 118.dp).offset { androidx.compose.ui.unit.IntOffset(smallCloudDx.roundToInt(), smallCloudDy.roundToInt()) }) {
            Box(modifier = Modifier.width(150.dp).height(44.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRoundRect(Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFE2ECF1))), size = Size(size.width, size.height), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2, size.height / 2))
                    drawCircle(Color.White, radius = 27.dp.toPx(), center = Offset(49.dp.toPx(), 3.dp.toPx()))
                    drawCircle(Color.White, radius = 20.dp.toPx(), center = Offset(104.dp.toPx(), 4.dp.toPx()))
                }
            }
        }

        // Placard
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 40.dp, y = 34.dp)
                .shadow(34.dp, ambientColor = Color(0x5914283C), spotColor = Color(0x5914283C))
                .background(Color(0xFFF6F3EA), RoundedCornerShape(6.dp))
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text("MYRTLE–WILLOUGHBY AVS", color = Color(0xFF17242E), fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text("oil on schedule, 2026", color = Color(0xFF5A6B76), fontSize = 16.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, modifier = Modifier.padding(top = 3.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF57A234)))
                Spacer(modifier = Modifier.width(8.dp))
                Text(formattedTime, color = Color(0xFF3A5A2C), fontFamily = FontFamily.Monospace, fontSize = 13.sp)
            }
        }

        // Caption
        Text("Ceci n'est pas un train.", modifier = Modifier.align(Alignment.BottomStart).offset(x = 54.dp, y = (-28).dp), fontSize = 34.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, color = Color(0xFF17242E))

        // Door to Night
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 50.dp)
                .width(140.dp)
                .height(222.dp)
                .shadow(44.dp, ambientColor = Color(0x7314283C), spotColor = Color(0x7314283C))
                .background(Brush.horizontalGradient(listOf(Color(0xFF6E5138), Color(0xFF4E3826))), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .padding(start = 11.dp, top = 11.dp, end = 11.dp, bottom = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A1120), RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    for (x in 0..size.width.toInt() step 34) {
                        for (y in 0..size.height.toInt() step 28) {
                            drawCircle(Color(0x80FFFFFF), 2f, Offset(x.toFloat(), y.toFloat()))
                        }
                    }
                }
                Box(modifier = Modifier.align(Alignment.TopCenter).offset(y = 37.dp).size(44.dp).shadow(28.dp, ambientColor = Color(0xD98CEE5F), spotColor = Color(0xD98CEE5F), shape = CircleShape).background(Color(0xFF57A234), CircleShape), contentAlignment = Alignment.Center) {
                    Text("G", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Text("ALREADY\nDEPARTED", color = Color(0xFF8CEE5F), fontSize = 10.sp, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.TopCenter).offset(y = 105.dp))
            }
            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 7.dp, y = 95.dp).size(9.dp).background(Color(0xFFC9A35B), CircleShape))
        }

        // Clouds implementation
        @Composable
        fun DriftingCloud(
            number: String,
            label: String,
            modifier: Modifier,
            fontSizePx: Int,
            baseWidthPx: Int,
            baseHeightPx: Int,
            overlapYPx: Int,
            animDurationMs: Int,
            isRev: Boolean
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val dx by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = if (isRev) -40f else 44f,
                animationSpec = infiniteRepeatable(animation = tween(animDurationMs / 2, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
            )
            val dy by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = if (isRev) -12f else -10f,
                animationSpec = infiniteRepeatable(animation = tween(animDurationMs / 2, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
            )

            Box(modifier = modifier.offset { androidx.compose.ui.unit.IntOffset(dx.roundToInt(), dy.roundToInt()) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = number,
                        fontSize = fontSizePx.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2D3A),
                        lineHeight = (fontSizePx * 0.8).sp,
                        modifier = Modifier.padding(bottom = 0.dp),
                        style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false))
                    )
                    Box(modifier = Modifier.offset(y = (-overlapYPx).dp).width(baseWidthPx.dp).height(baseHeightPx.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRoundRect(Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFDCE8EE))), size = Size(size.width, size.height), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2, size.height / 2))
                            drawCircle(Color.White, radius = (baseWidthPx * 0.18f).dp.toPx(), center = Offset((baseWidthPx * 0.31f).dp.toPx(), (-baseHeightPx * 0.1f).dp.toPx()))
                            drawCircle(Color.White, radius = (baseWidthPx * 0.13f).dp.toPx(), center = Offset((baseWidthPx * 0.7f).dp.toPx(), (-baseHeightPx * 0.0f).dp.toPx()))
                        }
                    }
                    Box(modifier = Modifier.offset(y = (-overlapYPx + 12).dp).background(Color(0x99FFFFFF), RoundedCornerShape(4.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text(label, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF1E3A4C))
                    }
                }
            }
        }

        DriftingCloud(sbTrains.getOrNull(0) ?: "--", "▼ CHURCH AV · NEXT", Modifier.align(Alignment.TopStart).offset(x = 70.dp, y = 130.dp), 200, 300, 86, 52, 16000, false)
        DriftingCloud(nbTrains.getOrNull(0) ?: "--", "▲ COURT SQ · NEXT", Modifier.align(Alignment.TopEnd).offset(x = (-100).dp, y = 95.dp), 170, 260, 76, 44, 19000, false)
        DriftingCloud(sbTrains.getOrNull(1) ?: "--", "▼ CHURCH AV · THEN", Modifier.align(Alignment.TopCenter).offset(x = (-100).dp, y = 355.dp), 120, 210, 64, 34, 17000, true)
        DriftingCloud(nbTrains.getOrNull(1) ?: "--", "▲ COURT SQ · THEN", Modifier.align(Alignment.TopEnd).offset(x = (-40).dp, y = 290.dp), 112, 200, 62, 32, 20000, true)
    }
}

// -------------------------------------------------------------------------------------
// SKIN 6: DALI (The Persistence of Departure)
// -------------------------------------------------------------------------------------
@Composable
fun DaliSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEBC796), Color(0xFFE29A72), Color(0xFF4A2C4A))))
            .clickable { onCycleSkin() }
    ) {
        // Horizon and Sun
        Canvas(modifier = Modifier.fillMaxSize()) {
            val horizonY = size.height * 0.6f
            drawRect(Color(0xFF4A2C4A), topLeft = Offset(0f, horizonY), size = Size(size.width, size.height - horizonY))
            drawCircle(Color(0xFFFFEEAA), radius = 120f, center = Offset(size.width * 0.65f, horizonY))
        }

        // Ledge Left
        Box(modifier = Modifier.fillMaxHeight(0.6f).width(280.dp).align(Alignment.BottomStart).background(Color(0xFF6B4432))) {
            Box(modifier = Modifier.fillMaxWidth().height(24.dp).background(Color(0xFF8C5F45)))
            // Dots
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(4) { Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF4A2C2A))) }
            }
            Text("▼ CHURCH AV · NEXT", modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp), color = Color(0xFFD4C4B4), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
        }

        // Dripping Number 1
        Box(modifier = Modifier.align(Alignment.CenterStart).offset(x = 60.dp, y = 40.dp)) {
            Text(sbTrains.getOrNull(0) ?: "--", fontSize = 320.sp, color = Color(0xFFF3EBD9), fontWeight = FontWeight.Black)
            Canvas(modifier = Modifier.size(120.dp, 200.dp).offset(y = 260.dp)) {
                drawRoundRect(Color(0xFFF3EBD9), topLeft = Offset(20f, 0f), size = Size(20f, 200f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f))
                drawRoundRect(Color(0xFFF3EBD9), topLeft = Offset(60f, 0f), size = Size(16f, 140f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f))
                drawRoundRect(Color(0xFFF3EBD9), topLeft = Offset(100f, 0f), size = Size(24f, 180f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f))
                drawCircle(Color(0xFFF3EBD9), 12f, Offset(112f, 220f))
            }
        }

        // Other Numbers
        Text(nbTrains.getOrNull(0) ?: "--", modifier = Modifier.align(Alignment.Center).offset(x = 40.dp, y = 140.dp), fontSize = 180.sp, color = Color(0xFFF3EBD9), fontWeight = FontWeight.Bold, style = TextStyle(shadow = Shadow(Color(0x882A1A2A), offset = Offset(10f, 40f), blurRadius = 10f)))
        Text(sbTrains.getOrNull(1) ?: "--", modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-240).dp, y = (-120).dp), fontSize = 140.sp, color = Color(0xFFDDBB99), fontWeight = FontWeight.Bold, style = TextStyle(shadow = Shadow(Color(0x882A1A2A), offset = Offset(15f, 20f), blurRadius = 8f)))
        Text(nbTrains.getOrNull(1) ?: "--", modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-120).dp, y = (-160).dp), fontSize = 100.sp, color = Color(0xFFAAA199), fontWeight = FontWeight.Bold, style = TextStyle(shadow = Shadow(Color(0x882A1A2A), offset = Offset(20f, 10f), blurRadius = 5f)))

        // Clock Dial
        Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-80).dp, y = 120.dp)) {
            Canvas(modifier = Modifier.size(240.dp, 120.dp)) {
                drawOval(Color(0xFFBCA160), topLeft = Offset(0f, 0f), size = Size(240f, 120f))
                drawOval(Color(0xFFF3EBD9), topLeft = Offset(8f, 8f), size = Size(224f, 104f))
                drawLine(Color(0xFFBCA160), Offset(120f, 120f), Offset(120f, 200f), strokeWidth = 8f)
            }
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LIVE", color = Color(0xFF8C5F45), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
                Text(formattedTime, color = Color(0xFF4A2C2A), fontSize = 24.sp, fontFamily = FontFamily.Monospace)
            }
        }

        // Labels
        Text("MYRTLE-WILLOUGHBY AVS · IND CROSSTOWN", modifier = Modifier.padding(32.dp).align(Alignment.TopStart), color = Color(0xFF6B4432), fontSize = 16.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)
        Text("The persistence of departure.", modifier = Modifier.padding(32.dp).align(Alignment.BottomStart), color = Color(0xFFF3EBD9), fontSize = 48.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)
    }
}

// -------------------------------------------------------------------------------------
// SKIN 7: CHIRICO (The Enigma of Arrival)
// -------------------------------------------------------------------------------------
@Composable
fun ChiricoSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B4D3E)) // Dark green sky
            .clickable { onCycleSkin() }
    ) {
        // Ground and Shadows
        Canvas(modifier = Modifier.fillMaxSize()) {
            val groundY = size.height * 0.6f
            drawRect(Color(0xFFD4A35B), topLeft = Offset(0f, groundY), size = Size(size.width, size.height - groundY))
            // Giant shadow
            val path = Path().apply {
                moveTo(0f, groundY)
                lineTo(size.width * 0.7f, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path, Color(0xFF8B5E34)) // Dark shadow
            
            // Building left shadow
            drawRect(Color(0xFF703A22), topLeft = Offset(0f, groundY), size = Size(size.width * 0.3f, size.height - groundY))
        }

        // Left Building
        Canvas(modifier = Modifier.fillMaxHeight(0.6f).fillMaxWidth(0.3f)) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.4f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path, Color(0xFFB86A44))
        }
        // Arches
        Row(modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 0.dp, start = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { idx ->
                Box(modifier = Modifier.width(60.dp).height(140.dp).background(Color(0xFF2A1510), RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)), contentAlignment = Alignment.BottomCenter) {
                    if (idx == 2) {
                        Text("G", color = Color(0xFF6CBE45), fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                    }
                }
            }
        }
        
        Text("MYRTLE-WILLOUGHBY AVS", modifier = Modifier.align(Alignment.CenterStart).offset(x = 24.dp, y = (-40).dp), color = Color(0xFF4A2C2A), fontSize = 16.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)

        // Right Tower
        Box(modifier = Modifier.align(Alignment.CenterEnd).offset(x = (-40).dp, y = 40.dp).width(80.dp).height(360.dp).background(Color(0xFF9E6542))) {
            Box(modifier = Modifier.fillMaxHeight().width(20.dp).align(Alignment.CenterStart).background(Color(0xFF703A22)))
            Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFF3EBD9)).align(Alignment.TopCenter).offset(y = 40.dp).border(4.dp, Color(0xFF4A2C2A), CircleShape), contentAlignment = Alignment.Center) {
                Text(formattedTime, color = Color(0xFF4A2C2A), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
            }
            // Flag
            Canvas(modifier = Modifier.align(Alignment.TopCenter).offset(y = (-30).dp).size(40.dp, 30.dp)) {
                drawLine(Color(0xFF2A1510), Offset(20f, 30f), Offset(20f, -20f), strokeWidth = 4f)
                val path = Path().apply { moveTo(20f, -20f); lineTo(40f, -10f); lineTo(20f, 0f); close() }
                drawPath(path, Color(0xFF337722))
            }
        }
        
        // Wall
        Box(modifier = Modifier.align(Alignment.Center).offset(y = 30.dp).fillMaxWidth(0.5f).height(20.dp).background(Color(0xFFB86A44)))
        Box(modifier = Modifier.align(Alignment.Center).offset(x = 60.dp, y = 10.dp).width(120.dp).height(20.dp).background(Color(0xFF4C8C3A), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))) {
            Row(modifier = Modifier.align(Alignment.Center).padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { Box(modifier = Modifier.size(12.dp).background(Color(0xFFF3EBD9))) }
            }
        }
        
        // Steam
        Canvas(modifier = Modifier.align(Alignment.Center).offset(x = 40.dp, y = (-40).dp).size(60.dp)) {
            drawCircle(Color(0x88FFFFFF), 8f, Offset(40f, 40f))
            drawCircle(Color(0x66FFFFFF), 12f, Offset(20f, 20f))
            drawCircle(Color(0x44FFFFFF), 16f, Offset(-10f, -10f))
        }

        // Numbers with Shadows
        @Composable
        fun StatuesqueNumber(number: String, size: Int, label: String, modifier: Modifier) {
            Box(modifier = modifier) {
                Text(number, color = Color(0x442A1510), fontSize = size.sp, fontWeight = FontWeight.Black, modifier = Modifier.offset(x = (-20).dp, y = 10.dp).clipToBounds()) // shadow
                Text(number, color = Color(0xFFF3EBD9), fontSize = size.sp, fontWeight = FontWeight.Black)
                Text(label, color = Color(0xFF2A1510), fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.align(Alignment.BottomCenter).offset(y = 20.dp))
            }
        }

        StatuesqueNumber(sbTrains.getOrNull(0) ?: "--", 260, "▼ CHURCH AV · NEXT", Modifier.align(Alignment.BottomStart).offset(x = 340.dp, y = (-40).dp))
        StatuesqueNumber(nbTrains.getOrNull(0) ?: "--", 160, "▲ COURT SQ · NEXT", Modifier.align(Alignment.BottomCenter).offset(x = 100.dp, y = (-80).dp))
        StatuesqueNumber(sbTrains.getOrNull(1) ?: "--", 120, "▼ CHURCH AV · THEN", Modifier.align(Alignment.BottomEnd).offset(x = (-240).dp, y = (-140).dp))
        StatuesqueNumber(nbTrains.getOrNull(1) ?: "--", 80, "▲ COURT SQ · THEN", Modifier.align(Alignment.BottomEnd).offset(x = (-120).dp, y = (-180).dp))

        Text("L'enigma dell'arrivo.", modifier = Modifier.padding(32.dp).align(Alignment.BottomStart), color = Color(0xFFF3EBD9), fontSize = 48.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)
    }
}

// -------------------------------------------------------------------------------------
// SKIN 8: JELLYFISH (The Crosstown Current)
// -------------------------------------------------------------------------------------
@Composable
fun JellyfishSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F2C3A), Color(0xFF0A1B24))))
            .clickable { onCycleSkin() }
    ) {
        // Light Rays
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path1 = Path().apply { moveTo(size.width * 0.2f, 0f); lineTo(size.width * 0.5f, 0f); lineTo(size.width * 0.3f, size.height); lineTo(0f, size.height); close() }
            drawPath(path1, Brush.linearGradient(listOf(Color(0x228CEE5F), Color.Transparent)))
            val path2 = Path().apply { moveTo(size.width * 0.6f, 0f); lineTo(size.width * 0.8f, 0f); lineTo(size.width * 0.5f, size.height); lineTo(size.width * 0.3f, size.height); close() }
            drawPath(path2, Brush.linearGradient(listOf(Color(0x118CEE5F), Color.Transparent)))
            
            // Fish
            drawOval(Color(0xFF225544), topLeft = Offset(size.width * 0.6f, size.height * 0.7f), size = Size(30f, 10f))
            drawOval(Color(0xFF225544), topLeft = Offset(size.width * 0.63f, size.height * 0.73f), size = Size(24f, 8f))
            drawOval(Color(0xFF225544), topLeft = Offset(size.width * 0.59f, size.height * 0.76f), size = Size(28f, 9f))
            drawOval(Color(0xFF225544), topLeft = Offset(size.width * 0.64f, size.height * 0.79f), size = Size(20f, 7f))
        }

        // Top Right Badge
        Row(
            modifier = Modifier.align(Alignment.TopEnd).padding(32.dp).background(Color(0x44000000), RoundedCornerShape(24.dp)).border(1.dp, Color(0x448CEE5F), RoundedCornerShape(24.dp)).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF6CBE45)), contentAlignment = Alignment.Center) {
                Text("G", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("MYRTLE-WILLOUGHBY AVS", color = Color.White, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(12.dp))
            Text(formattedTime, color = Color(0xFF8CEE5F), fontSize = 14.sp, fontFamily = FontFamily.Monospace)
        }

        // Jellyfish
        @Composable
        fun Jellyfish(number: String, label: String, colorStart: Color, colorEnd: Color, sizeDp: Int, modifier: Modifier) {
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size((sizeDp * 1.5).dp, sizeDp.dp)
                        .background(Brush.verticalGradient(listOf(colorStart, colorEnd)), RoundedCornerShape(topStart = sizeDp.dp, topEnd = sizeDp.dp, bottomStart = (sizeDp*0.2).dp, bottomEnd = (sizeDp*0.2).dp))
                        .shadow(40.dp, ambientColor = colorStart, spotColor = colorStart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(number, color = Color(0xFF0F2C3A), fontSize = (sizeDp * 0.8).sp, fontWeight = FontWeight.Black)
                }
                Canvas(modifier = Modifier.size((sizeDp).dp, (sizeDp * 0.8).dp).offset(y = (-4).dp)) {
                    val w = size.width
                    val h = size.height
                    drawLine(colorEnd, Offset(w*0.2f, 0f), Offset(w*0.2f, h), strokeWidth = 4f)
                    drawLine(colorEnd, Offset(w*0.4f, 0f), Offset(w*0.4f, h*0.8f), strokeWidth = 4f)
                    drawLine(colorEnd, Offset(w*0.6f, 0f), Offset(w*0.6f, h*0.9f), strokeWidth = 4f)
                    drawLine(colorEnd, Offset(w*0.8f, 0f), Offset(w*0.8f, h*0.7f), strokeWidth = 4f)
                }
                Text(label, color = Color(0xFFAABBCC), fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            }
        }

        Jellyfish(sbTrains.getOrNull(0) ?: "--", "▼ CHURCH AV · NEXT", Color(0xFF8CEE5F), Color(0x664C8C3A), 160, Modifier.align(Alignment.CenterStart).offset(x = 120.dp, y = (-40).dp))
        Jellyfish(nbTrains.getOrNull(0) ?: "--", "▲ COURT SQ · NEXT", Color(0xFF8CEE5F), Color(0x664C8C3A), 120, Modifier.align(Alignment.TopEnd).offset(x = (-300).dp, y = 140.dp))
        Jellyfish(sbTrains.getOrNull(1) ?: "--", "▼ CHURCH AV · THEN", Color(0xFF4C8C3A), Color(0x334C8C3A), 100, Modifier.align(Alignment.BottomCenter).offset(x = (-100).dp, y = (-80).dp))
        Jellyfish(nbTrains.getOrNull(1) ?: "--", "▲ COURT SQ · THEN", Color(0xFF4CAACC), Color(0x334CAACC), 90, Modifier.align(Alignment.CenterEnd).offset(x = (-120).dp, y = 80.dp))

        // Porthole
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(32.dp).size(160.dp).background(Color(0xFF2A1B14), CircleShape).border(12.dp, Color(0xFFBCA160), CircleShape), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.size(120.dp).background(Brush.radialGradient(listOf(Color(0xFF225544), Color(0xFF0F2C3A))), CircleShape).border(4.dp, Color(0xFF111111), CircleShape), contentAlignment = Alignment.Center) {
                Text("G", color = Color(0xFF8CEE5F), fontSize = 72.sp, fontWeight = FontWeight.Bold)
            }
            // Porthole bolts
            val boltOffset = 64f
            Canvas(modifier = Modifier.fillMaxSize()) {
                val c = Offset(size.width/2, size.height/2)
                drawCircle(Color(0xFF8C5F45), 6f, c.copy(y = c.y - boltOffset))
                drawCircle(Color(0xFF8C5F45), 6f, c.copy(y = c.y + boltOffset))
                drawCircle(Color(0xFF8C5F45), 6f, c.copy(x = c.x - boltOffset))
                drawCircle(Color(0xFF8C5F45), 6f, c.copy(x = c.x + boltOffset))
            }
        }

        Text("Local via the Crosstown Current.", modifier = Modifier.align(Alignment.BottomStart).offset(x = 220.dp, y = (-70).dp), color = Color(0xFFDDEEFF), fontSize = 36.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)
    }
}
