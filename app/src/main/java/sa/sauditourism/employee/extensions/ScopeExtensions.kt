package sa.sauditourism.employee.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

fun ViewModel.launchIO(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
    block()
}
fun ViewModel.asyncIO(block: suspend () -> Unit) = viewModelScope.async(Dispatchers.IO) {
    block()
}

fun ViewModel.launchMain(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.Main) {
    block()
}

fun ViewModel.launchDefault(block: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.Default) {
        block()
    }

fun ViewModel.launch(block: suspend () -> Unit) = viewModelScope.launch {
    block()
}