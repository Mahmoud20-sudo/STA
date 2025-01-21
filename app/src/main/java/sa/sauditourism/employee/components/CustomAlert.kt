package sa.sauditourism.employee.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.PRIMARY_BUTTON
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.resources.AppColors


@Composable
fun CustomAlertDialog(
    type: DialogType,
    modifier: Modifier = Modifier,
    imageResId: Int? = null,
    title: String? = null,
    description: String? = null,
    shouldShowCloseIcon: Boolean = true,
    primaryButtonText: String? = null,
    secondaryButtonText: String? = null,
    thirdButtonText: String? = null,
    usePlatformDefaultWidth: Boolean = false, // Set to false
    onPrimaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onThirdButtonClick: () -> Unit = {},
    tintColor: Color? = null,
    iconSize:Dp? = 80.dp,
    shouldDismissDialogOnPrimary: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = usePlatformDefaultWidth)
    ) {
        CustomAlert(
            type = type,
            modifier = modifier,
            imageResId = imageResId,
            title = title,
            iconSize = iconSize,
            description = description,
            usePlatformDefaultWidth = usePlatformDefaultWidth,
            shouldShowCloseIcon = shouldShowCloseIcon,
            primaryButtonText = primaryButtonText,
            secondaryButtonText = secondaryButtonText,
            thirdButtonText = thirdButtonText,
            onPrimaryButtonClick = onPrimaryButtonClick,
            onSecondaryButtonClick = onSecondaryButtonClick,
            onThirdButtonClick = onThirdButtonClick,
            tintColor = tintColor,
            shouldDismissDialogOnPrimary = shouldDismissDialogOnPrimary,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun CustomAlert(
    type: DialogType,
    modifier: Modifier = Modifier,
    imageResId: Int? = null,
    title: String? = null,
    description: String? = null,
    descriptionTopSpacing: Dp = 12.dp,
    usePlatformDefaultWidth: Boolean = false,
    shouldShowCloseIcon: Boolean = true,
    primaryButtonText: String? = null,
    secondaryButtonText: String? = null,
    thirdButtonText: String? = null,
    onPrimaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onThirdButtonClick: () -> Unit = {},
    cardElevation: Dp = 10.dp,
    cardColor: Color? = null,
    tintColor: Color? = null,
    iconSize:Dp? = 80.dp,
    shouldDismissDialogOnPrimary: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    val colors = CardDefaults.cardColors(Color.White)

    val cardModifier = modifier.fillMaxWidth()

    Card(
        modifier = cardModifier.padding(horizontal = 24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation,
        ),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        if (shouldShowCloseIcon) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(top = 24.dp, end = 24.dp)
                        .size(32.dp),
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_popup_close),
                        tint = Color.Black,
                        contentDescription = null
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageResId?.let { image ->
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize?:80.dp),
                    colorFilter = if (tintColor == null) null else ColorFilter.tint(tintColor)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (title != null) {
                CustomTextComponent(
                    text = title, style = AppFonts.heading4Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                )
            }

            if (description != null) {
                Spacer(modifier = Modifier.height(descriptionTopSpacing))
                CustomTextComponent(
                    text = description, style = AppFonts.body2Regular,
                    color = AppColors.ColorsPrimitivesOne,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp,
                )
            }

            if (primaryButtonText != null) {
                Spacer(modifier = Modifier.height(32.dp))
                ButtonComponent(
                    text = primaryButtonText,
                    clickListener = {
                        onPrimaryButtonClick.invoke()
                    },
                    modifier = Modifier.fillMaxWidth().testTag(PRIMARY_BUTTON),
                    buttonType = when (type) {
                        DialogType.Normal -> ButtonType.PRIMARY
                        DialogType.Success -> ButtonType.SUCCESS_PRIMARY
                        DialogType.Failed -> ButtonType.FAILED_PRIMARY
                    },
                    buttonSize = ButtonSize.LARGE,
                    enabled = true,
                )
            }

            if (secondaryButtonText != null) {
                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    text = secondaryButtonText,
                    clickListener = {
                        onSecondaryButtonClick.invoke()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = when (type) {
                        DialogType.Normal -> ButtonType.SECONDARY
                        DialogType.Success -> ButtonType.SUCCESS_SECONDARY
                        DialogType.Failed -> ButtonType.FAILED_SECONDARY
                    },
                    buttonSize = ButtonSize.LARGE,
                    enabled = true,
                )
            }

            if (thirdButtonText != null) {
                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    text = thirdButtonText,
                    clickListener = {
                        onThirdButtonClick.invoke()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = when (type) {
                        DialogType.Normal -> ButtonType.SECONDARY
                        DialogType.Success -> ButtonType.SUCCESS_SECONDARY
                        DialogType.Failed -> ButtonType.FAILED_SECONDARY
                    },
                    buttonSize = ButtonSize.LARGE,
                    enabled = true,
                )
            }
        }
    }
}

@Preview(
    showBackground = false,
)
@Composable
fun CustomAlertPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        CustomAlert(
            type = DialogType.Normal,
            imageResId = R.drawable.ic_activities,
            title = "Title Will goes\n" +
                    " here.",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            primaryButtonText = "TEST",
            secondaryButtonText = "TEST",
            onPrimaryButtonClick = {
                // Handle primary button click
            },
            onSecondaryButtonClick = {

            },
            onDismiss = {
                // Handle alert dismiss
            }
        )
    }
}

