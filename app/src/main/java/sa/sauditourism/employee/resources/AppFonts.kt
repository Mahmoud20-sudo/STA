package sa.sauditourism.employee.resources

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.R

object AppFonts {


    val destinationHeader: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 50.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading0Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 40.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading0Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 40.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading0Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 40.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading1Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 36.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading1Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 36.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading1Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 36.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading2Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 32.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading2Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 32.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading2Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 32.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading3Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 30.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading3Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 30.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading3Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 30.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading3SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 30.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading4Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading4Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading4SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading4Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading5Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 22.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading5Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 22.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading5SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 22.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading5Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 22.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading6Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 20.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading6Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 20.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading6SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 20.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading6Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 20.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val heading7Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 18.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading7Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 18.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading7SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 18.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val heading7Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 18.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val subtitle1SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val subtitle1Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val subtitle2SemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val subtitle2Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val body1Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val body1Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val body1Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val body2Regular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val body2Medium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val body2Bold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val body2MediumUnderlined: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val ctaSmall: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val ctaMedium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val ctaLarge: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val labelMedium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val labelSemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val labelBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 16.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val captionRegular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val captionMedium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val captionSemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val headerRegular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val headerMedium: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_medium))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val headerBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 24.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_serif_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    //no style for capital letter, we have to do it manually in component
    val capsSemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    //no style for capital letter, we have to do it manually in component
    val capsExtraBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                fontWeight = FontWeight.ExtraBold,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    //no style for capital letter, we have to do it manually in component
    val capsSmallSemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }


    val capsSmallRegular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }


    val tagsRegular: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
    val tagsBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val copy375: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 14.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val tagsSemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 12.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }

    val menuSemiBold: TextStyle
        @Composable
        get() {
            return TextStyle(
                fontSize = 10.sp,
                fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FontFamily(Font(R.font.saudi_sans_bold))
                } else {
                    null
                },
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )
        }
}
