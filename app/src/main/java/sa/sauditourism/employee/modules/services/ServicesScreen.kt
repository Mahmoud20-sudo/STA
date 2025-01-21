package sa.sauditourism.employee.modules.services

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.MediumCardComponent
import sa.sauditourism.employee.components.RequestsListComponent
import sa.sauditourism.employee.components.SearchHeaderComponent
import sa.sauditourism.employee.components.SearchHeaderComponentModel
import sa.sauditourism.employee.components.SearchHeaderType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.TestTagsConstants.SERVICES_LIST_TAG
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.services.requeststypes.RequestsTypesViewModel
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServicesScreen(
    navController: NavController,
    servicesViewModel: ServicesViewModel = hiltViewModel(),
    requestsTypesViewModel: RequestsTypesViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        var searchQuery by remember { mutableStateOf("") }
        var isSearching by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var isRequestTypesLoading by remember { mutableStateOf(false) }

        val servicesFlow by servicesViewModel.servicesFlow.collectAsState()
        val services = remember { mutableStateListOf<ServicesModel>() }
        val lazyGridMaxHeight = if (services.isEmpty()) 450.dp else (400 * (services.size / 2)).dp
        val requestsTypesFlow by requestsTypesViewModel.requestsTypes.collectAsState()

        val requestsTypes = remember { mutableStateListOf<RequestsType>() }

        val regularRequestsTypes = requestsTypes.filter { !it.comingSoon }
        val comingSoonList = requestsTypes.filter { it.comingSoon }

        val isRefreshing = if(searchQuery.isNotEmpty()) requestsTypes.isNotEmpty().and(isRequestTypesLoading) else services.isNotEmpty().and(isLoading)

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                if (searchQuery.isNotEmpty())
                    requestsTypesViewModel.searchRequestsTypes(searchQuery)
                else
                    servicesViewModel.getServices()
            }
        )

        LaunchedEffect(servicesFlow) {
            isLoading = servicesFlow is UiState.Loading
            if (servicesFlow is UiState.Success) {
                servicesFlow.data?.servicesLList?.let {
                    services.clear()
                    services.addAll(it)
                }
            }
        }

        LaunchedEffect(searchQuery) {
            if (searchQuery.isNotEmpty())
                requestsTypesViewModel.searchRequestsTypes(searchQuery)
        }

        LaunchedEffect(requestsTypesFlow) {
            isRequestTypesLoading = requestsTypesFlow is UiState.Loading
            if (requestsTypesFlow is UiState.Success) {
                requestsTypesFlow.data?.let { list ->
                    requestsTypes.clear()
                    requestsTypes.addAll(list.requestsTypeList)
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ChangeStatusBarIconsColors()

            Spacer(modifier = Modifier.height(if (!isSearching) 40.dp else 0.dp))

            SearchHeaderComponent(
                SearchHeaderComponentModel(
                    title = if (!isSearching) LanguageConstants.SERVICES_TAB.localizeString() else "",
                    searchValue = searchQuery,
                    searchHeaderType = SearchHeaderType.BIG,
                    showBack = isSearching || searchQuery.isNotEmpty()
                ),
                onSearchValueChange = {
                    searchQuery = it
                },
                onFocusChange = {
                    isSearching = it
                }
            )

            AnimatedVisibility(visible = !isSearching) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    CustomTextComponent(
                        text = LanguageConstants.SERVICES_CATEGORIES.localizeString(),
                        style = AppFonts.heading4Bold,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    LazyVerticalGrid(
                        userScrollEnabled = false,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .testTag(SERVICES_LIST_TAG)
                            .heightIn(0.dp, lazyGridMaxHeight),
                        // content padding
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = 16.dp,
                            start = 16.dp,
                            end = 10.dp
                        ),
                        content = {
                            if (isLoading) {
                                serviceShimmerSkeleton()
                                return@LazyVerticalGrid
                            }
                            itemsIndexed(services) { index, item ->
                                MediumCardComponent(
                                    servicesModel = item,
                                    isLoading = isLoading,
                                    modifier = Modifier.clickableWithoutRipple {
                                        if (item.comingSoon) return@clickableWithoutRipple
                                        navController.navigate("${Routes.REQUESTS_TYPES}/${item.id}/${item.title}")
                                    }
                                )
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(visible = isSearching) {
                Column {
                    if (requestsTypesFlow !is UiState.Error && searchQuery.isNotEmpty())
                        RequestsListComponent(
                            list = regularRequestsTypes,
                            isLoading = isRequestTypesLoading,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = true
                                )
                        ) { request ->
                            navController.navigate(
                                "${Routes.REQUESTS_FORM}/${request.serviceId}/${request.id}/${
                                    request.title.replace(
                                        "/",
                                        "$"
                                    )
                                }"
                            )
                        }

                    Spacer(modifier = Modifier.heightIn(24.dp))

                    if (comingSoonList.isNotEmpty()) {
                        RequestsListComponent(
                            list = comingSoonList,
                            enabled = false,
                            isLoading = isRequestTypesLoading,
                            offsetValue = (-23).dp,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = true
                                )
                        )
                    }
                }
            }
        }

        when (isSearching) {
            true -> if (requestsTypesFlow is UiState.Success && requestsTypes.isEmpty()) {
                EmptyViewComponent(
                    model = EmptyViewModel(
                        title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                        description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                        imageResId = R.mipmap.ic_no_data
                    )
                )
            }

            else -> if (servicesFlow !is UiState.Loading && searchQuery.isNotEmpty()) {
                EmptyViewComponent(
                    model = EmptyViewModel(
                        title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                        description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                        imageResId = R.mipmap.ic_no_data
                    )
                )
            }
        }

        PullRefreshIndicator(
            refreshing = if(searchQuery.isNotEmpty()) isRequestTypesLoading else isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )

        if (servicesFlow is UiState.Error) {
            servicesViewModel.HandleError(
                error = servicesFlow.networkError,
                isDataEmpty = servicesFlow.data?.servicesLList.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    navController.popBackStack()
                },
            ) {
                servicesViewModel.getServices()
            }
        }

        if (requestsTypesFlow is UiState.Error) {
            requestsTypesViewModel.HandleError(
                error = requestsTypesFlow.networkError,
                isDataEmpty = requestsTypesFlow.data?.requestsTypeList.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    navController.popBackStack()
                },
            ) {
                requestsTypesViewModel.searchRequestsTypes("")
            }
        }
    }
}

private fun LazyGridScope.serviceShimmerSkeleton() = items(List(4) { ServicesModel() }) { item ->
    MediumCardComponent(
        servicesModel = item,
        isLoading = true,
    )
}