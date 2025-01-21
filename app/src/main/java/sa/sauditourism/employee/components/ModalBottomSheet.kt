package sa.sauditourism.employee.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.resources.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    content: @Composable (onDismiss: () -> Unit) -> Unit,
    topPadding: Dp = 350.dp,
    onDismissed: () -> Unit = {}
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val onDismiss: () -> Unit = {
        scope.launch {
            modalBottomSheetState.hide()
            onDismissed.invoke()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = AppColors.WHITE,
        modifier = Modifier.padding(top = topPadding)
    ) {
        content(onDismiss)
    }
}
