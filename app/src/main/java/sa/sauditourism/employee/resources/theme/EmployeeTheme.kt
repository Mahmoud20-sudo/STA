package sa.sauditourism.employee.resources.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import sa.sauditourism.employee.ui.theme.AppColors


/**
 * to be updated and to include light and dark modes
 */
@Composable
fun EmployeeTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    systemInDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkColors = darkColorScheme(
        primary = Color(themeViewModel.themeState.theme.darkPrimary.toColorInt()),
        onPrimary = AppColors.OnPrimaryDark,
        primaryContainer = AppColors.PrimaryContainerDark,
        onPrimaryContainer = AppColors.OnPrimaryContainerDark,
        secondary = Color(themeViewModel.themeState.theme.darkSecondary.toColorInt()),
        onSecondary = AppColors.OnSecondaryDark,
        secondaryContainer = AppColors.SecondaryContainerDark,
        onSecondaryContainer = AppColors.OnSecondaryContainerDark,
        tertiary = AppColors.TertiaryDark,
        onTertiary = AppColors.OnTertiaryDark,
        tertiaryContainer = AppColors.TertiaryContainerDark,
        onTertiaryContainer = AppColors.OnTertiaryContainerDark,
        error = AppColors.ErrorDark,
        onError = AppColors.OnErrorDark,
        errorContainer = AppColors.ErrorContainerDark,
        onErrorContainer = AppColors.OnErrorContainerDark,
        background = AppColors.BackgroundDark,
        onBackground = AppColors.OnBackgroundDark,
        surface = AppColors.SurfaceDark,
        outline = AppColors.OutlineDark,
        surfaceVariant = AppColors.SurfaceVariantDark,
        onSurfaceVariant = AppColors.OnSurfaceVariantDark,
        inverseSurface = Color.White,
        inverseOnSurface = Color.Black
    )

    val lightColors = lightColorScheme(
        primary = Color(themeViewModel.themeState.theme.lightPrimary.toColorInt()),
        onPrimary = AppColors.OnPrimaryLight,
        primaryContainer = AppColors.PrimaryContainerLight,
        onPrimaryContainer = AppColors.OnPrimaryContainerLight,
        secondary = Color(themeViewModel.themeState.theme.lightSecondary.toColorInt()),
        onSecondary = AppColors.OnSecondaryLight,
        secondaryContainer = AppColors.SecondaryContainerLight,
        onSecondaryContainer = AppColors.OnSecondaryContainerLight,
        tertiary = AppColors.TertiaryLight,
        onTertiary = AppColors.OnTertiaryLight,
        tertiaryContainer = AppColors.TertiaryContainerLight,
        onTertiaryContainer = AppColors.OnTertiaryContainerLight,
        error = AppColors.ErrorLight,
        onError = AppColors.OnErrorLight,
        errorContainer = AppColors.ErrorContainerLight,
        onErrorContainer = AppColors.OnErrorContainerLight,
        background = AppColors.BackgroundLight,
        onBackground = AppColors.OnBackgroundLight,
        surface = AppColors.SurfaceLight,
        outline = AppColors.OutlineLight,
        surfaceVariant = AppColors.SurfaceVariantLight,
        onSurfaceVariant = AppColors.OnSurfaceVariantLight,
        inverseSurface = Color.Black,
        inverseOnSurface = Color.White
    )


    val colors = if (systemInDarkTheme) {
        lightColors
    } else {
        lightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            // added to use it in date picker
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 6.sp,
                letterSpacing = 0.5.sp
            )
        ),
        content = content
    )

    // Optional, this part helps you set the statusbar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
//            WindowCompat.getInsetsController(window, view)
//                .isAppearanceLightStatusBars = !systemInDarkTheme
//            WindowCompat.getInsetsController(window, view)
//                .isAppearanceLightNavigationBars = !systemInDarkTheme
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !systemInDarkTheme
        }
    }

}