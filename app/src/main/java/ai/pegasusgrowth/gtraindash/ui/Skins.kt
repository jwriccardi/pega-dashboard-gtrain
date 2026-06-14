package ai.pegasusgrowth.gtraindash.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.pegasusgrowth.gtraindash.R
import ai.pegasusgrowth.gtraindash.data.TrainArrivals
import java.text.SimpleDateFormat
import java.util.*

enum class DashboardSkin {
    PREMIUM_DARK, GREEN_BAR, DOT_MATRIX, SPATIAL, MAGRITTE, DALI, CHIRICO, JELLYFISH
}

// -------------------------------------------------------------------------------------
// SKIN 1: GREEN_BAR
// -------------------------------------------------------------------------------------
@Composable
fun GreenBarSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
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

    val sbDest = arrivals.southbound.firstOrNull()?.destination?.uppercase() ?: "CHURCH AV"
    val nbDest = arrivals.northbound.firstOrNull()?.destination?.uppercase() ?: "COURT SQ"

    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F1110))) {
        // TOP GREEN BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6CBE45))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text("G", color = Color(0xFF6CBE45), fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("MYRTLE-WILLOUGHBY AVS", color = Color(0xFF1A2A1A), fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, lineHeight = 32.sp)
                Text("IND CROSSTOWN LINE", color = Color(0xFF335522), fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
            // LIVE BADGE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFF111111)).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("LIVE", color = Color(0xFF6CBE45), fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(formattedTime, color = Color(0xFF1A2A1A), fontSize = 24.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }

        // CONTENT AREA
        Column(modifier = Modifier.weight(1f)) {
            // Southbound Row
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                // Left arrow strip
                Box(modifier = Modifier.width(120.dp).fillMaxHeight().background(Color(0xFF6CBE45)), contentAlignment = Alignment.Center) {
                    Text("▼", color = Color.Black, fontSize = 64.sp)
                }
                Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black))
                // Right content
                Row(modifier = Modifier.weight(1f).padding(32.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("SOUTHBOUND", color = Color(0xFF6CBE45), fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, letterSpacing = 2.sp)
                        Text(sbDest, color = Color.White, fontSize = 72.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.Bottom) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(sbMin1, color = Color(0xFF8CEE5F), fontSize = 100.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, style = TextStyle(shadow = Shadow(Color(0xFF8CEE5F), blurRadius = 30f)))
                            Text("MIN", color = Color.Gray, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(sbMin2, color = Color.White, fontSize = 100.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                            Text("MIN", color = Color.Gray, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFF222222)))
            // Northbound Row
            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                // Left arrow strip
                Box(modifier = Modifier.width(120.dp).fillMaxHeight().background(Color(0xFF6CBE45)), contentAlignment = Alignment.Center) {
                    Text("▲", color = Color.Black, fontSize = 64.sp)
                }
                Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black))
                // Right content
                Row(modifier = Modifier.weight(1f).padding(32.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("NORTHBOUND", color = Color(0xFF6CBE45), fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, letterSpacing = 2.sp)
                        Text(nbDest, color = Color.White, fontSize = 72.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.Bottom) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(nbMin1, color = Color(0xFF8CEE5F), fontSize = 100.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, style = TextStyle(shadow = Shadow(Color(0xFF8CEE5F), blurRadius = 30f)))
                            Text("MIN", color = Color.Gray, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(nbMin2, color = Color.White, fontSize = 100.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                            Text("MIN", color = Color.Gray, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------
// SKIN 2: DOT_MATRIX
// -------------------------------------------------------------------------------------
@Composable
fun DotMatrixSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val dotMatrixFont = FontFamily(Font(R.font.vt323))

    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds) }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds) }

    val sbMin1 = sbTrains.getOrNull(0)?.toString() ?: "--"
    val sbMin2 = sbTrains.getOrNull(1)?.toString() ?: "--"
    val nbMin1 = nbTrains.getOrNull(0)?.toString() ?: "--"
    val nbMin2 = nbTrains.getOrNull(1)?.toString() ?: "--"

    val sbDest = arrivals.southbound.firstOrNull()?.destination?.uppercase() ?: "CHURCH AV"
    val nbDest = arrivals.northbound.firstOrNull()?.destination?.uppercase() ?: "COURT SQ"

    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    val dotBrush = remember {
        val bitmap = ImageBitmap(4, 4)
        val canvas = ComposeCanvas(bitmap)
        val paint = Paint().apply { color = Color(0xFF6CBE45); isAntiAlias = true }
        canvas.drawCircle(Offset(2f, 2f), 1.5f, paint)
        ShaderBrush(ImageShader(bitmap, TileMode.Repeated, TileMode.Repeated))
    }

    ScaledLayout(onCycleSkin = onCycleSkin) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp)) {
        Box(modifier = Modifier.fillMaxSize().border(2.dp, Color(0xFF111111), RoundedCornerShape(16.dp)).padding(24.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("MYRTLE-WILLOUGHBY AVS · CROSSTOWN", color = Color.Gray, fontSize = 16.sp, fontFamily = dotMatrixFont, letterSpacing = 4.sp)
                    Text("🟢 $formattedTime", color = Color(0xFF558833), fontSize = 16.sp, fontFamily = dotMatrixFont)
                }
                Spacer(modifier = Modifier.height(32.dp))

                // Rows
                @Composable
                fun MatrixRow(index: String, dest: String, dir: String, mins: String) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(index, color = Color.DarkGray, fontSize = 24.sp, fontFamily = dotMatrixFont)
                        Spacer(modifier = Modifier.width(32.dp))
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF55BB33)), contentAlignment = Alignment.Center) {
                            Text("G", color = Color.Black, fontSize = 40.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(dest, fontSize = 64.sp, fontFamily = dotMatrixFont, lineHeight = 64.sp, style = TextStyle(brush = dotBrush))
                            Text(dir, color = Color.DarkGray, fontSize = 16.sp, fontFamily = dotMatrixFont, letterSpacing = 2.sp)
                        }
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(mins, fontSize = 100.sp, fontFamily = dotMatrixFont, style = TextStyle(brush = dotBrush, shadow = Shadow(Color(0xFF55BB33), blurRadius = 15f)))
                            Text(" MIN", fontSize = 24.sp, fontFamily = dotMatrixFont, modifier = Modifier.padding(bottom = 16.dp), style = TextStyle(brush = dotBrush))
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1A1A1A)))
                }

                MatrixRow("1", sbDest, "▼ SOUTHBOUND", sbMin1)
                MatrixRow("2", sbDest, "▼ SOUTHBOUND", sbMin2)
                MatrixRow("3", nbDest, "▲ NORTHBOUND", nbMin1)
                MatrixRow("4", nbDest, "▲ NORTHBOUND", nbMin2)
            }
        }
    }
    }
}

// -------------------------------------------------------------------------------------
// SKIN 3: SPATIAL
// -------------------------------------------------------------------------------------
@Composable
fun SpatialSkin(
    arrivals: TrainArrivals,
    currentTimeSeconds: Long,
    onCycleSkin: () -> Unit
) {
    val oswaldFontFamily = FontFamily(
        Font(R.font.oswald_regular, FontWeight.Normal),
        Font(R.font.oswald_bold, FontWeight.Bold)
    )

    val sbTrains = arrivals.southbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds) }
    val nbTrains = arrivals.northbound.take(2).map { it.getMinutesRemaining(currentTimeSeconds) }

    val sbMin1 = sbTrains.getOrNull(0)?.toString() ?: "--"
    val sbMin2 = sbTrains.getOrNull(1)?.toString() ?: "--"
    val nbMin1 = nbTrains.getOrNull(0)?.toString() ?: "--"
    val nbMin2 = nbTrains.getOrNull(1)?.toString() ?: "--"

    val sbDest = arrivals.southbound.firstOrNull()?.destination?.uppercase() ?: "CHURCH AV"
    val nbDest = arrivals.northbound.firstOrNull()?.destination?.uppercase() ?: "COURT SQ"

    val timeFormatter = remember { SimpleDateFormat("h:mm:ss a", Locale.US) }
    val formattedTime = remember(currentTimeSeconds) { timeFormatter.format(Date(currentTimeSeconds * 1000L)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1110))
            .drawBehind {
                val gridSize = 100f
                val stroke = Stroke(width = 1f)
                val color = Color(0xFF1A1D1A)
                for (x in 0..size.width.toInt() step gridSize.toInt()) {
                    drawLine(color, Offset(x.toFloat(), 0f), Offset(x.toFloat(), size.height), strokeWidth = 1f)
                }
                for (y in 0..size.height.toInt() step gridSize.toInt()) {
                    drawLine(color, Offset(0f, y.toFloat()), Offset(size.width, y.toFloat()), strokeWidth = 1f)
                }
                drawLine(Color(0xFF335522), Offset(0f, size.height * 0.7f), Offset(size.width, size.height * 0.3f), strokeWidth = 2f)
            }
            .padding(32.dp)
    ) {
        // Top Left
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            Text("MYRTLE-\nWILLOUGHBY AVS", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, lineHeight = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("IND CROSSTOWN · G LOCAL", color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)
        }

        // Top Right
        Row(modifier = Modifier.align(Alignment.TopEnd), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF6CBE45)), contentAlignment = Alignment.Center) {
                Text("G", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(formattedTime, color = Color(0xFF6CBE45), fontSize = 20.sp, fontFamily = FontFamily.Monospace)
        }

        // Bottom Left
        Text("NEXT TWO TRAINS · BOTH DIRECTIONS", color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp, modifier = Modifier.align(Alignment.BottomStart))

        // Giant Numbers
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.CenterStart).offset(x = 60.dp, y = 80.dp), horizontalAlignment = Alignment.Start) {
                Box(modifier = Modifier.background(Color(0xFF6CBE45), RoundedCornerShape(4.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("▼ $sbDest · NEXT", color = Color.Black, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
                Text(sbMin1, color = Color(0xFF8CEE5F), fontSize = 280.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, style = TextStyle(shadow = Shadow(Color(0xFF8CEE5F), blurRadius = 40f)), modifier = Modifier.offset(y = (-40).dp))
            }

            Column(modifier = Modifier.align(Alignment.BottomCenter).offset(x = (-100).dp, y = 0.dp), horizontalAlignment = Alignment.Start) {
                Box(modifier = Modifier.border(1.dp, Color(0xFF6CBE45), RoundedCornerShape(4.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("▼ $sbDest · THEN", color = Color(0xFF6CBE45), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Text(sbMin2, color = Color.White, fontSize = 140.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, modifier = Modifier.offset(y = (-20).dp))
            }

            Column(modifier = Modifier.align(Alignment.TopCenter).offset(x = 100.dp, y = 180.dp), horizontalAlignment = Alignment.Start) {
                Box(modifier = Modifier.background(Color(0xFF6CBE45), RoundedCornerShape(4.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("▲ $nbDest · NEXT", color = Color.Black, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
                Text(nbMin1, color = Color(0xFF6CBE45), fontSize = 240.sp, fontWeight = FontWeight.Bold, fontFamily = oswaldFontFamily, modifier = Modifier.offset(y = (-30).dp))
            }

            Column(modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-40).dp, y = 0.dp), horizontalAlignment = Alignment.Start) {
                Box(modifier = Modifier.border(1.dp, Color(0xFF6CBE45), RoundedCornerShape(4.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("▲ $nbDest · THEN", color = Color(0xFF6CBE45), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Box {
                    Text(
                        text = nbMin2,
                        fontSize = 180.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = oswaldFontFamily,
                        color = Color.Transparent,
                        style = TextStyle(drawStyle = Stroke(width = 4f, join = androidx.compose.ui.graphics.StrokeJoin.Round)),
                        modifier = Modifier.offset(y = (-20).dp)
                    )
                    Text(
                        text = nbMin2,
                        fontSize = 180.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = oswaldFontFamily,
                        color = Color(0xFF6CBE45),
                        style = TextStyle(drawStyle = Stroke(width = 4f, join = androidx.compose.ui.graphics.StrokeJoin.Round)),
                        modifier = Modifier.offset(y = (-20).dp)
                    )
                }
            }
        }
    }
}
