package sa.sauditourism.employee.modules

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import sa.sauditourism.employee.components.DatePickerComponent
import java.time.Duration
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DateComponentTest() {
    val datePickerState = rememberDatePickerState(selectableDates = PastOrPresentSelectableDates,)
    DatePickerComponent(state = datePickerState)


}

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis() - Duration.ofDays(1).toMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object FutureSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        // Only allow today or future dates
        return utcTimeMillis >= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        // Allow all future years
        return year >= LocalDate.now().year
    }
}


