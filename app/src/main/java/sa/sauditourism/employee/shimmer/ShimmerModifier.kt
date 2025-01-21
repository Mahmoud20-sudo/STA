package sa.sauditourism.employee.shimmer

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.resources.AppColors

fun Modifier.shimmerModifier(
    isLoading: Boolean,
    shape: Shape = RoundedCornerShape(CornerSize(4.dp)),
    shimmerColor: Color = AppColors.ShimmerColor
): Modifier {
    return this.then(
        Modifier.placeholder(
            visible = isLoading,
            color = shimmerColor,
            highlight = PlaceholderHighlight.shimmer(Color.White),
            shape = shape,
        ),
    )
}
