package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString

@Composable
fun RequestActivitiesFooterComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = false,
    backgroundColor: Color = Color.White,
    title: String = LanguageConstants.ADD_COMMENT.localizeString(),
) {

    Column(
        modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(96.dp)
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp, top = 16.dp)
        ,
        verticalArrangement = Arrangement.Top
    ) {

        ButtonComponent(
            title,
            clickListener = onClick,
            modifier = Modifier.padding(4.dp).fillMaxWidth().padding(horizontal = 8.dp),
            buttonType = ButtonType.SECONDARY,
            buttonSize = ButtonSize.LARGE,
            enabled = enabled,
        )
    }

}
