package not.djinni.presentation

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import not.djinni.presentation.navigation.NavigationController.Companion.rememberNavigationController
import not.djinni.presentation.navigation.NotDjinniNavDisplay
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val systemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        installSplashScreen().apply {
            setOnExitAnimationListener { it.remove() }
            setKeepOnScreenCondition { false }
        }
        enableEdgeToEdge(
            statusBarStyle = systemBarStyle,
            navigationBarStyle = systemBarStyle
        )
        setContent {
            val view = LocalView.current
            NotDjinniTheme {
                val controller = rememberNavigationController(
                    onRootBack = this@MainActivity::finish,
                )

                NotDjinniNavDisplay(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NotDjinniTheme.colors.background),
                    controller = controller
                )
            }

            LaunchedEffect(Unit) {
                view.setSystemBarStyle(isLight = true)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration).apply { fontScale = 1f }
        applyOverrideConfiguration(newOverride)
        super.attachBaseContext(newBase)
    }

    private fun View.setSystemBarStyle(isLight: Boolean) {
        if (isInEditMode) return
        val window = (context as? Activity)?.window ?: return
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, this).run {
            isAppearanceLightNavigationBars = !isLight
            isAppearanceLightStatusBars = !isLight
        }
    }
}
