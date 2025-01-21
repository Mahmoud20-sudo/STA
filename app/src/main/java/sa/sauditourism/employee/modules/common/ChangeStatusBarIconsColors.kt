package sa.sauditourism.employee.modules.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import sa.sauditourism.employee.LocalLoggedIn

@Composable
fun ChangeStatusBarIconsColors(color: Color = Color.Transparent) {
    val view = LocalView.current
    val localLoggedIn = LocalLoggedIn.current
    val window = (view.context as Activity).window
    window.navigationBarColor = Color.Transparent.toArgb()
    window.statusBarColor = color.toArgb()
    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = localLoggedIn
}
