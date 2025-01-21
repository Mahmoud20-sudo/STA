package sa.sauditourism.employee.modules.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CategoryTagComponent
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.DropDownType
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.FooterComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.components.ItemSize
import sa.sauditourism.employee.components.ItemState
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.ModalBottomSheet
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.openBrowser
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.model.Payslip
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaySlipScreen(
    navController: NavController,
    viewModel: PaySlipsViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var filtersNum by remember { mutableIntStateOf(0) }
    val date by remember { mutableStateOf<String?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    val paySlipFlow by viewModel.paySlipsFlow.collectAsState()
    val paySlipsList = remember { mutableStateListOf<Payslip>() }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var yearsList = remember { mutableStateListOf<String>() }
    val monthsList = remember { mutableStateListOf<String>() }

    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedMonth by remember { mutableStateOf<String?>(null) }

    var recentSelectedYear by remember { mutableStateOf<String?>(null) }
    var recentSelectedMonth by remember { mutableStateOf<String?>(null) }

    var tags by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(true) {
        (6 downTo 0).onEach {
            yearsList.add("${calendar.get(Calendar.YEAR)}")
            calendar.add(Calendar.YEAR, -1)
        }

        calendar.set(Calendar.MONTH, 0)
        (11 downTo 0).onEach {
            val formatter =
                SimpleDateFormat("MMMM", Locale(sharedPreferencesManager.preferredLocale ?: "en"))
            monthsList.add(formatter.format(calendar.time))
            calendar.add(Calendar.MONTH, 1)
        }

        yearsList.apply { reverse() }
        viewModel.getPaySlips(date)
    }

    LaunchedEffect(key1 = paySlipFlow) {
        isLoading = paySlipFlow is UiState.Loading
        if (paySlipFlow is UiState.Success) {
            paySlipFlow.data?.payslips?.let { payslips ->
                paySlipsList.clear()
                paySlipsList.addAll(payslips)
            }
        }
    }

    val onPaySlipRetrieve = {
        if (selectedYear.isNotNull() && selectedMonth.isNotNull()) {
            recentSelectedMonth = selectedMonth
            recentSelectedYear = selectedYear
            viewModel.getPaySlips("$selectedYear-${monthsList.indexOfMonth(selectedMonth)}-25")
            tags = "$recentSelectedMonth - $recentSelectedYear"
        } else {
            tags = null
            viewModel.getPaySlips()
        }
    }

    if (paySlipFlow is UiState.Error)
        viewModel.HandleError(
            error = paySlipFlow.networkError,
            isDataEmpty = paySlipFlow.data?.payslips == null,
            showBack = false,
            withTab = true,
            dismissCallback = {
                navController.popBackStack()
            },
        ) {
            onPaySlipRetrieve.invoke()
        }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = false
            onPaySlipRetrieve.invoke()
        }
    )

    val state = rememberScrollState()
    val showOverlay by remember {
        derivedStateOf {
            state.value == 0
        }
    }

    ChangeStatusBarIconsColors()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .pullRefresh(pullRefreshState),
            verticalArrangement = Arrangement.Top
        ) {
            DetailsHeaderComponent(
                HeaderModel(
                    title = LanguageConstants.ACCOUNT_MY_PAYSLIP.localizeString(),
                    showFilterIcon = true,
                    favoriteIconRes = R.drawable.ic_filter,
                    tags = if (tags.isNullOrEmpty()) null else listOf(tags),
                    filtersNumber = filtersNum
                ),
                isStandAlone = true,
                showShadow = false,
                backgroundColor = Color.Transparent,
                backHandler = {
                    navController.popBackStack()
                    true
                },
                onTagClick = {
                    selectedMonth = null
                    selectedYear = null
                    filtersNum = 0
                    tags = null
                    viewModel.getPaySlips()
                },
                filterClick = { showFilterSheet = true },
                showOverlay = showOverlay
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .verticalScroll(state)
            ) {
                Spacer(modifier = Modifier.height(if (filtersNum > 0) 0.dp else 20.dp))

//                if (filtersNum > 0) {
//                    CategoryTagComponent(
//                        text = "$recentSelectedMonth - $recentSelectedYear",
//                        icon = R.drawable.ic_close,
//                        withEndIcon = true,
//                        readOnly = false,
//                        itemSize = ItemSize.Large,
//                        itemState = ItemState.Active,
//                        modifier = Modifier.padding(horizontal = 24.dp)
//                    ) {
//                        selectedMonth = null
//                        selectedYear = null
//                        filtersNum = 0
//                        viewModel.getPaySlips()
//                    }
//                    Spacer(modifier = Modifier.height(20.dp))
//                }


                if (isLoading)
                    (0..6).onEach {
                        PaySlipRow(isLoading = true)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                if (paySlipFlow is UiState.Success && paySlipsList.isNotEmpty())
                    paySlipsList.onEachIndexed { _, payslip ->
                        PaySlipRow(payslip = payslip) { url ->
                            context.openBrowser(url, external = true)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )

        if (paySlipFlow !is UiState.Loading && paySlipFlow.data?.payslips?.isEmpty() == true)
            EmptyViewComponent(
                model = EmptyViewModel(
                    title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                    description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                    imageResId = R.mipmap.ic_no_data
                )
            )

        if (showFilterSheet)
            ModalBottomSheet(
                topPadding = 150.dp,
                content = { _ ->
                    Box {
                        Column(
                            Modifier
                                .padding(horizontal = 16.dp)
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            CustomTextComponent(
                                text = LanguageConstants.FILTER.localizeString(),
                                style = AppFonts.heading1Medium.copy(color = AppColors.BLACK),
                                lineHeight = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.shimmerModifier(isLoading)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            CustomTextComponent(
                                text = LanguageConstants.SELECT_MONTH.localizeString(),
                                style = AppFonts.heading7Regular.copy(color = AppColors.ColorsPrimitivesTwo),
                                lineHeight = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.shimmerModifier(isLoading)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            InputFieldComponent(
                                value = selectedMonth ?: "",
                                title = null,
                                inputFieldModel = InputFieldModel(
                                    type = InputFieldType.Dropdown,
                                    dropdownWithSearchIcon = false,
                                    iconTintColor = AppColors.OnPrimaryDark,
                                    placeholderText = LanguageConstants.CHOOSE_MONTH.localizeString()
                                ),
                                originalModels = monthsList,
                                dropDownType = DropDownType.StringSelection,
                                readOnly = false,
                                clickable = true,
                                onDropDownItemSelected = { month, _ ->
                                    selectedMonth = month as String
                                },
                                onValueChange = {},
                                modifier = Modifier,
                                selectedDropDownItem = selectedMonth
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomTextComponent(
                                text = LanguageConstants.SELECT_YEAR.localizeString(),
                                style = AppFonts.heading7Regular.copy(color = AppColors.ColorsPrimitivesTwo),
                                lineHeight = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.shimmerModifier(isLoading)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            InputFieldComponent(
                                value = selectedYear ?: "",
                                title = null,
                                inputFieldModel = InputFieldModel(
                                    type = InputFieldType.Dropdown,
                                    dropdownWithSearchIcon = false,
                                    iconTintColor = AppColors.OnPrimaryDark,
                                    placeholderText = LanguageConstants.CHOOSE_YEAR.localizeString()
                                ),
                                originalModels = yearsList,
                                dropDownType = DropDownType.StringSelection,
                                readOnly = false,
                                clickable = true,
                                onDropDownItemSelected = { year, _ ->
                                    selectedYear = year as String
                                },
                                onValueChange = {},
                                modifier = Modifier,
                                selectedDropDownItem = selectedYear
                            )

                            Spacer(modifier = Modifier.height(120.dp))

                        }

                        FooterComponent(modifier = Modifier.align(Alignment.BottomCenter)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ButtonComponent(
                                    LanguageConstants.CANCEL.localizeString(),
                                    clickListener = {
                                        selectedMonth = recentSelectedMonth
                                        selectedYear = recentSelectedYear
                                        showFilterSheet = false
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    buttonType = ButtonType.SECONDARY,
                                    buttonSize = ButtonSize.LARGE
                                )
                                Spacer(modifier = Modifier.width(24.dp))

                                ButtonComponent(
                                    LanguageConstants.SAVE.localizeString(),
                                    clickListener = {
                                        filtersNum = 2
                                        onPaySlipRetrieve.invoke()
                                        showFilterSheet = false
                                        Timber.d(
                                            "DATA FORMAT : $selectedYear-${
                                                monthsList.indexOfMonth(
                                                    selectedMonth
                                                )
                                            }-25"
                                        )
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    buttonType = ButtonType.PRIMARY,
                                    buttonSize = ButtonSize.LARGE,
                                    enabled = selectedMonth.isNotNull() && selectedYear.isNotNull(),
                                )
                            }
                        }

                    }
                },
                onDismissed = { showFilterSheet = false }
            )
    }
}

@Composable
fun PaySlipRow(
    payslip: Payslip = Payslip(),
    isLoading: Boolean = false,
    onItemClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(16.dp), color = AppColors.WHITE)
            .shimmerModifier(isLoading)
            .zIndex(100f)
            .padding(16.dp)
            .clickableWithoutRipple {
                payslip.downloadUrl?.let {
                    onItemClick.invoke(
                        it.replace(
                            "PersonId",
                            "${sharedPreferencesManager.accountDetails?.id}"
                        )
                    )
                }
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val formattedDate = payslip.date.runCatching {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = formatter.parse(this)
            DateTimeFormatter.ofPattern(
                "MMMM - yyyy",
                Locale(sharedPreferencesManager.preferredLocale ?: "en")
            ).format(date)
        }.getOrElse { payslip.date }

        val mediaContent by remember {
            mutableStateOf(
                MediaContent(
                    GalleryType.IMAGE,
                    R.drawable.ic_pdf
                )
            )
        }

        MediaComponent(
            mediaContent = mediaContent,
            modifier = Modifier
                .size(24.dp)
                .shimmerModifier(isLoading)
        )

        Spacer(modifier = Modifier.width(8.dp))

        CustomTextComponent(
            text = formattedDate,
            style = AppFonts.subtitle1SemiBold.copy(color = AppColors.BLACK90),
            lineHeight = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            isLoading = isLoading,
            modifier = Modifier.shimmerModifier(isLoading)
        )
    }
}

private fun List<*>.indexOfMonth(month: String?) =
    if (indexOf(month) > 8) "${indexOf(month) + 1}" else "0${indexOf(month) + 1}"