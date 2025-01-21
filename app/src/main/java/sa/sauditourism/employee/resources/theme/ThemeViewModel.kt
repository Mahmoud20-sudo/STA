package sa.sauditourism.employee.resources.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.resources.theme.model.AppTheme
import sa.sauditourism.employee.ui.theme.AppColors
import dagger.hilt.android.lifecycle.HiltViewModel
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
     realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    var themeState by mutableStateOf(ThemeState())

    init {
        fetchThemeColorsFromDatabase()
    }

    private fun fetchThemeColorsFromDatabase() {
        realTimeDatabase.fetchAppTheme(this)
    }

    fun onEvent(event: ThemeEvent) {
        when (event) {
            is ThemeEvent.ThemeChange -> {
                themeState = themeState.copy(theme = event.theme)
            }
        }
    }

    override fun onSwipeToRefresh() {
    }
}

sealed class ThemeEvent {
    data class ThemeChange(val theme: AppTheme) : ThemeEvent()
}

data class ThemeState(
    val theme: AppTheme = AppTheme(
        AppColors.PrimaryDark,
        AppColors.PrimaryLight,
        AppColors.SecondaryDark,
        AppColors.SecondaryLight,
        FirebaseConstants.REALTIME_DB_PROD_VALUE
    ),
)
