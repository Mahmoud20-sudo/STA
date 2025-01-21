package sa.sauditourism.employee.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.resources.AppFonts
import androidx.compose.runtime.remember as remember1

data class ProfileHeaderModel(
    val title: String? = null,
    val showAvatar: Boolean = false,
    val avatar: Any? = null,
    val showOverlay: Boolean = true
)

@Composable
fun ProfileHeaderComponent(
    profileHeaderModel: ProfileHeaderModel,
    modifier: Modifier = Modifier,
    showOverlay: Boolean = true,
    showDarkTextIcons: Boolean = true,
    showShadow: Boolean = true,
    onAvatarClick: () -> Unit = {},
) {
    val transition = updateTransition(targetState = showOverlay, label = "BackgroundTransition")
    var showBottomShadow by remember { mutableStateOf(false) }

    val background by transition.animateColor(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = if (showDarkTextIcons) 0 else 300)
            } else {
                tween(durationMillis = if (showDarkTextIcons) 0 else 300)
            }
        },
        label = "background",
    ) { isOverlay ->
        if (isOverlay) {
            showBottomShadow = false
            Color.Transparent
        } else {
            showBottomShadow = true
            MaterialTheme.colorScheme.background // Change this to your desired color when showOverlay is false
        }
    }

    val brush = remember1 {
        Brush.verticalGradient(
            colors = listOf(
                Color.Black.copy(alpha = if (profileHeaderModel.showOverlay) 0.6f else 0.0f),
                Color.Transparent,
            ),
            startY = 0f,
            endY = Float.POSITIVE_INFINITY,
        )
    }

    val isOverlay = transition.targetState

    Crossfade(
        animationSpec = tween(durationMillis = if (showDarkTextIcons) 0 else 300),
        targetState = isOverlay,
        label = "cross-fade",
    ) { isFadeOverlay ->
        val shadowModifier = Modifier
            .fillMaxWidth()

        Column {

            Column(
                modifier = shadowModifier
                    .then(
                        if (isFadeOverlay) {
                            Modifier.background(brush)
                        } else {
                            Modifier.background(
                                background,
                            )
                        },
                    )
                    .wrapContentHeight()
                    .then(modifier),
                verticalArrangement = Arrangement.Center,
            ) {
                val tintColor =
                    if (isFadeOverlay && !showDarkTextIcons) {
                        Color.White
                    } else if (SharedPreferencesManager(
                            LocalContext.current,
                        ).darkMode
                    ) {
                        Color.White
                    } else {
                        Color.Black
                    }

                Row(
                    modifier = Modifier.statusBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(24.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                    ) {
                        CustomTextComponent(
                            color = tintColor,
                            text = profileHeaderModel.title.orEmpty(),
                            modifier = Modifier,
                            style = AppFonts.headerBold,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    if (profileHeaderModel.showAvatar) {
                        val mediaContent by remember {
                            mutableStateOf(
                                MediaContent(
                                    GalleryType.IMAGE,
                                    profileHeaderModel.avatar
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clickableWithoutRipple { onAvatarClick.invoke() },
                            contentAlignment = Alignment.Center,
                        ) {
                            MediaComponent(
                                mediaContent = mediaContent,
                                modifier = Modifier
                                    .width(126.dp)
                                    .height(35.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }


            if (showBottomShadow)
                BottomShadow(0.15f, 2.dp)
        }
    }
}

@Composable
private fun BottomShadow(alpha: Float = 0.15f, height: Dp = 8.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Transparent,
                    )
                )
            )
    )
}
