package sa.sauditourism.employee.modules.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.BACK_BUTTON_TAG
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.mirror
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun MaintenancePage(
    navController: NavHostController = LocalNavController.current,
    modifier: Modifier = Modifier,
    shouldIncludeBackButton: Boolean = true,
    arrowImage: Int = R.drawable.ic_arrow_left_tail,
    mainImageResId: Int = R.drawable.ic_activities,
    title: String = LanguageConstants.MOBILE_UNDER_MAINTENANCE.localizeString(),
    description: String? = LanguageConstants.MOBILE_UNDER_MAINTENANCE_DESCRIPTION.localizeString(),
) {
    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                type = GalleryType.IMAGE,
                source = mainImageResId
            )
        )
    }

    ChangeStatusBarIconsColors(Color.White)

    Box(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        if (shouldIncludeBackButton) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                IconButton(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .size(24.dp)
                        .align(Alignment.TopStart)
                        .mirror()
                        .testTag(BACK_BUTTON_TAG),
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = arrowImage),
                        contentDescription = "Back",
                    )
                }
            }
        }


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MediaComponent(
                mediaContent = mediaContent,
                modifier = Modifier
                    .size(80.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            CustomTextComponent(
                text = title,
                style = AppFonts.heading5SemiBold,
                color = AppColors.Black,
                textAlign = TextAlign.Center
            )

            description?.let {
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextComponent(
                    text = it,
                    style = AppFonts.body2Regular,
                    color = AppColors.ColorTextSubdued,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}