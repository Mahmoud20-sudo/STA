package sa.sauditourism.employee.modules.account.myRequests

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.AnimateExpandableList
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CategoryTagComponent
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.FilterCheckboxState
import sa.sauditourism.employee.components.FilterModel
import sa.sauditourism.employee.components.FooterComponent
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.ItemSize
import sa.sauditourism.employee.components.ItemState
import sa.sauditourism.employee.components.ModalBottomSheet
import sa.sauditourism.employee.components.RadioButtonComponent
import sa.sauditourism.employee.components.ToastPresentable
import sa.sauditourism.employee.components.ToastType
import sa.sauditourism.employee.components.ToastView
import sa.sauditourism.employee.constants.CommonConstants.FILTER_TYPE
import sa.sauditourism.employee.constants.CommonConstants.OWNED_REQUESTS
import sa.sauditourism.employee.constants.CommonConstants.PARTICIPATED_REQUESTS
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.account.myRequests.comonents.MyRequestItem
import sa.sauditourism.employee.modules.account.myRequests.comonents.MyRequestList
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequest
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.services.model.details.Request
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyRequestsScreen(
    onBackButtonClick: () -> Unit,
    onItemClick: (MyRequest) -> Unit,
    onDismissButtonClicked: () -> Unit,
    requestsViewModel: MyRequestsViewModel = hiltViewModel()
) {

    var searchQuery by remember { mutableStateOf("") }
    val myRequestsFlow by requestsViewModel.myRequestsFlow.collectAsState()
    val servicesFilterFlow by requestsViewModel.servicesFiltersFlow.collectAsState()
    val firstLaunch = remember { mutableStateOf(true) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var filtersNum by remember { mutableIntStateOf(0) }
    var isHaveFilters by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val filtersList = remember { mutableStateListOf<ServicesModel>() }
    val radioOptions = remember { mutableStateListOf<FilterModel>() }

    val myRequestsList = remember { mutableStateListOf<MyRequest>() }

    var selectedRequestId by remember { mutableStateOf("") }
    var selectedAssignee by remember { mutableStateOf("") }

    var recentSelectedRequestId by remember { mutableStateOf("") }
    var recentSelectedAssignee by remember { mutableStateOf("") }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = if (myRequestsFlow.data?.isNotEmpty() == true) myRequestsFlow is UiState.Loading else false,
        onRefresh = {
            requestsViewModel.getMyRequest(
                selectedRequestId.ifEmpty { null },
                selectedAssignee.ifEmpty { null })
        }
    )

    val onDismissInvoked = {
        if (filtersNum == 0) {
            selectedRequestId = ""
            selectedAssignee = ""
            recentSelectedRequestId = ""
            recentSelectedAssignee = ""
        }
        if (selectedRequestId.isEmpty()) {
            recentSelectedRequestId = ""
            filtersList.onEach { it.isSelected.value = FilterCheckboxState.Unchecked }
                .flatMap { it.requestsTypes }.onEach { request ->
                    request.isSelected.value = FilterCheckboxState.Unchecked
                }
        }

        if (selectedAssignee.isEmpty()) {
            recentSelectedAssignee = ""
            radioOptions.onEach { it.isSelected.value = FilterCheckboxState.Unchecked }
        }

        if (recentSelectedRequestId.isNotEmpty())
            filtersList.flatMap { it.requestsTypes }
                .first { it.id == recentSelectedRequestId }.isSelected.value =
                FilterCheckboxState.Checked

        if (recentSelectedAssignee.isNotEmpty())
            radioOptions.first { it.id == recentSelectedAssignee }.isSelected.value =
                FilterCheckboxState.Checked

        showFilterSheet = false
    }

    LaunchedEffect(searchQuery) {
        if (firstLaunch.value) {
            firstLaunch.value = false
            return@LaunchedEffect
        }
        requestsViewModel.onSearchQueryChanged(query = searchQuery)
    }

    LaunchedEffect(myRequestsFlow) {
        if (myRequestsFlow is UiState.Success) {
            myRequestsFlow.data?.let { list ->
                myRequestsList.clear()
                val items = if (searchQuery.isEmpty()) list else list.filter {
                    it.title.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                }
                myRequestsList.addAll(items)
            }
        }
    }

    LaunchedEffect(servicesFilterFlow) {
        isLoading = servicesFilterFlow !is UiState.Success
        showError = servicesFilterFlow is UiState.Error
        if (servicesFilterFlow is UiState.Success) {
            servicesFilterFlow.data?.let {
                filtersList.clear()
                filtersList.addAll(it)
            }
        }
    }

//    LaunchedEffect(selectedRequestId, selectedAssignee) {
//        filtersNum = if (selectedAssignee.isNotEmpty() && selectedRequestId.isNotEmpty()) 2
//        else if (selectedAssignee.isNotEmpty() || selectedRequestId.isNotEmpty()) 1
//        else 0
//    }

    LaunchedEffect(true) {
        requestsViewModel.init()
        radioOptions.addAll(
            listOf(
                FilterModel(OWNED_REQUESTS, LanguageConstants.CREATED_BY_ME.localizeString()),
                FilterModel(
                    PARTICIPATED_REQUESTS,
                    LanguageConstants.ASSIGNED_TO_ME.localizeString()
                )
            )
        )
    }

    ChangeStatusBarIconsColors(Color.White)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            val headerModel = HeaderModel(
                title = LanguageConstants.MY_REQUESTS.localizeString(),
                showSearch = true,
                subTitle = "",
                showBackIcon = true,
                searchValue = searchQuery,
                showFilterIcon = true,
                favoriteIconRes = R.drawable.ic_filter,
                tags = listOf(
                    filtersList.flatMap { it.requestsTypes }
                        .find { it.id == selectedRequestId }?.title?.ifEmpty { null },
                    radioOptions.find { it.id == selectedAssignee }?.title?.ifEmpty { null }),
                filtersNumber = filtersNum
            )
            HeaderComponent(
                headerModel,
                onSearchValueChange = { searchQuery = it },
                isStandAlone = true,
                titleMaxLines = 1,
                backHandler = {
                    onBackButtonClick.invoke()
                    true
                },
                onTagClick = { index ->
                    when (index) {
                        0 -> {
                            selectedRequestId = ""
                            if (filtersNum > 0) filtersNum--
                            onDismissInvoked.invoke()
                            requestsViewModel.getMyRequest(null, selectedAssignee.ifEmpty { null })
                        }

                        else -> {
                            selectedAssignee = ""
                            if (filtersNum > 0) filtersNum--
                            onDismissInvoked.invoke()
                            requestsViewModel.getMyRequest(selectedRequestId.ifEmpty { null }, null)
                        }
                    }
                },
                filterClick = { showFilterSheet = true }
            )

//            AnimatedVisibility(visible = filtersNum > 0) {
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .horizontalScroll(rememberScrollState())
//                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 10.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    if (selectedAssignee.isNotEmpty())
//                        CategoryTagComponent(
//                            text = "${radioOptions.find { it.id == selectedAssignee }?.title}",
//                            icon = R.drawable.ic_close,
//                            withEndIcon = true,
//                            readOnly = false,
//                            itemSize = ItemSize.Large,
//                            itemState = ItemState.Active,
//                        ) {
//                            selectedAssignee = ""
//                            if (filtersNum > 0) filtersNum--
//                            onDismissInvoked.invoke()
//                            requestsViewModel.getMyRequest(selectedRequestId.ifEmpty { null }, null)
//                        }
//
//                    if (selectedRequestId.isNotEmpty())
//                        CategoryTagComponent(
//                            text = "${
//                                filtersList.flatMap { it.requestsTypes }
//                                    .find { it.id == selectedRequestId }?.title
//                            }",
//                            icon = R.drawable.ic_close,
//                            withEndIcon = true,
//                            readOnly = false,
//                            itemSize = ItemSize.Large,
//                            itemState = ItemState.Active,
//                        ) {
//                            selectedRequestId = ""
//                            if (filtersNum > 0) filtersNum--
//                            onDismissInvoked.invoke()
//                            requestsViewModel.getMyRequest(null, selectedAssignee.ifEmpty { null })
//                        }
//                }
//            }
            if (myRequestsFlow is UiState.Loading) {
                MyRequestsShimmerSkeleton()
            }

            if (myRequestsFlow is UiState.Error) {
                requestsViewModel.HandleError(
                    error = myRequestsFlow.networkError,
                    isDataEmpty = myRequestsFlow.data?.isEmpty() ?: false,
                    showBack = false,
                    withTab = true,
                    dismissCallback = {
                        onDismissButtonClicked.invoke()
                    },
                ) {
                    requestsViewModel.getMyRequest()

                }
            }

            if (myRequestsFlow is UiState.Success) {
                if (myRequestsList.isNotEmpty()) {
                    MyRequestList(modifier = Modifier, myRequestsList) { request ->
                        if (request.id == "N/A") return@MyRequestList
                        onItemClick(request)
                    }
                } else {
                    EmptyViewComponent(
                        model = EmptyViewModel(
                            title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                            description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                            imageResId = R.mipmap.ic_no_data
                        )
                    )
                }
            }
        }
        PullRefreshIndicator(
            refreshing = myRequestsFlow is UiState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )

        if (showFilterSheet)
            ModalBottomSheet(
                topPadding = 150.dp,
                content = { _ ->
                    Box {
                        Column(
                            Modifier
                                .padding(horizontal = 24.dp)
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
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomTextComponent(
                                text = LanguageConstants.CHOOSE_SERVICE_CATEGORY.localizeString(),
                                style = AppFonts.heading7Regular.copy(color = AppColors.ColorsPrimitivesTwo),
                                lineHeight = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            AnimateExpandableList(
                                list = filtersList, isLoading
                            ) { _, request ->
                                selectedRequestId =
                                    if (request.isSelected.value == FilterCheckboxState.Checked) request.id else ""
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(bottom = 20.dp, top = 20.dp),
                                thickness = 1.dp,
                                color = AppColors.ColorsPrimitivesFour
                            )

                            radioOptions.onEach { child ->
                                RadioButtonComponent(
                                    modifier = Modifier,
                                    title = child.title,
                                    onSelectedChange = { checked ->
                                        radioOptions.forEach {
                                            it.isSelected.value = FilterCheckboxState.Unchecked
                                        }
                                        selectedAssignee = if (checked) child.id else ""
                                        child.isSelected.value =
                                            if (checked) FilterCheckboxState.Checked else FilterCheckboxState.Unchecked
                                    },
                                    selected = child.isSelected.value == FilterCheckboxState.Checked,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }

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
                                        if (filtersNum > 0) {
                                            //RESET IF USER SELECT WITHOUT SAVE
                                            selectedRequestId = recentSelectedRequestId
                                            selectedAssignee = recentSelectedAssignee
                                        }
                                        onDismissInvoked.invoke()
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
                                        filtersNum =
                                            if (selectedAssignee.isNotEmpty() && selectedRequestId.isNotEmpty()) 2
                                            else if (selectedAssignee.isNotEmpty() || selectedRequestId.isNotEmpty()) 1
                                            else 0
                                        requestsViewModel.getMyRequest(
                                            selectedRequestId.ifEmpty { null },
                                            selectedAssignee.ifEmpty { null }
                                        )
                                        if (filtersNum > 0) isHaveFilters = true
                                        showFilterSheet = false
                                        recentSelectedAssignee = selectedAssignee
                                        recentSelectedRequestId = selectedRequestId
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    buttonType = ButtonType.PRIMARY,
                                    buttonSize = ButtonSize.LARGE,
                                    enabled = selectedAssignee.isNotEmpty() || selectedRequestId.isNotEmpty(),
                                )
                            }
                        }

                    }
                }, onDismissed = {
                    if (filtersNum > 0) {
                        //RESET IF USER SELECT WITHOUT SAVE
                        selectedRequestId = recentSelectedRequestId
                        selectedAssignee = recentSelectedAssignee
                    }
                    onDismissInvoked.invoke()
                }
            )

        if (showError)
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Error,
                    title = servicesFilterFlow.networkError?.message
                        ?: LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString(),
                ),
                toastDuration = 5000L,
                onDismiss = {
                    showError = false
                },
            )
    }
}

@Composable
private fun MyRequestsShimmerSkeleton() = (List(6) { MyRequest() }).forEach { item ->
    Box(Modifier.padding(horizontal = 16.dp)) {
        MyRequestItem(
            myRequest = item,
            isLoading = true
        )
    }
}
