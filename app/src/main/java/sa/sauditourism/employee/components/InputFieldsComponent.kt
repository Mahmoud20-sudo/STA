package sa.sauditourism.employee.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import ir.kaaveh.sdpcompose.sdp
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.LanguageConstants.NONE
import sa.sauditourism.employee.constants.TestTagsConstants.BASIC_TEXT_FIELD
import sa.sauditourism.employee.extensions.ScrollbarConfig
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.toFlagEmoji
import sa.sauditourism.employee.extensions.useDebounce
import sa.sauditourism.employee.extensions.verticalScrollWithScrollbar
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.utils.EmptyTextToolbar
import timber.log.Timber

enum class InputFieldType {
    Normal,
    Search,
    Dropdown,
    Password,
    Email,
    PhoneNumber,
    Numbers,
    DatePicker,
    Time
}

enum class DropDownType {
    NONE,
    SingleSelection,
    MultiSelection,
    LanguageSelection,
    StringSelection
}

data class InputFieldModel(
    val placeholderText: String? = null,
    val type: InputFieldType = InputFieldType.Normal,
    val textColor: Color = Color.Black,
    val maxCharLimit: Int? = null,
    val minLines: Int = 1,
    val maxLines: Int = 1,
    val singleLine: Boolean = true,
    val dropdownWithSearchIcon: Boolean = true,
    val showCharCounter: Boolean = false,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val iconTintColor: Color? = null
)

/**
 * This class is being used for the dropdown list and the phone number.
 *
 * @param label is the title that will be shown in the dropdown item.
 * @param extra is considered as the flag or country code for countries and will be
 * used as the phone number if the type is PhoneNumber.
 */
data class DropDownModel(
    val label: String = "",
    val name: String? = null,
    val extra: String? = null,
    var selected: Boolean = false,
    val id: Int = -1,
    val stringId: String? = null,
    val innerList: List<DropDownModel> = emptyList(),
    val imageUrl: String? = null

)

@Composable
fun InputFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    inputFieldModel: InputFieldModel,
    modifier: Modifier = Modifier,
    shouldUpdateValue: ((String) -> Boolean) = { true },
    enabled: Boolean = true,
    clickable: Boolean = false,
    readOnly: Boolean = false,
    title: String? = null,
    isOptional: Boolean = false,
    isDropDownUserSearch: Boolean = false,
    isMultiDropDown: Boolean = false,
    isExpanded: MutableState<Boolean?> = mutableStateOf(null),
    textStyle: TextStyle = AppFonts.body2Regular,
    textAlign: TextAlign = TextAlign.Start,
    dropDownType: DropDownType = DropDownType.NONE,
    showNoneOption: Boolean = true,
    originalModels: List<Any> = emptyList(),
    onDropDownItemSelected: ((Any, Any?) -> Unit)? = null,
    selectedDropDownItem: Any? = null,
    initialSelectedItems: List<Any>? = null,
    countryPhoneNumber: DropDownModel? = null,
    leadingView: @Composable (() -> Unit)? = null,
    trailingView: @Composable (() -> Unit)? = null,
    informativeMessage: String? = null,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    localFocusManager: FocusManager = LocalFocusManager.current,
    errorMessage: String? = null,
    showErrorMessage: Boolean? = false,
    onStartSearch: (() -> Unit)? = null,
    onFocusChange: ((Boolean) -> Unit)? = null,
    displayUnAvaiableSection: Boolean = false,
    dropdownWithImage: Boolean = false,
    // for the min value if the lines is 1
    minTextFieldHeight: Dp = 44.dp,
    maxTextFieldHeight: Dp = 125.dp,
    paddingStart: Dp? = null,
    filledBorderColor: Color? = AppColors.OnPrimaryDark,
    onSearchContainterClick: (() -> Unit)? = null,
    onDoneClick: (() -> Unit)? = null,


    ) {
    val layoutDirection = LocalLayoutDirection.current

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = when {
                    value.isEmpty() -> TextRange.Zero
                    else -> TextRange(value.length, value.length)
                },
            ),
        )
    }

    // 2. debouncing
    textFieldValue.useDebounce {
        onValueChange(textFieldValue.text)
    }

    // Update internal state when the external value changes
    LaunchedEffect(value) {
        if (value.isEmpty()) {
            textFieldValue = textFieldValue.copy(text = "")
            onValueChange("")
            onStartSearch?.invoke()
        }
    }


//    val showPassword = remember { mutableStateOf(inputFieldModel.type != InputFieldType.Password) }
    val hasFocus = remember { mutableStateOf(false) }
    val keyboardType: KeyboardType = when (inputFieldModel.type) {
//        InputFieldType.Password -> KeyboardType.Password
        InputFieldType.Email -> KeyboardType.Email
        InputFieldType.PhoneNumber -> KeyboardType.Phone
        InputFieldType.Numbers -> KeyboardType.Number
        else -> inputFieldModel.keyboardType
    }

    var textFieldSizeModifier = Modifier.fillMaxWidth()
    if (inputFieldModel.minLines == inputFieldModel.maxLines && inputFieldModel.maxLines == 1) {
        textFieldSizeModifier = textFieldSizeModifier.height(minTextFieldHeight)
    } else {
        textFieldSizeModifier = textFieldSizeModifier.height(maxTextFieldHeight)
    }
    val phoneNumberUtil = PhoneNumberUtil.getInstance()

//    val inputHasError = when (inputFieldModel.type) {
//        InputFieldType.Normal -> false
//        InputFieldType.Search -> false
//        InputFieldType.Dropdown -> false
//        InputFieldType.Password -> textFieldValue.text.isNotEmpty() && !textFieldValue.text.isValidPassword()
//        InputFieldType.Email -> textFieldValue.text.isNotEmpty() && !textFieldValue.text.isValidEmail()
//        InputFieldType.PhoneNumber -> {
//            try {
//                if (textFieldValue.text.isEmpty()) {
//                    false
//                } else {
//                    val phoneNumber =
//                        phoneNumberUtil.parse("+${textFieldValue.text.replace(" ", "")}", null)
//                    !phoneNumberUtil.isValidNumber(phoneNumber)
//                }
//            } catch (ex: java.lang.Exception) {
//                true
//            }
//        }
//    }

//    val informativeMessages =
//        when (inputFieldModel.type) {
//            InputFieldType.Password ->
//                mapOf(
//                    Pair(
//                        ".{8,}",
//                        LanguageConstants.PASSWORD_VALIDATION_8_CHARACTER.localizeString()
//                    ),
//                    Pair(
//                        ".*[A-Z].*",
//                        LanguageConstants.PASSWORD_VALIDATION_UPPERCASE.localizeString()
//                    ),
//                    Pair(".*\\d+.*", LanguageConstants.PASSWORD_VALIDATION_NUMBER.localizeString()),
//                    Pair(
//                        ".*[@#_$%^&+=].*",
//                        LanguageConstants.PASSWORD_VALIDATION_SPECIAL_CHARACTER.localizeString()
//                    )
//                )
//
//            InputFieldType.Email -> mapOf(
//                Pair(
//                    "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",
//                    LanguageConstants.EMAIL_VALIDATION_VALID_EMAIL.localizeString()
//                )
//            )
//
//            else -> {
//                informativeMessage?.let {
//                    mapOf(Pair("*", informativeMessage))
//                }
//            }
//        }

    val filteredModels = remember { mutableStateListOf<Any>() }
    var selectedItem by remember { mutableStateOf(selectedDropDownItem) }
    val expanded = remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<DropDownModel>() }.apply {
        initialSelectedItems?.let {
            clear()
            it.onEach { dropDownItem ->
                add(dropDownItem as DropDownModel)
            }
        }
        if (readOnly && dropDownType == DropDownType.MultiSelection) {
            clear()
            originalModels.onEach { add(it as DropDownModel) }
        }
    }

    LaunchedEffect(originalModels.size) {
        //new list has been emitted, then coupling filteredModels state with viewmodel state
        when (isDropDownUserSearch) {
            true -> {
                // update list whenever it's changed
                filteredModels.clear()
                if (originalModels.isNotEmpty()) {
                    filteredModels.addAll(originalModels)

                    if (dropDownType == DropDownType.SingleSelection)
                        filteredModels.add(
                            filteredModels.size - 1,
                            DropDownModel(id = -1, stringId = null, label = NONE.localizeString())
                        )
                }
            }

            else -> {
                // update list first time only
                if (filteredModels.isEmpty() && originalModels.isNotEmpty()) {
                    filteredModels.addAll(originalModels)
                    if (dropDownType == DropDownType.SingleSelection)
                        filteredModels.add(
                            filteredModels.size - 1,
                            DropDownModel(id = -1, stringId = null, label = NONE.localizeString())
                        )
                }
            }
        }
    }

    LaunchedEffect(isExpanded.value) {
        isExpanded.value?.let {
            expanded.value = it
        }
    }

    val onEndSearch: () -> Unit = {
        if (textFieldValue.text.isEmpty()) {
            onValueChange.invoke("")
        }
        keyboardController?.hide()
        localFocusManager.clearFocus()
        expanded.value = false
        runCatching {
            onDropDownItemSelected?.invoke(
                DropDownModel(id = -1, label = NONE.localizeString()),
                (selectedItem as DropDownModel).stringId
            )
        }
        selectedItem = DropDownModel(id = -1, label = NONE.localizeString())
    }

    LaunchedEffect(hasFocus.value) {
        if (readOnly) return@LaunchedEffect
        if (inputFieldModel.type == InputFieldType.Search)
            onFocusChange?.invoke(hasFocus.value || textFieldValue.text.isNotEmpty())
        if (inputFieldModel.type == InputFieldType.Dropdown && !hasFocus.value)
            expanded.value = false
    }

    LaunchedEffect(selectedDropDownItem) {
        runCatching {
            val isLanguageOrStringDropDown =
                dropDownType != DropDownType.LanguageSelection && dropDownType != DropDownType.StringSelection
            if (selectedDropDownItem == DropDownModel(id = -1, label = NONE.localizeString()) && isLanguageOrStringDropDown) {
                selectedItem = selectedDropDownItem
                textFieldValue = textFieldValue.copy(text = "")
                expanded.value = false
            }
        }
    }


    if (inputFieldModel.type == InputFieldType.PhoneNumber) {
        if (textFieldValue.text.isEmpty() && countryPhoneNumber != null && selectedItem == null) {
            selectedItem = countryPhoneNumber
            val phoneNumberText = "+" +
                    phoneNumberUtil.getExampleNumber(countryPhoneNumber.extra?.uppercase()).countryCode.toString()

            val textLength = textFieldValue.text.length
            val selectionStart = textFieldValue.selection.start.coerceIn(0, textLength)
            val selectionEnd = textFieldValue.selection.end.coerceIn(0, textLength)
            textFieldValue = textFieldValue.copy(
                text = phoneNumberText,
                selection = TextRange(selectionStart, selectionEnd),
            )
        }
    }
    val txtDirection =
        if (sharedPreferencesManager.preferredLocale.orEmpty() == "ar") TextDirection.Rtl else TextDirection.Ltr
    val direction =
        if (inputFieldModel.type == InputFieldType.PhoneNumber) {
            LayoutDirection.Ltr
        } else {
            layoutDirection
        }
    val textDirection: TextDirection = if (inputFieldModel.type == InputFieldType.PhoneNumber) {
        TextDirection.Ltr
    } else {
        txtDirection
    }
    val focusRequester = FocusRequester()
    if (onStartSearch != null) {
        SideEffect {
            focusRequester.requestFocus()
        }
    }
    var rowModifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()


    if (clickable && inputFieldModel.type == InputFieldType.Dropdown) {
        rowModifier = rowModifier.clickableWithoutRipple {

            if (filteredModels.size == 0) {
                filteredModels.addAll(originalModels)
            }
            expanded.value = !expanded.value
        }
    }

    val shape =
        if (dropDownType == DropDownType.LanguageSelection || dropDownType == DropDownType.StringSelection) RoundedCornerShape(
            16.dp
        ) else RoundedCornerShape(
            4.dp
        )
    val topPadding =
        if (dropDownType == DropDownType.LanguageSelection || dropDownType == DropDownType.StringSelection) 8.dp else 2.dp
    val paddingModifier =
        if (dropDownType == DropDownType.LanguageSelection || dropDownType == DropDownType.StringSelection) Modifier.padding(
            top = 30.dp,
            bottom = 20.dp
        ) else Modifier

    val compositionsVararg = if (inputFieldModel.type == InputFieldType.Numbers)
        arrayOf(
            LocalLayoutDirection provides direction,
            LocalTextToolbar provides EmptyTextToolbar
        )
    else arrayOf(LocalLayoutDirection provides direction)

    CompositionLocalProvider(
        values = compositionsVararg
    ) {
        Column(modifier = modifier) {
            Row(Modifier.fillMaxWidth()) {
                title?.let {
                    CustomTextComponent(
                        text = it,
                        color = Color.Black,
                        style = AppFonts.labelSemiBold,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                if (isOptional)
                    CustomTextComponent(
                        text = LanguageConstants.OPTIONAL.localizeString().lowercase(),
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.labelSemiBold
                    )
            }

            Spacer(modifier = Modifier.height(if (title.isNullOrEmpty()) 8.dp else 2.dp))

            if (readOnly && dropDownType == DropDownType.MultiSelection)
                Timber.d("No Input Field here")
            else
                Box(
                    modifier = textFieldSizeModifier // texts will get cut if its less than 50, because we can't change the internal padding of the OutlinedTextView
                        .onFocusChanged {
                            hasFocus.value = it.hasFocus
                            if (hasFocus.value && inputFieldModel.type == InputFieldType.Dropdown) {
                                if (displayUnAvaiableSection) onSearchContainterClick?.invoke()
                                showDropDownList(
                                    textFieldValue,
                                    filteredModels,
                                    originalModels,
                                    expanded,
                                    isDropDownUserSearch,
                                    isMultiDropDown,
                                    showNoneOption
                                )
                            }
                        }
                        .border(
                            width = 1.dp,
                            color =
                            if (readOnly) AppColors.ColorsPrimitivesFour
                            else if (dropDownType == DropDownType.LanguageSelection || dropDownType == DropDownType.StringSelection) {
                                AppColors.ColorsPrimitivesFour
                            } else if (!errorMessage.isNullOrEmpty()) {
                                AppColors.ColorFeedbackError
                            } else if (hasFocus.value) {
                                if (textFieldValue.text.isEmpty()) {
                                    AppColors.OnSecondaryDark
                                } else AppColors.OnPrimaryDark
                            } else if (textFieldValue.text.isNotEmpty() && (inputFieldModel.type == InputFieldType.Search || dropDownType != DropDownType.LanguageSelection)) {
                                filledBorderColor!!
                            } else {
                                AppColors.ColorsPrimitivesFour
                            },
                            shape = RoundedCornerShape(size = 8.dp),
                        )
                        .background(
                            color = if (enabled) Color.White else AppColors.ColorsPrimitivesFive,
                            shape = RoundedCornerShape(size = 8.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier = rowModifier.padding(bottom = if (inputFieldModel.maxCharLimit == null || !inputFieldModel.showCharCounter) 0.dp else 12.dp)
                    ) {
                        var hasLeadingViewIcon = !readOnly
                        val mPaddingStart = paddingStart ?: if (hasLeadingViewIcon) 8.dp else 16.dp

                        if (!readOnly)
                            leadingView ?: when (inputFieldModel.type) {
                                InputFieldType.Dropdown -> if (inputFieldModel.dropdownWithSearchIcon) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_search),
                                        tint = if (textFieldValue.text.isEmpty()) {
                                            AppColors.ColorsPrimitivesThree
                                        } else {
                                            Color.Black
                                        },
                                        contentDescription = "Search",
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .size(20.dp)
                                            .align(Alignment.CenterVertically),
                                    )
                                } else {
                                    hasLeadingViewIcon = false
                                }

                                InputFieldType.Search -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_search),
                                        tint = if (textFieldValue.text.isEmpty()) {
                                            AppColors.ColorsPrimitivesThree
                                        } else {
                                            Color.Black
                                        },
                                        contentDescription = "Search",
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .size(20.dp)
                                            .align(Alignment.CenterVertically),
                                    )
                                }

                                InputFieldType.PhoneNumber -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .wrapContentWidth()
                                            .background(
                                                color = AppColors.ColorsPrimitivesFive,
                                                shape = RoundedCornerShape(
                                                    topStart = 8.dp,
                                                    bottomStart = 8.dp,
                                                ),
                                            )
                                            .clickableWithoutRipple {
                                                if (filteredModels.isEmpty() || textFieldValue.text.isEmpty()) {
                                                    filteredModels.clear()
                                                    filteredModels.addAll(originalModels)
                                                }
                                                expanded.value = !expanded.value
                                            },
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .wrapContentWidth()
                                                .padding(horizontal = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
//                                    CustomTextComponent(
//                                        text = selectedItem?.extra?.toFlagEmoji(),
//                                        style = AppFonts.heading7SemiBold,
//                                        modifier = Modifier
//                                            .padding(end = 2.dp)
//                                            .size(28.dp, 20.dp),
//                                    )
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.ic_arrow_down),
//                                        "expand",
//                                        tint = Color.Black,
//                                        modifier = Modifier
//                                            .size(16.dp),
//                                    )
                                        }
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .width(1.dp)
                                                .fillMaxHeight(),
                                            color = AppColors.ColorsPrimitivesFour
                                        )
                                    }
                                }

                                else -> {
                                    hasLeadingViewIcon = false
                                }
                            }

                        Box(
                            modifier = Modifier
                                .padding(
                                    top = if (minTextFieldHeight == 48.dp) 12.dp else 10.dp,
                                    bottom = 8.dp,
                                    end = 8.dp,
                                    start = mPaddingStart,
                                )
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (inputFieldModel.placeholderText != null && textFieldValue.text.isEmpty()) {
                                when (inputFieldModel.type) {
                                    InputFieldType.Dropdown -> CustomTextComponent(
                                        text = inputFieldModel.placeholderText,
                                        color = AppColors.SwitchButtonTrackColorDisabled,
                                        style = AppFonts.body2Regular.copy(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = true
                                            )
                                        ),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 2.dp),
                                        textAlign = textAlign,
                                    )

                                    else -> CustomTextComponent(
                                        text = inputFieldModel.placeholderText,
                                        style = textStyle.copy(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = true
                                            )
                                        ),
                                        color = AppColors.ColorsPrimitivesThree,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 2.dp),
                                        textAlign = textAlign,

                                        )
                                }
                            }

                            BasicTextField(
                                readOnly = readOnly,
                                value = textFieldValue,
                                onValueChange = { newValue ->
                                    if (textFieldValue.text == newValue.text) {
                                        return@BasicTextField
                                    }
                                    if (!shouldUpdateValue(newValue.text)) {
                                        return@BasicTextField
                                    }
                                    if (inputFieldModel.maxCharLimit == null || newValue.text.length <= inputFieldModel.maxCharLimit) {
                                        textFieldValue = newValue
                                    }

                                    if (inputFieldModel.type == InputFieldType.PhoneNumber) {
                                        try {
                                            val numberWithNoSpaces = newValue.text.replace(" ", "")
                                            val textNumber =
                                                if (numberWithNoSpaces.first() == '+') {
                                                    numberWithNoSpaces
                                                } else {
                                                    "+$numberWithNoSpaces"
                                                }
                                            val phoneNumber: Phonenumber.PhoneNumber =
                                                phoneNumberUtil.parse(
                                                    textNumber,
                                                    null,
                                                )
                                            val countryCodeNumber = phoneNumber.countryCode

                                            val countryCode =
                                                phoneNumberUtil.getRegionCodeForCountryCode(
                                                    countryCodeNumber,
                                                )
                                            if (countryCode.lowercase() != "zz") {
                                                filteredModels.clear()
//                                            filteredModels.addAll(
//                                                originalModels.filter { item ->
//                                                    item.extra?.lowercase() == countryCode.lowercase()
//                                                },
//                                            )
                                                if (filteredModels.size == 1) {
                                                    selectedItem = filteredModels[0]
                                                }
                                                if (filteredModels.isEmpty()) {
                                                    filteredModels.addAll(originalModels)
                                                }

                                                phoneNumber.countryCode = countryCodeNumber
                                                phoneNumber.nationalNumber =
                                                    numberWithNoSpaces
                                                        .replace(countryCodeNumber.toString(), "")
                                                        .toLongOrNull() ?: 0
                                                if (phoneNumberUtil.isValidNumber(phoneNumber)) {
                                                    val newPhoneNumVal =
                                                        phoneNumberUtil.formatNumberForMobileDialing(
                                                            phoneNumber,
                                                            "",
                                                            true,
                                                        )

                                                    val textLength = textFieldValue.text.length
                                                    val selectionStart =
                                                        textFieldValue.selection.start.coerceIn(
                                                            0,
                                                            textLength
                                                        )
                                                    val selectionEnd =
                                                        textFieldValue.selection.end.coerceIn(
                                                            0,
                                                            textLength
                                                        )
                                                    textFieldValue = textFieldValue.copy(
                                                        text = newPhoneNumVal,
                                                        selection = TextRange(
                                                            selectionStart,
                                                            selectionEnd
                                                        ),
                                                    )
                                                    onValueChange(textFieldValue.text)
                                                }
                                            }
                                        } catch (ignored: Exception) {
                                        }
                                    } else if (inputFieldModel.type == InputFieldType.Dropdown) {
                                        if (displayUnAvaiableSection) onSearchContainterClick?.invoke()
                                        showDropDownList(
                                            textFieldValue,
                                            filteredModels,
                                            originalModels,
                                            expanded,
                                            isDropDownUserSearch,
                                            isMultiDropDown,
                                            showNoneOption
                                        )
                                    }
                                },
                                enabled = enabled && !clickable,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 5.5.dp)
                                    .focusRequester(focusRequester)
                                    .alpha(if (enabled && !clickable) 1.0f else 1f)
                                    .testTag(BASIC_TEXT_FIELD),
                                textStyle = textStyle.copy(
                                    color = if (enabled) inputFieldModel.textColor else AppColors.ColorsPrimitivesThree,
                                    textAlign = textAlign,
                                    textDirection = textDirection ?: TextDirection.Ltr,
                                ),
                                keyboardOptions = if (onStartSearch == null) {
                                    KeyboardOptions.Default.copy(
                                        keyboardType = keyboardType,
                                        imeAction = ImeAction.Done
                                    )

                                } else {
                                    KeyboardOptions(
                                        keyboardType = keyboardType,
                                        imeAction =
                                        if (inputFieldModel.type == InputFieldType.Search) {
                                            ImeAction.Search
                                        } else {
                                            ImeAction.Done
                                        },
                                    )
                                },
                                keyboardActions =
                                KeyboardActions {
                                    onEndSearch.invoke()
                                },
                                cursorBrush = if (textFieldValue.text.isEmpty()) {
                                    SolidColor(AppColors.OnSecondaryDark)
                                } else {
                                    SolidColor(AppColors.OnPrimaryDark)
                                },
                                minLines = inputFieldModel.minLines,
                                maxLines = inputFieldModel.maxLines,
                                singleLine = inputFieldModel.singleLine,
//                            visualTransformation = if (!showPassword.value) {
//                                PasswordVisualTransformation()
//                            } else {`
//                                VisualTransformation.None
//                            },
                            )
                        }

                        if (!readOnly)
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .wrapContentWidth()
                                    .animateContentSize(tween()),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                when (inputFieldModel.type) {
                                    InputFieldType.Search -> {
                                        if (textFieldValue.text.isNotEmpty()) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_close),
                                                contentDescription = "Close",
                                                tint = AppColors.ColorsPrimitivesOne,
                                                modifier = Modifier
                                                    .padding(end = 8.dp)
                                                    .size(20.dp)
                                                    .clickableWithoutRipple {
                                                        localFocusManager.clearFocus()
                                                        keyboardController?.hide()
                                                        onValueChange.invoke("")
                                                        onFocusChange?.invoke(false)
                                                    },
                                            )
                                        }
                                    }

                                    InputFieldType.DatePicker -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_calendar),
                                            modifier = Modifier
                                                .padding(end = 10.dp),
                                            contentDescription = "", tint = AppColors.OnPrimaryDark
                                        )
                                    }

                                    InputFieldType.Time -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_clock),
                                            modifier = Modifier
                                                .padding(end = 4.dp)
                                                .size(16.25.dp),
                                            contentDescription = "",
                                            tint = AppColors.OnPrimaryDark
                                        )
                                    }

                                    InputFieldType.Dropdown -> {
                                        val trailingIcon = when (dropDownType) {
                                            DropDownType.LanguageSelection, DropDownType.StringSelection -> R.drawable.ic_arrow_down
                                            else -> if (textFieldValue.text.isNotEmpty()) R.drawable.ic_close else R.drawable.ic_transparent
                                        }

                                        Icon(
                                            painter = painterResource(id = trailingIcon),
                                            "expand",
                                            tint = inputFieldModel.iconTintColor ?: Color.Black,
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(20.dp)
                                                .rotate(if (expanded.value && (dropDownType == DropDownType.LanguageSelection || dropDownType == DropDownType.StringSelection)) 180f else 0f)
                                                .clickableWithoutRipple {
                                                    when (trailingIcon) {
                                                        R.drawable.ic_search -> {
                                                            if (filteredModels.isEmpty() || textFieldValue.text.isEmpty()) {
                                                                filteredModels.clear()
                                                                filteredModels.addAll(originalModels)
                                                            }
                                                            expanded.value = !expanded.value
                                                            focusRequester.requestFocus()
                                                            hasFocus.value = true
                                                        }

                                                        R.drawable.ic_arrow_down -> {
                                                            if (filteredModels.isEmpty() || textFieldValue.text.isEmpty()) {
                                                                filteredModels.clear()
                                                                filteredModels.addAll(originalModels)
                                                            }
                                                            expanded.value = !expanded.value
                                                        }

                                                        R.drawable.ic_close -> {
                                                            textFieldValue =
                                                                textFieldValue.copy(text = "")
                                                            expanded.value = false
                                                            onEndSearch.invoke()
                                                        }
                                                    }
                                                },
                                        )
                                    }

//                            InputFieldType.Password -> {
//                                Icon(
//                                    painter = painterResource(
//                                        id = if (showPassword.value) {
//                                            R.drawable.ic_visibility_on
//                                        } else {
//                                            R.drawable.ic_visibility_off
//                                        },
//                                    ),
//                                    "expand",
//                                    tint = if (textFieldValue.text.isEmpty()) {
//                                        AppColors.ColorsPrimitivesThree
//                                    } else {
//                                        Color.Black
//                                    },
//                                    modifier = Modifier
//                                        .padding(end = 8.dp)
//                                        .size(20.dp)
//                                        .clickableWithoutRipple {
//                                            showPassword.value = !showPassword.value
//                                        },
//                                )
//                            }

                                    else -> {}
                                }

                                trailingView?.let {
                                    it()
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                    }
                    // Character counter
                    if (inputFieldModel.showCharCounter) {
                        inputFieldModel.maxCharLimit?.let { limit ->
                            CustomTextComponent(
                                text = "${textFieldValue.text.length}/$limit",
                                style = AppFonts.captionRegular,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 8.dp, end = 8.dp)
                            )
                        }
                    }
                }
            if (!informativeMessage.isNullOrEmpty() && !expanded.value)
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                    ) {
                        Icon(
                            painter = if (inputFieldModel.type == InputFieldType.Password) {
                                if (textFieldValue.text.isEmpty() || !textFieldValue.text.matches(
                                        informativeMessage.toRegex()
                                    )
                                ) {
                                    painterResource(id = R.drawable.ic_close)
                                } else {
                                    painterResource(id = R.drawable.ic_check)
                                }
                            } else {
                                painterResource(id = R.drawable.ic_information_icon)
                            },
                            "info",
                            tint = AppColors.ColorsPrimitivesTwo,
                            modifier = Modifier
                                .align(Alignment.Top)
                                .size(16.dp),
                        )
                        CustomTextComponent(
                            text = informativeMessage,
                            color = AppColors.ColorsPrimitivesTwo,
                            style = AppFonts.tagsSemiBold,
                            lineHeight = 20.79.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                }

            if (!errorMessage.isNullOrEmpty() && showErrorMessage == true) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_information_icon),
                        "info",
                        tint = AppColors.ColorFeedbackError,
                        modifier = Modifier
                            .size(16.dp),
                    )
                    CustomTextComponent(
                        text = errorMessage,
                        color = AppColors.ColorFeedbackError,
                        style = AppFonts.tagsSemiBold,
                    )
                }
            }

            val heightModifier =
                if (dropDownType == DropDownType.StringSelection) Modifier.heightIn(
                    0.dp,
                    250.dp
                ) else Modifier.wrapContentHeight()

            val columnScrollModifier = if (dropDownType == DropDownType.StringSelection)
                Modifier.verticalScrollWithScrollbar(
                    rememberScrollState(),
                    scrollbarConfig = ScrollbarConfig(
                        padding = PaddingValues(4.dp, 4.dp, 4.dp, 4.dp)
                    ),
                    defaultPlanning = true
                )
            else Modifier.wrapContentHeight()

            if (expanded.value) {
                Card(
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = shape,
                    modifier = heightModifier
                        .padding(top = topPadding)
                        .fillMaxWidth(),
                ) {
                    Column(modifier = columnScrollModifier) {
                        when (dropDownType) {
                            DropDownType.StringSelection -> ValueDropDownComponent(
                                filteredModels = filteredModels,
                                selectedItem = selectedItem
                            ) { item ->
                                selectedItem = item

                                val newText = selectedItem as String

                                val textLength = textFieldValue.text.length
                                val selectionStart =
                                    textFieldValue.selection.start.coerceIn(0, textLength)
                                val selectionEnd =
                                    textFieldValue.selection.end.coerceIn(0, textLength)
                                textFieldValue = textFieldValue.copy(
                                    newText,
                                    TextRange(selectionStart, selectionEnd),
                                )
                                onDropDownItemSelected?.invoke(
                                    selectedItem as String,
                                    null
                                )
                                expanded.value = false
                            }

                            DropDownType.LanguageSelection -> LanguageDropDownComponent(
                                filteredModels = filteredModels,
                                selectedItem = selectedItem
                            ) { item ->
                                selectedItem = item
                                val newText = (selectedItem as SupportedLanguage).value

                                val textLength = textFieldValue.text.length
                                val selectionStart =
                                    textFieldValue.selection.start.coerceIn(0, textLength)
                                val selectionEnd =
                                    textFieldValue.selection.end.coerceIn(0, textLength)
                                textFieldValue = textFieldValue.copy(
                                    newText,
                                    TextRange(selectionStart, selectionEnd),
                                )
                                onDropDownItemSelected?.invoke(
                                    (selectedItem as SupportedLanguage).code,
                                    null
                                )
                                expanded.value = false
                            }

                            DropDownType.SingleSelection -> {
                                Spacer(modifier = Modifier.height(10.dp))

                                SingleSelectionComponent(
                                    filteredModels = filteredModels,
                                    selectedItem
                                ) { item ->

                                    val newText =
                                        if ((item as DropDownModel).stringId == null) "" else item.label
                                    textFieldValue = textFieldValue.copy(
                                        newText,
                                        TextRange(newText.length),
                                    )

                                    onDropDownItemSelected?.invoke(
                                        item,
                                        kotlin.runCatching { (selectedItem as DropDownModel).stringId }
                                            .getOrNull()
                                    )
                                    selectedItem = item
                                    expanded.value = false
                                    localFocusManager.clearFocus()
                                    keyboardController?.hide()
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            DropDownType.MultiSelection -> {
                                MultiSelectionComponent(
                                    filteredModels = filteredModels,
                                    selectedItems = selectedItems,
                                    onItemSelected = { item ->
                                        if (selectedItems.contains((item as DropDownModel)))
                                            selectedItems.remove(item)
                                        else
                                            selectedItems.add(item)

                                        onDropDownItemSelected?.invoke(item, item.stringId)
                                    },
                                    showNoDataSection = displayUnAvaiableSection,
                                    displayImageInDropDownItem = dropdownWithImage,

                                    ) {
                                    textFieldValue = textFieldValue.copy(
                                        "",
                                        TextRange(0),
                                    )
                                    expanded.value = false
                                    localFocusManager.clearFocus()
                                    keyboardController?.hide()
//                                    selectedItem = item
//
//                                    val newText =
//                                        if ((item as DropDownModel).id == -1) "" else item.title
//                                    textFieldValue = textFieldValue.copy(
//                                        newText,
//                                        TextRange(newText.length),
//                                    )
//                                    if (item.id != -1)
//                                        onDropDownItemSelected?.invoke(item)
//
                                    expanded.value = false
                                    onDoneClick?.invoke()
                                }
                            }

                            DropDownType.NONE -> {}
                        }
                    }
                }
            }

            AnimatedVisibility(visible = selectedItems.isNotEmpty()) {

                LazyRow(
                    modifier = Modifier
                        .padding(top = if (readOnly) 0.dp else 8.dp)
                        .fillMaxWidth()
                        .zIndex(5f)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(4.dp), clip = true)
                        .background(
                            shape = RoundedCornerShape(4.dp),
                            color = if (!enabled) AppColors.ColorsPrimitivesFive else Color.White
                        ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(all = 12.dp)
                ) {
                    items(selectedItems) { tag ->
                        if (tag.label.isEmpty()) return@items
                        CategoryTagComponent(
                            text = tag.label,
                            icon = R.drawable.ic_close,
                            withEndIcon = !readOnly,
                            readOnly = readOnly,
                            itemSize = ItemSize.Large,
                            itemState = ItemState.DropDown,
                        ) {
                            if (readOnly) return@CategoryTagComponent
                            selectedItems.remove(tag)
                            onDropDownItemSelected?.invoke(tag, tag.stringId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ValueDropDownComponent(
    filteredModels: List<Any>,
    selectedItem: Any?,
    onItemSelected: (Any) -> Unit
) = filteredModels.forEachIndexed { index, item ->

    val valueItem = item as String

    DropdownMenuItem(
        text = {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CustomTextComponent(
                    text = valueItem,
                    style = AppFonts.subtitle1SemiBold,
                    color = AppColors.BLACK90,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp, end = 16.dp)
                )

                if (valueItem == selectedItem as? String)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        "expand",
                        tint = Color.Unspecified,
                    )
            }
        },
        onClick = { onItemSelected(item) },
        contentPadding = PaddingValues(15.dp)
    )
    if (index < filteredModels.lastIndex) {
        HorizontalDivider(
            thickness = 0.25.dp,
            color = AppColors.SwitchButtonTrackColorDisabled
        )
    }
}

@Composable
private fun LanguageDropDownComponent(
    filteredModels: List<Any>,
    selectedItem: Any?,
    onItemSelected: (Any) -> Unit
) = filteredModels.forEachIndexed { index, item ->

    val languageItem = item as SupportedLanguage

    DropdownMenuItem(
        text = {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextComponent(
                    text = languageItem.flag.toFlagEmoji(),
                    style = AppFonts.heading7SemiBold,
                    modifier = Modifier
                )

                CustomTextComponent(
                    text = languageItem.value,
                    style = AppFonts.body2Medium,
                    color = Color.Black,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp, end = 16.dp)
                )

                if (languageItem.code == (selectedItem as? SupportedLanguage)?.code)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        "expand",
                        tint = Color.Unspecified,
                    )
            }
        },
        onClick = { onItemSelected(item) },
        contentPadding = PaddingValues(15.dp)
    )
    if (index < filteredModels.lastIndex) {
        HorizontalDivider(
            thickness = 0.25.dp,
            color = AppColors.SwitchButtonTrackColorDisabled
        )
    }
}

@Composable
private fun SingleSelectionComponent(
    filteredModels: List<Any>,
    selectedItem: Any?,
    onItemSelected: (selected: Any?) -> Unit
) {

//    AnimatedVisibility(
//        visible = filteredModels.isEmpty(), modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(0.dp, 200.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .padding(vertical = 15.dp)
//        ) {
//            EmptyViewComponent(
//                model = EmptyViewModel(
//                    title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
//                    description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
//                    imageResId = R.mipmap.ic_no_data
//                )
//            )
//        }
//    }

    if (filteredModels.isNotEmpty())
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, 200.dp)
        ) {
            itemsIndexed(filteredModels) { index, item ->
                val dropDownItem = item as DropDownModel
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButtonComponent(
                                enabled = true,
                                title = dropDownItem.label,
                                onSelectedChange = {
                                    onItemSelected.invoke(dropDownItem)
                                },
                                selected = selectedItem == item,
                            )
                        }
                    },
                    onClick = { onItemSelected(item) },
                    contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
                )
            }
        }
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun MultiSelectionComponent(
    filteredModels: List<Any>,
    selectedItems: Any?,
    onItemSelected: (selected: Any?) -> Unit,
    showNoDataSection: Boolean = false,
    displayImageInDropDownItem: Boolean = false,
    onDoneClick: () -> Unit
) {
    val state = rememberScrollState()

//    if (showNoDataSection)
//        AnimatedVisibility(
//            visible = filteredModels.isEmpty(), modifier = Modifier
//                .fillMaxWidth()
//                .heightIn(0.dp, 200.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .padding(vertical = 15.dp)
//            ) {
//                EmptyViewComponent(
//                    model = EmptyViewModel(
//                        title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
//                        description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
//                        imageResId = R.mipmap.ic_no_data
//                    )
//                )
//            }
//        }

    Column {
        Column(
            modifier = Modifier
                .heightIn(0.dp, 120.sdp)
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                .verticalScrollWithScrollbar(
                    state,
                    defaultPlanning = true
                )
        ) {
            filteredModels.onEachIndexed { index, item ->
                val dropDownItem = item as DropDownModel
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CheckBoxComponent(
                                textColor = AppColors.BLACK,
                                enabled = true,
                                title = dropDownItem.label,
                                onCheckedChange = { _ ->
                                    onItemSelected.invoke(dropDownItem)
                                },
                                checked = if ((selectedItems as SnapshotStateList<*>).contains(
                                        dropDownItem
                                    )
                                ) FilterCheckboxState.Checked else FilterCheckboxState.Unchecked,
                                displayImage = displayImageInDropDownItem,
                                imageUrl = ""
                            )

                        }
                    },
                    onClick = { onItemSelected(item) },
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 0.dp),

                    )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .heightIn(0.dp, 120.sdp),
//                    contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
//                ) {
//                    itemsIndexed(filteredModels) { index, item ->
//                        val dropDownItem = item as DropDownModel
//                        DropdownMenuItem(
//                            text = {
//                                Row(
//                                    modifier = Modifier,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    CheckBoxComponent(
//                                        textColor = AppColors.BLACK,
//                                        enabled = true,
//                                        title = dropDownItem.label,
//                                        onCheckedChange = { _ ->
//                                            onItemSelected.invoke(dropDownItem)
//                                        },
//                                        checked = if ((selectedItems as SnapshotStateList<*>).contains(
//                                                dropDownItem
//                                            )
//                                        ) FilterCheckboxState.Checked else FilterCheckboxState.Unchecked,
//                                        displayImage = displayImageInDropDownItem,
//                                        imageUrl = ""
//                                    )
//
//                                }
//                            },
//                            onClick = { onItemSelected(item) },
//                            contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
//
//                            )
//                    }
//                }
        if (filteredModels.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            ButtonComponent(
                text = LanguageConstants.DONE_BUTTON_TITLE.localizeString(),
                clickListener = {
                    onDoneClick.invoke()
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                    .fillMaxWidth(),
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.MEDIUM
            )
        }
    }
}

private fun showDropDownList(
    textFieldValue: TextFieldValue,
    filteredModels: SnapshotStateList<Any>,
    originalModels: List<Any>,
    expanded: MutableState<Boolean>,
    isDropDownUserSearch: Boolean = false,
    isMultiDropDown: Boolean = false,
    addNoneOption: Boolean? = true
) {
    if (textFieldValue.text.isEmpty() && isDropDownUserSearch) {
        filteredModels.clear()
//        filteredModels.addAll(originalModels)
    } else {
        filteredModels.clear()

        if (!isDropDownUserSearch) {
            filteredModels.addAll(
                originalModels.filter {
                    (it as DropDownModel).label.lowercase()
                        .contains(textFieldValue.text.lowercase())
                },
            )
        }
    }
    if (addNoneOption == true) {
        if (filteredModels.size == originalModels.size && filteredModels.isNotEmpty())
            filteredModels.add(
                filteredModels.size - 1,
                DropDownModel(id = -1, stringId = null, label = NONE.localizeString())
            )
    }


    expanded.value = true
}

@Preview(showBackground = true)
@Composable
fun InputFieldComponentPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            InputFieldComponent(
                title = "Search with error",
                value = "",
                inputFieldModel = InputFieldModel(

                    placeholderText = "Search Here",
                    type = InputFieldType.Search,
                ),
                errorMessage = "Error",
                onValueChange = {},
            )
            InputFieldComponent(
                title = "Normal with info",
                value = "",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Enter value",
                    type = InputFieldType.Normal,
                ),
                onValueChange = {},
                informativeMessage = "Info",
            )

            InputFieldComponent(
                title = "Phone Number",
                countryPhoneNumber = DropDownModel("saudi", "sa"),
                value = "+966 559043105",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Enter value",
                    type = InputFieldType.PhoneNumber,
                ),
                onValueChange = {},
            )

            InputFieldComponent(
                title = "Phone Number",
                value = "+966 559043105",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Enter value",
                    type = InputFieldType.DatePicker,
                ),
                onValueChange = {},
            )
        }
    }
}