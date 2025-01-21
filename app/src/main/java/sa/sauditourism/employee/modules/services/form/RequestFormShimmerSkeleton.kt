package sa.sauditourism.employee.modules.services.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.components.DatePickerComponent
import sa.sauditourism.employee.components.DateTimePickerComponent
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.components.TimePickerComponent
import sa.sauditourism.employee.modules.PastOrPresentSelectableDates
import sa.sauditourism.employee.shimmer.shimmerModifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFormShimmerSkeleton() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 70.dp, start = 24.dp, end = 24.dp),
    ) {
        val datePickerState =
            rememberDatePickerState(selectableDates = PastOrPresentSelectableDates)

        DatePickerComponent(
            informativeMessage = "",
            label = "",
            isOptional = false,
            state = datePickerState,
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))

        TimePickerComponent(
            informativeMessage = "",
            label = "",
            isOptional = true,
            onConfirmRequest = { time -> }, onDismissRequest = {},
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))


        DateTimePickerComponent(
            label = "",
            informativeMessage = "",
            isOptional = false,
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))


        InputFieldComponent(
            title = "",
            value = "",
            inputFieldModel = InputFieldModel(
                placeholderText = "",
                type = InputFieldType.Normal,
            ),
            isOptional = true,
            onValueChange = {},
            informativeMessage = "",
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))

        InputFieldComponent(
            title = "",
            value = "",
            inputFieldModel = InputFieldModel(
                placeholderText = "",
                type = InputFieldType.Numbers,
            ),
            isOptional = false,
            onValueChange = {},
            informativeMessage = "",
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))

        InputFieldComponent(
            title = "",
            value = "",
            inputFieldModel = InputFieldModel(
                placeholderText = "",
                type = InputFieldType.Numbers,
            ),
            isOptional = false,
            onValueChange = {},
            informativeMessage = "",
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))

        InputFieldComponent(
            title = "",
            value = "",
            inputFieldModel = InputFieldModel(
                placeholderText = "",
                type = InputFieldType.Numbers,
            ),
            isOptional = false,
            onValueChange = {},
            informativeMessage = "",
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))

        TimePickerComponent(
            informativeMessage = "",
            label = "",
            isOptional = true,
            onConfirmRequest = { time -> }, onDismissRequest = {},
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))


        DateTimePickerComponent(
            label = "",
            informativeMessage = "",
            isOptional = false,
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))


        InputFieldComponent(
            title = "",
            value = "",
            inputFieldModel = InputFieldModel(
                placeholderText = "",
                type = InputFieldType.Normal,
            ),
            isOptional = true,
            onValueChange = {},
            informativeMessage = "",
            modifier = Modifier.shimmerModifier(true)
        )

        TimePickerComponent(
            informativeMessage = "",
            label = "",
            isOptional = true,
            onConfirmRequest = { time -> }, onDismissRequest = {},
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))


        DateTimePickerComponent(
            label = "",
            informativeMessage = "",
            isOptional = false,
            modifier = Modifier.shimmerModifier(true)
        )

        Spacer(modifier = Modifier.height(25.dp))


        InputFieldComponent(
            title = "",
            value = "",
            inputFieldModel = InputFieldModel(
                placeholderText = "",
                type = InputFieldType.Normal,
            ),
            isOptional = true,
            onValueChange = {},
            informativeMessage = "",
            modifier = Modifier.shimmerModifier(true)
        )

    }
}