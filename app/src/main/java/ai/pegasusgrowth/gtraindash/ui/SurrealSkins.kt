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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontFamily
import ai.pegasusgrowth.gtraindash.R
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
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
    val oswaldFont = FontFamily(Font(R.font.oswald_bold))
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
            Text("G", fontSize = 84.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color.White)
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

// Extension for skew
fun Modifier.skewX(degrees: Float, pivotX: Float = 0f, pivotY: Float = 1f) = this.drawWithContent {
    val rad = Math.toRadians(degrees.toDouble()).toFloat()
    val tan = kotlin.math.tan(rad.toDouble()).toFloat()
    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(size.width * pivotX, size.height * pivotY)
        canvas.skew(tan, 0f)
        canvas.translate(-size.width * pivotX, -size.height * pivotY)
        drawContent()
        canvas.restore()
    }
}

@Composable
fun DaliSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds).toString() }
    val oswaldFont = FontFamily(Font(R.font.oswald_bold))
    val timeFormatter = remember { java.text.SimpleDateFormat("h:mm:ss a", java.util.Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(java.util.Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF6DCA8), Color(0xFFF0B57E), Color(0xFFE08A5C))))
            .clickable { onCycleSkin() }
    ) {
        // Low sun
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 750.dp, y = 292.dp)
                .size(120.dp)
                .shadow(90.dp, ambientColor = Color(0xD9FFDC8C), spotColor = Color(0xD9FFDC8C), shape = CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFFFFF3C9), Color(0xFFFFD98E)), center = Offset(0.45f, 0.4f)), CircleShape)
        )

        // Ground
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(280.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF93525F), Color(0xFF5A3158), Color(0xFF2E1B3E))))
        )

        // Ledge
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 318.dp)
                .width(330.dp)
                .height(346.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF8A573B), Color(0xFF5E3A2A))))
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(22.dp).background(Color(0xFFB27A4E)))
            Row(modifier = Modifier.padding(top = 28.dp, start = 30.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(13) { Box(modifier = Modifier.size(6.dp).background(Color(0xFF2A1B25), CircleShape)) }
            }
        }

        // Clock watch on a branch
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 920.dp, y = 96.dp).width(290.dp).height(4.dp).graphicsLayer(rotationZ = 10f).background(Color(0xFF4A2C22)))
        
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 905.dp, y = 120.dp)
                .size(230.dp, 140.dp)
                .shadow(40.dp, ambientColor = Color(0x803C1E32), spotColor = Color(0x803C1E32), shape = CircleShape)
                .background(Color(0xFFF8EFD9), CircleShape)
                .border(7.dp, Color(0xFFC9A35B), CircleShape)
                .graphicsLayer(rotationZ = -16f).skewX(-8f, 0.5f, 0.5f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.skewX(8f, 0.5f, 0.5f).graphicsLayer(rotationZ = 4f)) {
                Text("LIVE", color = Color(0xFFA0743C), fontSize = 10.sp, fontFamily = FontFamily.Monospace, letterSpacing = 3.sp)
                Text(formattedTime, color = Color(0xFF4A3A28), fontSize = 22.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
            }
        }

        @Composable
        fun Drip(x: Int, y: Int, w: Int, h: Int, color: Color, duration: Int) {
            val scaleY by rememberInfiniteTransition().animateFloat(
                initialValue = 1f, targetValue = 1.4f,
                animationSpec = infiniteRepeatable(animation = tween(duration / 2, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = x.dp, y = y.dp)
                    .width(w.dp)
                    .height((h * scaleY).dp)
                    .background(color, RoundedCornerShape(bottomStart = (w/2).dp, bottomEnd = (w/2).dp))
            )
        }

        Drip(982, 252, 10, 60, Color(0xFFC9A35B), 8000)
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 981.dp, y = 316.dp).size(11.dp).background(Color(0xFFC9A35B), CircleShape))

        // Melting 2
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 90.dp, y = 134.dp).height(184.dp).clipToBounds()) {
            Text(
                text = sbTrains.getOrNull(0) ?: "--", 
                fontSize = 240.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFFBEED3), 
                modifier = Modifier.skewX(-3f).graphicsLayer(scaleY = 1.12f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)),
                style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false), shadow = Shadow(Color(0x595A281E), Offset(0f, 6f), 18f))
            )
        }
        Drip(120, 318, 14, 120, Color(0xFFFBEED3), 7000)
        Drip(178, 318, 12, 70, Color(0xFFFBEED3), 9000)
        Drip(236, 318, 16, 150, Color(0xFFFBEED3), 11000)
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 237.dp, y = 512.dp).size(14.dp).background(Color(0xFFFBEED3), CircleShape))
        Text("▼ CHURCH AV · NEXT", modifier = Modifier.align(Alignment.TopStart).offset(x = 96.dp, y = 556.dp), color = Color(0xFFE8C9A8), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)

        // Standing 4 + shadow
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 600.dp, y = 334.dp)) {
            Text(nbTrains.getOrNull(0) ?: "--", fontSize = 170.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x6628122A), modifier = Modifier.offset(x = 60.dp, y = 80.dp).graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(54f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(nbTrains.getOrNull(0) ?: "--", fontSize = 170.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFFBEED3), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
        }
        Text("▲ COURT SQ · NEXT", modifier = Modifier.align(Alignment.TopStart).offset(x = 600.dp, y = 486.dp), color = Color(0xFFE8C9A8), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)

        // 9 + shadow
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 868.dp, y = 357.dp)) {
            Text(sbTrains.getOrNull(1) ?: "--", fontSize = 110.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x6128122A), modifier = Modifier.offset(x = 40.dp, y = 50.dp).graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(54f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(sbTrains.getOrNull(1) ?: "--", fontSize = 110.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFF4DEC4), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
        }
        Text("▼ CHURCH AV · THEN", modifier = Modifier.align(Alignment.TopStart).offset(x = 868.dp, y = 455.dp), color = Color(0xFFD9B393), fontSize = 11.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)

        // 11 + shadow
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 1014.dp, y = 366.dp)) {
            Text(nbTrains.getOrNull(1) ?: "--", fontSize = 64.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x5928122A), modifier = Modifier.offset(x = 20.dp, y = 30.dp).graphicsLayer(scaleY = 0.3f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(54f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(nbTrains.getOrNull(1) ?: "--", fontSize = 64.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFEFD2AE), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
        }
        Text("▲ COURT SQ · THEN", modifier = Modifier.align(Alignment.TopStart).offset(x = 1010.dp, y = 426.dp), color = Color(0xFFCBA384), fontSize = 10.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)

        Text("MYRTLE–WILLOUGHBY AVS · IND CROSSTOWN", modifier = Modifier.align(Alignment.TopStart).offset(x = 40.dp, y = 34.dp), color = Color(0xFF6E3F2A), fontSize = 13.sp, fontFamily = FontFamily.Monospace, letterSpacing = 3.sp)
        Text("The persistence of departure.", modifier = Modifier.align(Alignment.BottomStart).offset(x = 40.dp, y = (-30).dp), color = Color(0xFFF4DEC4), fontSize = 33.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)
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
    val oswaldFont = FontFamily(Font(R.font.oswald_bold))
    val timeFormatter = remember { java.text.SimpleDateFormat("h:mm:ss a", java.util.Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(java.util.Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F3A38), Color(0xFF2E6A52), Color(0xFFA8B468))))
            .clickable { onCycleSkin() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(270.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFFD09A45), Color(0xFFB97F32))))
        )
        
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 700.dp, y = 348.dp).width(150.dp).height(30.dp).background(Color(0xFF3F8A26), RoundedCornerShape(8.dp)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 712.dp, y = 356.dp).width(20.dp).height(12.dp).background(Color(0xFFF0F4F0)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 744.dp, y = 356.dp).width(20.dp).height(12.dp).background(Color(0xFFF0F4F0)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 776.dp, y = 356.dp).width(20.dp).height(12.dp).background(Color(0xFFF0F4F0)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 808.dp, y = 356.dp).width(20.dp).height(12.dp).background(Color(0xFFF0F4F0)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 690.dp, y = 344.dp).width(6.dp).height(14.dp).background(Color(0xFF1E1E1E)))
        
        val steamRise by rememberInfiniteTransition().animateFloat(
            initialValue = 1f, targetValue = 0f,
            animationSpec = infiniteRepeatable(animation = tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Restart)
        )
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 680.dp, y = (330 - steamRise*40).dp.value.dp).size(24.dp).background(Color(0x88FFFFFF), CircleShape))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 660.dp, y = (310 - steamRise*60).dp.value.dp).size(36.dp).background(Color(0x55FFFFFF), CircleShape))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 620.dp, y = (280 - steamRise*80).dp.value.dp).size(50.dp).background(Color(0x22FFFFFF), CircleShape))

        Box(modifier = Modifier.align(Alignment.TopStart).offset(y = 370.dp).fillMaxWidth().height(26.dp).background(Color(0xFF7D4E3A)))

        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(220.dp.toPx(), 396.dp.toPx())
                lineTo(1180.dp.toPx(), 260.dp.toPx())
                lineTo(1180.dp.toPx(), 664.dp.toPx())
                lineTo(340.dp.toPx(), 664.dp.toPx())
                close()
            }
            drawPath(path, Color(0xFF6E4A25))
        }

        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = (-30).dp).width(250.dp).fillMaxHeight().background(Color(0xFFA66D4F)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = (-10).dp, y = 340.dp).width(110.dp).height(340.dp).background(Color(0xFF2E1812), RoundedCornerShape(topStart = 55.dp, topEnd = 55.dp)))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 130.dp, y = 340.dp).width(110.dp).height(340.dp).background(Color(0xFF2E1812), RoundedCornerShape(topStart = 55.dp, topEnd = 55.dp)))

        Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-80).dp).width(200.dp).height(240.dp).background(Color(0xFFD49A6A)))
        Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-260).dp).width(20.dp).height(240.dp).background(Color(0xFF8B5E34)))
        Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-130).dp, y = 140.dp).size(100.dp).background(Color(0xFFF6F3EA), CircleShape).border(6.dp, Color(0xFF2E1812), CircleShape), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LIVE", color = Color(0xFFA66D4F), fontSize = 10.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
                Text(formattedTime, color = Color(0xFF2E1812), fontSize = 14.sp, fontFamily = FontFamily.Monospace)
            }
        }
        
        Canvas(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-210).dp, y = (-20).dp).size(80.dp, 60.dp)) {
            drawLine(Color(0xFF2E1812), Offset(20f, 60f), Offset(20f, -40f), strokeWidth = 6f)
            val path1 = Path().apply { moveTo(20f, -40f); lineTo(80f, -20f); lineTo(20f, 0f); close() }
            drawPath(path1, Color(0xFFD34F3F))
            val path2 = Path().apply { moveTo(20f, 10f); lineTo(70f, 25f); lineTo(20f, 40f); close() }
            drawPath(path2, Color(0xFFF0D568))
        }

        Text("MYRTLE–WILLOUGHBY AVS · IND CROSSTOWN", modifier = Modifier.align(Alignment.TopStart).offset(x = 24.dp, y = 24.dp), color = Color(0xFF2E1812), fontSize = 14.sp, fontFamily = FontFamily.Monospace, letterSpacing = 3.sp)
        Text("L'enigma dell'arrivo.", modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-40).dp, y = (-24).dp), color = Color(0xFFC9923E), fontSize = 34.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)

        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 340.dp, y = 410.dp)) {
            Text(sbTrains.getOrNull(0) ?: "--", fontSize = 160.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x662E1812), modifier = Modifier.offset(x = 140.dp, y = 80.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(sbTrains.getOrNull(0) ?: "--", fontSize = 160.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFE8D5C4), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text("▼ CHURCH AV · NEXT", modifier = Modifier.offset(y = 150.dp), color = Color(0xFF4A281E), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
        }
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 610.dp, y = 460.dp)) {
            Text(nbTrains.getOrNull(0) ?: "--", fontSize = 120.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x662E1812), modifier = Modifier.offset(x = 100.dp, y = 60.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(nbTrains.getOrNull(0) ?: "--", fontSize = 120.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFE8D5C4), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text("▲ COURT SQ · NEXT", modifier = Modifier.offset(y = 110.dp), color = Color(0xFF4A281E), fontSize = 11.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
        }
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 810.dp, y = 500.dp)) {
            Text(sbTrains.getOrNull(1) ?: "--", fontSize = 90.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x662E1812), modifier = Modifier.offset(x = 70.dp, y = 50.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(sbTrains.getOrNull(1) ?: "--", fontSize = 90.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFD4A35B), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text("▼ CHURCH AV · THEN", modifier = Modifier.offset(y = 80.dp), color = Color(0xFF2E1812), fontSize = 10.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
        }
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 1000.dp, y = 540.dp)) {
            Text(nbTrains.getOrNull(1) ?: "--", fontSize = 70.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0x662E1812), modifier = Modifier.offset(x = 50.dp, y = 40.dp).graphicsLayer(scaleY = 0.4f, transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f)).skewX(64f), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text(nbTrains.getOrNull(1) ?: "--", fontSize = 70.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFont, color = Color(0xFFD4A35B), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
            Text("▲ COURT SQ · THEN", modifier = Modifier.offset(y = 60.dp), color = Color(0xFF2E1812), fontSize = 9.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
        }
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
    val oswaldFont = FontFamily(Font(R.font.oswald_bold))
    val timeFormatter = remember { java.text.SimpleDateFormat("h:mm:ss a", java.util.Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(java.util.Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0E3A4A), Color(0xFF0A2538), Color(0xFF061722))))
            .clickable { onCycleSkin() }
    ) {
        val sway by rememberInfiniteTransition().animateFloat(
            initialValue = 0.35f, targetValue = 0.75f,
            animationSpec = infiniteRepeatable(animation = tween(4500, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
        )
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 200.dp, y = (-80).dp).size(140.dp, 520.dp).graphicsLayer(rotationZ = 14f, alpha = sway).background(Brush.verticalGradient(listOf(Color(0x24BEFFE6), Color.Transparent))))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 520.dp, y = (-80).dp).size(110.dp, 560.dp).graphicsLayer(rotationZ = 15f, alpha = sway*0.8f).background(Brush.verticalGradient(listOf(Color(0x1EBEFFE6), Color.Transparent))))
        Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 830.dp, y = (-80).dp).size(150.dp, 500.dp).graphicsLayer(rotationZ = 13f, alpha = sway*0.9f).background(Brush.verticalGradient(listOf(Color(0x1ABEFFE6), Color.Transparent))))

        @Composable
        fun Bubble(x: Int, size: Int, duration: Int) {
            val rise by rememberInfiniteTransition().animateFloat(
                initialValue = 0f, targetValue = -800f,
                animationSpec = infiniteRepeatable(animation = tween(duration, easing = LinearEasing), repeatMode = RepeatMode.Restart)
            )
            Box(modifier = Modifier.align(Alignment.BottomStart).offset(x = x.dp, y = (60 + rise).dp.value.dp).size(size.dp).border(1.5.dp, Color(0x55FFFFFF), CircleShape))
        }
        Bubble(100, 18, 14000)
        Bubble(330, 10, 11000)
        Bubble(560, 26, 17000)
        Bubble(760, 14, 12000)
        Bubble(985, 20, 15000)
        Bubble(1110, 12, 10000)

        @Composable
        fun Jelly(number: String, label: String, x: Int, y: Int, w: Int, h: Int, colorStart: Color, colorEnd: Color, textCol: Color, animDuration: Int) {
            val bob by rememberInfiniteTransition().animateFloat(
                initialValue = 0f, targetValue = -18f,
                animationSpec = infiniteRepeatable(animation = tween(animDuration / 2, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
            )
            val rotate by rememberInfiniteTransition().animateFloat(
                initialValue = -2f, targetValue = 2f,
                animationSpec = infiniteRepeatable(animation = tween(animDuration / 2, easing = LinearEasing), repeatMode = RepeatMode.Reverse)
            )
            
            Box(modifier = Modifier.align(Alignment.TopStart).offset(x = x.dp, y = y.dp).offset(y = bob.dp.value.dp).graphicsLayer(rotationZ = rotate)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(w.dp, h.dp).background(Brush.radialGradient(listOf(colorStart, colorEnd), center = Offset(0.5f, 0.3f)), RoundedCornerShape(topStart = (w/2).dp, topEnd = (w/2).dp, bottomStart = 16.dp, bottomEnd = 16.dp)).shadow(50.dp, ambientColor = colorStart.copy(alpha = 0.35f), spotColor = colorStart.copy(alpha = 0.35f)), contentAlignment = Alignment.Center) {
                        Text(number, fontSize = (w*0.5).sp, fontWeight = FontWeight.Bold, color = textCol, modifier = Modifier.offset(y = 6.dp), style = TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy((w*0.06).dp), modifier = Modifier.padding(top = 5.dp)) {
                        repeat(5) {
                            val dripScale by rememberInfiniteTransition().animateFloat(initialValue = 1f, targetValue = 1.4f, animationSpec = infiniteRepeatable(animation = tween(5000 + it*500, easing = LinearEasing), repeatMode = RepeatMode.Reverse))
                            Box(modifier = Modifier.width(5.dp).height((h * 0.4f * dripScale).dp.value.dp).background(colorStart.copy(alpha = 0.5f), RoundedCornerShape(3.dp)))
                        }
                    }
                    Text(label, modifier = Modifier.padding(top = 12.dp), color = Color(0xFF7FD9C9), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
                }
            }
        }

        Jelly(sbTrains.getOrNull(0) ?: "--", "▼ CHURCH AV · NEXT", 140, 120, 190, 150, Color(0xF28CEE5F), Color(0x4D6CBE45), Color(0xFF06281E), 7000)
        Jelly(nbTrains.getOrNull(0) ?: "--", "▲ COURT SQ · NEXT", 660, 90, 170, 134, Color(0xF28CEE5F), Color(0x4D6CBE45), Color(0xFF06281E), 8000)
        Jelly(sbTrains.getOrNull(1) ?: "--", "▼ CHURCH AV · THEN", 430, 330, 130, 102, Color(0xB3A0DCFF), Color(0x3878B4DC), Color(0xFF0A2538), 9000)
        Jelly(nbTrains.getOrNull(1) ?: "--", "▲ COURT SQ · THEN", 920, 300, 120, 94, Color(0xB3A0DCFF), Color(0x3878B4DC), Color(0xFF0A2538), 8500)

        Box(modifier = Modifier.align(Alignment.BottomCenter).offset(y = 120.dp).size(240.dp).background(Color(0xFF040E16), CircleShape).border(12.dp, Color(0xFF1E3A4A), CircleShape)) {
            Box(modifier = Modifier.align(Alignment.Center).size(80.dp).background(Color(0xFF6CBE45), CircleShape).shadow(40.dp, ambientColor = Color(0xD98CEE5F), spotColor = Color(0xD98CEE5F)), contentAlignment = Alignment.Center) {
                Text("G", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            }
        }

        Row(modifier = Modifier.align(Alignment.TopStart).padding(34.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(46.dp).background(Color(0xFF6CBE45), CircleShape), contentAlignment = Alignment.Center) {
                Text("G", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text("MYRTLE–WILLOUGHBY AVS", color = Color(0xFFDFF2EC), fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Text("IND CROSSTOWN", color = Color(0xFF7FD9C9), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
            }
        }
        
        Row(modifier = Modifier.align(Alignment.TopEnd).padding(34.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("LIVE", color = Color(0xFF7FD9C9), fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(formattedTime, color = Color(0xFF6CBE45), fontSize = 16.sp, fontFamily = FontFamily.Monospace)
        }

        Text("Every orbit returns to Court Sq.", modifier = Modifier.align(Alignment.BottomStart).padding(40.dp), color = Color(0xFFC9BFE8), fontSize = 30.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)
    }
}