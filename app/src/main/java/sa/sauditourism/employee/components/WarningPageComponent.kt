package sa.sauditourism.employee.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.mirror

@Composable
fun WarningPage(
    modifier: Modifier = Modifier,
    shouldIncludeBackButton: Boolean = false,
    arrowImage: Int = R.drawable.ic_arrow_left_tail,
    mainImageResId: Int = R.drawable.ic_activities,
    title: String = "",
    description: String = "",
    warningBgColor: Color = Color.White,
    buttonText: String = LanguageConstants.TRY_AGAIN.localizeString(),
    buttonClickListener: (() -> Unit)? = { },
    dialogType: DialogType = DialogType.Normal,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(bottom = 100.dp),
    ) {
        if (shouldIncludeBackButton) {
            val navController = LocalNavController.current
            Image(
                painter = painterResource(id = arrowImage),
                contentDescription = "Arrow",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .align(Alignment.TopStart)
                    .mirror()
                    .clickableOnce {
                        navController.popBackStack()
                    },
            )
        }

        CustomAlert(
            type = dialogType,
            imageResId = mainImageResId,
            modifier = Modifier
                .align(Alignment.Center),
            title = title,
            description = description,
            descriptionTopSpacing = 16.dp,
            shouldShowCloseIcon = false,
            primaryButtonText = null,
            secondaryButtonText = null,
            onPrimaryButtonClick = {
            },
            onSecondaryButtonClick = {
            },
            cardElevation = 0.dp,
            cardColor = warningBgColor,
        )

        if (buttonClickListener != null) {
            ButtonComponent(
                text = buttonText,
                clickListener = buttonClickListener,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .align(
                        Alignment.BottomCenter,
                    ),
            )
        }
    }
}
