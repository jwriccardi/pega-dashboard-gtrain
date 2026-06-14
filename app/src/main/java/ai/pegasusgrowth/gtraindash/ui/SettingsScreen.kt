package ai.pegasusgrowth.gtraindash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen(
    currentSkin: DashboardSkin,
    onSkinSelected: (DashboardSkin) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("dashboard_settings", Context.MODE_PRIVATE) }
    
    var randomSkinEnabled by remember { mutableStateOf(prefs.getBoolean("random_skin", false)) }
    var minInterval by remember { mutableStateOf(prefs.getInt("random_min", 5)) }
    var maxInterval by remember { mutableStateOf(prefs.getInt("random_max", 360)) }
    
    var enabledSkins by remember { 
        val defaultSet = DashboardSkin.values().map { it.name }.toSet()
        val saved = prefs.getStringSet("enabled_skins", defaultSet) ?: defaultSet
        mutableStateOf(saved)
    }

    LaunchedEffect(randomSkinEnabled, minInterval, maxInterval, enabledSkins) {
        prefs.edit()
            .putBoolean("random_skin", randomSkinEnabled)
            .putInt("random_min", minInterval)
            .putInt("random_max", maxInterval)
            .putStringSet("enabled_skins", enabledSkins)
            .apply()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E1E1E))) {
        Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Dashboard Settings", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Button(onClick = onClose) {
                    Text("Close")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text("Select Skin", color = Color.Gray, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(DashboardSkin.values()) { skin ->
                    val isEnabled = enabledSkins.contains(skin.name)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f/9f)
                            .background(if (currentSkin == skin) Color(0xFF4C8C25) else Color.DarkGray, RoundedCornerShape(8.dp))
                            .clickable { onSkinSelected(skin) }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(skin.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isEnabled,
                                    onCheckedChange = { checked ->
                                        val newSet = enabledSkins.toMutableSet()
                                        if (checked) newSet.add(skin.name) else newSet.remove(skin.name)
                                        if (newSet.isNotEmpty()) {
                                            enabledSkins = newSet
                                        }
                                    }
                                )
                                Text("In Rotation", color = Color.LightGray, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Random Skin Rotation", color = Color.Gray, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = randomSkinEnabled,
                    onCheckedChange = { randomSkinEnabled = it }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Enable Random Rotation", color = Color.White, fontSize = 16.sp)
            }
            
            if (randomSkinEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Min Interval (mins): ", color = Color.White, fontSize = 16.sp)
                    Slider(
                        value = minInterval.toFloat(),
                        onValueChange = { minInterval = it.toInt().coerceAtMost(maxInterval) },
                        valueRange = 1f..360f,
                        modifier = Modifier.width(200.dp)
                    )
                    Text("$minInterval", color = Color.White, fontSize = 16.sp, modifier = Modifier.width(40.dp))
                    
                    Spacer(modifier = Modifier.width(32.dp))
                    
                    Text("Max Interval (mins): ", color = Color.White, fontSize = 16.sp)
                    Slider(
                        value = maxInterval.toFloat(),
                        onValueChange = { maxInterval = it.toInt().coerceAtLeast(minInterval) },
                        valueRange = 1f..360f,
                        modifier = Modifier.width(200.dp)
                    )
                    Text("$maxInterval", color = Color.White, fontSize = 16.sp, modifier = Modifier.width(40.dp))
                }
            }
        }
    }
}
