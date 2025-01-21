package sa.sauditourism.employee.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.R
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppColors.OnPrimaryDark
import sa.sauditourism.employee.resources.AppColors.OnSecondaryDark
import sa.sauditourism.employee.resources.AppColors.TertiaryDark
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

@SuppressLint("RememberReturnType")
@Composable
fun ButtonComponent(
    text: String,
    clickListener: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.PRIMARY,
    buttonSize: ButtonSize = ButtonSize.LARGE,
    startIconModifier: Modifier? = null,
    enabled: Boolean = true,
    startIcon: Any? = null,
    endIcon: Any? = null,
    showLoading: Boolean = false,
    removePadding: Boolean = false,
    isBgTransparent: Boolean = false,
    isLoading: Boolean = false,
    textAlign: TextAlign = TextAlign.Center,
    resizableTextSize: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    tag: String = ""
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        modifier = modifier
            .height(
                when (buttonSize) {
                    ButtonSize.LARGE -> 48.dp
                    ButtonSize.MEDIUM -> 40.dp
                    ButtonSize.SMALL -> 32.dp
                }
            )
            .testTag(tag),
        onClick = clickListener,
        shape = shape,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isBgTransparent) {
                Color.Transparent
            } else {
                getButtonColor(
                    buttonType,
                    true,
                    isPressed,
                )
            },
            disabledContainerColor = getButtonColor(buttonType, false, isPressed),
        ),
        contentPadding = when (buttonSize) {
            ButtonSize.LARGE -> {
                if (removePadding) {
                    PaddingValues(0.dp)
                } else {
                    PaddingValues(
                        top = 12.dp,
                        bottom = 12.dp,
                        start = 24.dp,
                        end = 24.dp,
                    )
                }
            }

            ButtonSize.MEDIUM -> {
                if (removePadding) {
                    PaddingValues(0.dp)
                } else {
                    PaddingValues(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                }
            }

            else -> {
                if (removePadding) PaddingValues(0.dp) else PaddingValues(8.dp)
            }
        },
        border = BorderStroke(
            1.dp,
            if (enabled) {
                getBorderColor(buttonType, isPressed)
            } else {
                Color(
                    getBorderColor(buttonType, isPressed).red,
                    getBorderColor(buttonType, isPressed).green,
                    getBorderColor(buttonType, isPressed).blue,
                    0.5f,
                )
            },
        ).takeIf {
            buttonType == ButtonType.SECONDARY_BLACK || buttonType == ButtonType.SECONDARY ||
                    buttonType == ButtonType.SECONDARY_WHITE || buttonType == ButtonType.SUCCESS_SECONDARY || buttonType == ButtonType.FAILED_SECONDARY
        },
        enabled = enabled,
    ) {
        val shouldRemoveTextPadding = removePadding && endIcon == null && startIcon == null
        startIcon?.let {
            if (!isLoading) {
                val mediaContent by remember {
                    mutableStateOf(
                        MediaContent(
                            type = GalleryType.IMAGE,
                            source = startIcon
                        )
                    )
                }
                MediaComponent(
                    mediaContent = mediaContent,
                    modifier = startIconModifier
                        ?: Modifier.size(if (buttonSize == ButtonSize.LARGE) 24.dp else if (buttonSize == ButtonSize.MEDIUM) 20.dp else 16.dp),
                    imageOptions = ImageOptions(
                        colorFilter = ColorFilter.tint(
                            if (enabled) {
                                getIconColor(buttonType, isPressed)
                            } else {
                                Color(
                                    getIconColor(buttonType, isPressed).red,
                                    getIconColor(buttonType, isPressed).green,
                                    getIconColor(buttonType, isPressed).blue,
                                    0.7f,
                                )
                            }
                        ),
                        contentScale = ContentScale.Inside
                    )
                )
            }
        }
        if (showLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 1.dp,
            )
        } else {
            var buttonFontSize by remember {
                mutableIntStateOf(
                    when (buttonSize) {
                        ButtonSize.LARGE -> {
                            16
                        }

                        ButtonSize.MEDIUM -> {
                            14
                        }

                        else -> {
                            12
                        }
                    }
                )
            }
            CustomTextComponent(
                text = text,
                textAlign = textAlign,
                style = when (buttonSize) {
                    ButtonSize.LARGE -> {
                        AppFonts.ctaLarge.copy(fontSize = buttonFontSize.sp)
                    }

                    ButtonSize.MEDIUM -> {
                        AppFonts.ctaMedium.copy(fontSize = buttonFontSize.sp)
                    }

                    else -> {
                        AppFonts.ctaSmall.copy(fontSize = buttonFontSize.sp)
                    }
                },
                color = getTextColor(buttonType, isPressed),
                modifier = Modifier
                    .padding(
                        start = if (shouldRemoveTextPadding) 0.dp else 6.dp,
                        end = if (shouldRemoveTextPadding) 0.dp else 6.dp
                    )
                    .shimmerModifier(isLoading),
                onTextLayout = if (resizableTextSize) {
                    { textLayoutResult ->
                        if (textLayoutResult.didOverflowHeight || textLayoutResult.didOverflowWidth) {
                            buttonFontSize -= 1
                        }
                    }
                } else {
                    {}
                }
            )
        }
        endIcon?.let {
            if (!isLoading) {
                val mediaContent by remember {
                    mutableStateOf(
                        MediaContent(
                            type = GalleryType.IMAGE,
                            source = endIcon
                        )
                    )
                }
                MediaComponent(
                    mediaContent = mediaContent,
                    modifier = Modifier.size(if (buttonSize == ButtonSize.LARGE) 24.dp else if (buttonSize == ButtonSize.MEDIUM) 20.dp else 16.dp),
                    imageOptions = ImageOptions(
                        colorFilter = ColorFilter.tint(
                            if (enabled) {
                                getIconColor(buttonType, isPressed)
                            } else {
                                Color(
                                    getIconColor(buttonType, isPressed).red,
                                    getIconColor(buttonType, isPressed).green,
                                    getIconColor(buttonType, isPressed).blue,
                                    0.7f,
                                )
                            }
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun getButtonColor(buttonType: ButtonType, enabled: Boolean, isPressed: Boolean): Color {
    return when (buttonType) {
        ButtonType.Dissmiss ->{
            Color(0XFFF4F4F4)
        }
        ButtonType.PRIMARY -> {
            if (isPressed) {
                AppColors.OnSecondaryDark
            } else if (enabled) {
                OnPrimaryDark
            } else {
                AppColors.PrimaryDark50
            }
        }

        ButtonType.SECONDARY -> {
            Color.Transparent
        }

        ButtonType.LINK_PRIMARY -> {
            Color.Transparent
        }

        ButtonType.WHITE_BACKGROUND, ButtonType.WHITE_BLACK_BACKGROUND -> {
            if (enabled) {
                Color.White
            } else {
                Color(Color.White.red, Color.White.green, Color.White.blue, 0.5f)
            }
        }

        ButtonType.SECONDARY_WHITE -> {
            Color.Transparent
        }

        ButtonType.LINK_WHITE -> {
            Color.Transparent
        }

        ButtonType.PRIMARY_BLACK -> {
            if (isPressed) {
                AppColors.ColorsPrimitivesOne
            } else if (enabled) {
                Color.Black
            } else {
                Color(Color.Black.red, Color.Black.green, Color.Black.blue, 0.5f)
            }
        }

        ButtonType.SECONDARY_BLACK -> {
            Color.Transparent
        }

        ButtonType.LINK_BLACK -> {
            Color.Transparent
        }

        ButtonType.SUCCESS_PRIMARY -> {
            AppColors.SuccessButtonColor
        }

        ButtonType.FAILED_PRIMARY -> {
            AppColors.FailedButtonColor
        }

        ButtonType.SUCCESS_SECONDARY, ButtonType.FAILED_SECONDARY -> {
            Color.Transparent
        }

        ButtonType.LINK_SECONDARY -> {
            Color.Transparent
        }

        ButtonType.FAILED_LINK_PRIMARY -> {
            Color.Transparent
        }
    }
}

@Composable
fun getBorderColor(buttonType: ButtonType, isPressed: Boolean): Color  {
    return when (buttonType) {

        ButtonType.PRIMARY -> {
            Color.Transparent
        }

        ButtonType.Dissmiss -> {
            Color.Transparent
        }

        ButtonType.SECONDARY -> {
            if (isPressed) AppColors.OnSecondaryDark else OnPrimaryDark
        }

        ButtonType.LINK_PRIMARY -> {
            Color.Transparent
        }

        ButtonType.WHITE_BACKGROUND, ButtonType.WHITE_BLACK_BACKGROUND -> {
            Color.Transparent
        }

        ButtonType.SECONDARY_WHITE -> {
            Color.White
        }

        ButtonType.LINK_WHITE -> {
            Color.Transparent
        }

        ButtonType.PRIMARY_BLACK -> {
            Color.Transparent
        }

        ButtonType.SECONDARY_BLACK -> {
            Color.Black
        }

        ButtonType.LINK_BLACK -> {
            Color.Transparent
        }

        ButtonType.SUCCESS_PRIMARY, ButtonType.FAILED_PRIMARY -> {
            Color.Transparent
        }

        ButtonType.SUCCESS_SECONDARY -> {
            AppColors.SuccessButtonColor
        }

        ButtonType.FAILED_SECONDARY -> {
            AppColors.FailedButtonColor
        }

        ButtonType.LINK_SECONDARY -> {
            Color.Transparent
        }

        ButtonType.FAILED_LINK_PRIMARY -> {
            Color.Transparent
        }
    }
}

@Composable
fun getTextColor(buttonType: ButtonType, isPressed: Boolean): Color {
    return when (buttonType) {

        ButtonType.Dissmiss -> {
            Color(0xFF9A9A9A)
        }
        ButtonType.PRIMARY -> {
            Color.White
        }

        ButtonType.SECONDARY -> {
            if (isPressed) OnSecondaryDark else OnPrimaryDark
        }

        ButtonType.LINK_PRIMARY -> {
            if (isPressed) OnSecondaryDark else OnPrimaryDark
        }

        ButtonType.WHITE_BACKGROUND -> {
            if (isPressed) OnSecondaryDark else OnPrimaryDark
        }

        ButtonType.WHITE_BLACK_BACKGROUND -> {
            Color.Black
        }

        ButtonType.SECONDARY_WHITE -> {
            Color.White
        }

        ButtonType.LINK_WHITE -> {
            Color.White
        }

        ButtonType.PRIMARY_BLACK -> {
            Color.White
        }

        ButtonType.SECONDARY_BLACK -> {
            Color.Black
        }

        ButtonType.LINK_BLACK -> {
            if (isPressed) AppColors.ColorsPrimitivesOne else Color.Black
        }

        ButtonType.SUCCESS_PRIMARY, ButtonType.FAILED_PRIMARY -> {
            Color.White
        }

        ButtonType.SUCCESS_SECONDARY -> {
            AppColors.SuccessButtonColor
        }

        ButtonType.FAILED_SECONDARY, ButtonType.FAILED_LINK_PRIMARY -> {
            AppColors.FailedButtonColor
        }

        ButtonType.LINK_SECONDARY -> {
            MaterialTheme.colorScheme.secondary
        }
    }
}

@Composable
fun getIconColor(buttonType: ButtonType, isPressed: Boolean): Color {
    return when (buttonType) {
        ButtonType.Dissmiss -> {
            Color(0xFF9A9A9A)
        }
        ButtonType.PRIMARY -> {
            Color.White
        }

        ButtonType.SECONDARY -> {
            if (isPressed) TertiaryDark else OnPrimaryDark
        }

        ButtonType.LINK_PRIMARY -> {
            if (isPressed) TertiaryDark else OnPrimaryDark
        }

        ButtonType.WHITE_BACKGROUND -> {
            if (isPressed) OnSecondaryDark else OnPrimaryDark
        }

        ButtonType.WHITE_BLACK_BACKGROUND -> {
            Color.Black
        }

        ButtonType.SECONDARY_WHITE -> {
            Color.White
        }

        ButtonType.LINK_WHITE -> {
            if (isPressed) TertiaryDark else OnPrimaryDark
        }

        ButtonType.PRIMARY_BLACK -> {
            Color.White
        }

        ButtonType.SECONDARY_BLACK -> {
            Color.Black
        }

        ButtonType.LINK_BLACK -> {
            if (isPressed) AppColors.ColorsPrimitivesOne else Color.Black
        }

        ButtonType.SUCCESS_PRIMARY, ButtonType.SUCCESS_SECONDARY -> {
            AppColors.SuccessButtonColor
        }

        ButtonType.FAILED_PRIMARY, ButtonType.FAILED_SECONDARY, ButtonType.FAILED_LINK_PRIMARY -> {
            AppColors.FailedButtonColor
        }

        ButtonType.LINK_SECONDARY -> {
            MaterialTheme.colorScheme.secondary
        }
    }
}

enum class ButtonType {
    PRIMARY,
    SECONDARY,
    LINK_PRIMARY,
    LINK_SECONDARY,
    WHITE_BACKGROUND,
    WHITE_BLACK_BACKGROUND,
    SECONDARY_WHITE,
    LINK_WHITE,
    PRIMARY_BLACK,
    SECONDARY_BLACK,
    LINK_BLACK,
    SUCCESS_PRIMARY,
    SUCCESS_SECONDARY,
    FAILED_PRIMARY,
    FAILED_SECONDARY,
    FAILED_LINK_PRIMARY,
    Dissmiss
}

enum class ButtonSize {
    LARGE,
    SMALL,
    MEDIUM,
}

@Preview(
    showBackground = true,
)
@Composable
fun PrimaryButtonComponentPreview() {
//    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
//                ButtonComponent(
//                    stringResource(id = R.string.app_name),
//                    clickListener = { },
//                    modifier = Modifier.fillMaxWidth(),
//                    buttonType = ButtonType.WHITE_BLACK_BACKGROUND,
//                    buttonSize = ButtonSize.LARGE,
//                )

            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
                enabled = false,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
                enabled = false,
            )

            Spacer(modifier = Modifier.height(10.dp))
            // // white

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_PRIMARY,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_PRIMARY,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_PRIMARY,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.WHITE_BACKGROUND,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.WHITE_BACKGROUND,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.WHITE_BACKGROUND,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
                enabled = false,
            )

            // white
            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY_WHITE,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY_WHITE,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY_WHITE,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_WHITE,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_WHITE,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_WHITE,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            // black
            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY_BLACK,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY_BLACK,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.PRIMARY_BLACK,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY_BLACK,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
                enabled = false,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY_BLACK,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.SECONDARY_BLACK,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
                enabled = false,
            )

            // medium
            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_BLACK,
                buttonSize = ButtonSize.LARGE,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_BLACK,
                buttonSize = ButtonSize.MEDIUM,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ButtonComponent(
                stringResource(id = R.string.app_name),
                clickListener = { },
                modifier = Modifier,
                buttonType = ButtonType.LINK_BLACK,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.arrow_left,
                endIcon = R.drawable.arrow_right,
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
//    }
}
