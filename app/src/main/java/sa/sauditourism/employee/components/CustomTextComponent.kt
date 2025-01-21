package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun CustomTextComponent(
    text: String? = null,
    modifier: Modifier = Modifier,
    style: TextStyle = AppFonts.body2Regular,
    color: Color? = null,
    overflow: TextOverflow = TextOverflow.Visible,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    lineHeight: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    isLoading: Boolean = false,
    textDecoration: TextDecoration? = null,
    maxLength: Int = Int.MAX_VALUE,
    annotatedString: AnnotatedString? = null, //in case of spannable instead of text param
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {
    val displayedText = if (text.orEmpty().length > maxLength) {
        text?.substring(0, maxLength) + "..."
    } else {
        text
    }
    if (color != null) {
        if (annotatedString?.isNotEmpty() == true) {
            Text(
                text = annotatedString,
                color = color,
                style = style,
                overflow = overflow,
                maxLines = maxLines,
                minLines = minLines,
                modifier = modifier.defaultMinSize(minWidth = 50.dp).then(Modifier.shimmerModifier(isLoading)),
                textAlign = textAlign,
                lineHeight = lineHeight,
                textDecoration = textDecoration,
                onTextLayout = onTextLayout
            )
        } else {
            Text(
                text = displayedText.orEmpty(),
                color = color,
                style = style,
                overflow = overflow,
                maxLines = maxLines,
                minLines = minLines,
                modifier = modifier.defaultMinSize(minWidth = if(isLoading) 50.dp else 0.dp).then(Modifier.shimmerModifier(isLoading)),
                textAlign = textAlign,
                lineHeight = lineHeight,
                textDecoration = textDecoration,
                onTextLayout = onTextLayout
            )
        }
    } else {
        if (annotatedString?.isNotEmpty() == true) {
            Text(
                text = annotatedString,
                style = style,
                lineHeight = lineHeight,
                textAlign = textAlign,
                modifier = modifier.defaultMinSize(minWidth = if(isLoading) 50.dp else 0.dp).then(Modifier.shimmerModifier(isLoading)),
                textDecoration = textDecoration,
                onTextLayout = onTextLayout
            )
        } else {
            Text(
                text = displayedText.orEmpty(),
                style = style,
                lineHeight = lineHeight,
                textAlign = textAlign,
                overflow = overflow,
                maxLines = maxLines,
                minLines = minLines,
                modifier = modifier.defaultMinSize(minWidth = if(isLoading) 50.dp else 0.dp).then(Modifier.shimmerModifier(isLoading)),
                textDecoration = textDecoration,
                onTextLayout = onTextLayout
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextComponentPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomTextComponent(
                stringResource(id = R.string.app_name),
                style = AppFonts.heading1Regular,
            )

            CustomTextComponent(
                stringResource(id = R.string.app_name),
                style = AppFonts.heading2Medium,
            )

            CustomTextComponent(
                stringResource(id = R.string.app_name),
                style = AppFonts.captionRegular,
            )
            CustomTextComponent(
                stringResource(id = R.string.app_name),
                style = AppFonts.tagsRegular,
            )
            CustomTextComponent(
                stringResource(id = R.string.app_name),
                style = AppFonts.body1Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextComponentArabicPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.LANG_AR) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomTextComponent(
                "ريدكس برو",
                style = AppFonts.heading1Bold,
            )

            CustomTextComponent(
                "ريدكس برو",
                style = AppFonts.heading2Medium,
            )

            CustomTextComponent(
                "ريدكس برو",
                style = AppFonts.captionRegular,
            )
            CustomTextComponent(
                "ريدكس برو",
                style = AppFonts.tagsRegular,
            )
            CustomTextComponent(
                "ريدكس برو",
                style = AppFonts.body1Bold,
            )
        }
    }
}
