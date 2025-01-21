package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.resources.AppColors

@Composable
fun FooterComponent(
    modifier: Modifier,
    content: @Composable() () -> Unit
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .zIndex(100f)
        .shadow(
            20.dp,
            shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)),
            ambientColor = AppColors.BLACK,
            clip = true
        )
        .background(Color.White)
    ) {
        content()
    }
}
