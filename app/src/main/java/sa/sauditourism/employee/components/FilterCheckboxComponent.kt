package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.R
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.resources.AppColors


enum class FilterCheckboxState {
    Checked,
    Unchecked,
    PartialChecked
}


@Composable
fun CustomCheckbox(
    state: FilterCheckboxState,
    onStateChanged: (FilterCheckboxState) -> Unit,
    enabled: Boolean = true
) {
    var currentState by remember(state) { mutableStateOf(state) }

    Box(
        modifier = Modifier
            .size(20.dp)
            .border(
                1.dp,
                if (currentState == FilterCheckboxState.Unchecked) AppColors.ColorsPrimitivesThree else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .clickableWithoutRipple {
                if (enabled) {
                    currentState =
                        if (currentState == FilterCheckboxState.Unchecked || currentState == FilterCheckboxState.PartialChecked) {
                            FilterCheckboxState.Checked
                        } else {
                            FilterCheckboxState.Unchecked
                        }

                    onStateChanged(currentState)
                }
            }
            .background(
                color = if (enabled) if (state != FilterCheckboxState.Unchecked) AppColors.OnPrimaryDark else Color.Transparent else AppColors.ColorsPrimitivesThree,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            FilterCheckboxState.Checked -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rect_tick),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp).padding(horizontal = 4.dp),
                    tint = Color.White
                )
            }

            FilterCheckboxState.Unchecked -> {

            }

            FilterCheckboxState.PartialChecked -> {
                HorizontalDivider(
                    modifier = Modifier
                        .width(15.dp)
                        .height(3.dp)
                        .background(shape = RoundedCornerShape(2.dp), color = Color.Transparent),
                    thickness = 3.dp,
                    color = Color.White
                )
            }
        }
    }
}
