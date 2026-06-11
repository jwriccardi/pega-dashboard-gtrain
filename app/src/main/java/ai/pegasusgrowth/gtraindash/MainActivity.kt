package ai.pegasusgrowth.gtraindash

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ai.pegasusgrowth.gtraindash.ui.DashboardScreen
import ai.pegasusgrowth.gtraindash.ui.DashboardViewModel
import ai.pegasusgrowth.gtraindash.ui.theme.GTrainDashboardTheme

class MainActivity : ComponentActivity() {
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enforce always-on screen behavior for kiosk mounting
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Enable edge-to-edge content rendering
        enableEdgeToEdge()

        // Configure Immersive Mode to hide status and navigation bars
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = 
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            GTrainDashboardTheme {
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}