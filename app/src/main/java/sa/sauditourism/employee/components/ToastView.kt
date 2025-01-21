package sa.sauditourism.employee.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.resources.AppFonts
import kotlinx.coroutines.delay
import sa.sauditourism.employee.resources.AppColors

data class ToastPresentable(
    val type: ToastType,
    val title: String,
    val description: String? = null,
    val customIcon: Any? = null,
)

enum class ToastType {
    Error,
    Success,
    Default,
    Warning,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToastView(
    presentable: ToastPresentable,
    toastDuration: Long,
    onDismiss: () -> Unit,
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = true

    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { onDismiss() },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            excludeFromSystemGesture = true,
        ),
        content = {

            val dismissState = rememberSwipeToDismissBoxState()
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Transparent),
                    )
                }
            ) {
                AnimatedVisibility(
                    expandedState,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                ) {
                    ToastComponent(presentable, onDismiss, includePadding = true)
                    LaunchedEffect(Unit) {
                        delay(toastDuration)
                        onDismiss()
                    }
                }
            }
        },
    )
}

@Composable
fun ToastComponent(
    presentable: ToastPresentable,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    includePadding: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickableOnce { onDismiss() }
            .padding(if (includePadding) 24.dp else 0.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(getBackgroundColor(presentable.type)),
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp,
                )
                .fillMaxWidth(),
            verticalAlignment = if (presentable.description == null) Alignment.CenterVertically else Alignment.Top,
        ) {
            MediaComponent(
                mediaContent = MediaContent(
                    GalleryType.IMAGE,
                    presentable.customIcon ?: getIcon(presentable.type),
                ),
                imageOptions = ImageOptions(
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "",
                ),
                modifier = Modifier
                    .size(24.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
            ) {
                CustomTextComponent(
                    text = presentable.title,
                    style = AppFonts.labelSemiBold,
                    color = Color.White,
                )

                if (presentable.description != null) {
                    CustomTextComponent(
                        text = presentable.description,
                        style = AppFonts.captionRegular,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp),
                    )
                }
            }
        }
        /*  Image(
              painter = painterResource(id = R.drawable.toaser_brushes),
              contentDescription = null,
              modifier = Modifier
                  .fillMaxWidth()
                  .align(Alignment.BottomCenter)
                  .height(20.dp),
          )*/
    }
}

@Composable
fun getBackgroundColor(type: ToastType): Color {
    return when (type) {
        ToastType.Error -> AppColors.ToastRed
        ToastType.Success -> AppColors.ToastGreen
        ToastType.Default -> AppColors.OnPrimaryDark
        ToastType.Warning -> AppColors.ToastWarning
    }
}

@Composable
fun getIcon(type: ToastType): Int {
    return when (type) {
        ToastType.Error -> R.drawable.ic_error_icon
        ToastType.Success -> R.drawable.ic_check_icon
        ToastType.Default -> R.drawable.ic_information_icon
        ToastType.Warning -> R.drawable.ic_warning_icon
    }
}

@Preview(showBackground = true)
@Composable
fun ToastViewPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        val presentable = ToastPresentable(
            type = ToastType.Success,
            title = "Sample Toast",
        )

        ToastView(
            presentable = presentable,
            toastDuration = 5000L,
            onDismiss = {},
        )
    }
}