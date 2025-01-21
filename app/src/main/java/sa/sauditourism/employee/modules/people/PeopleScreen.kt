package sa.sauditourism.employee.modules.people

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ComingSoonModel
import sa.sauditourism.employee.components.ComingSoonViewComponent
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.resources.AppFonts

@Composable
fun PeopleScreen(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxSize().shadow(
                0.dp,
                shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)),
            ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ){
            ChangeStatusBarIconsColors()
            Spacer(modifier = Modifier.height(40.dp ))

            CustomTextComponent(
                text = LanguageConstants.PEOPLE_TAB.localizeString(),
                modifier = Modifier.padding(horizontal = 24.dp),
                style = AppFonts.headerBold,
                lineHeight = 54.sp,
            )

            ComingSoonViewComponent(
                model = ComingSoonModel(
                    title = LanguageConstants.COMING_SOON.localizeString(),
                    description =  LanguageConstants.PEOPLE_TAB_COMING_SOON_SUBTITLE.localizeString(),
                    imageResId = R.mipmap.ic_people_soon
                )
            )

        }
    }



}