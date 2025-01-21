package sa.sauditourism.employee.modules.services.requeststypes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.EmptyViewComponent
import sa.sauditourism.employee.components.EmptyViewModel
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.RequestTypeRowComponent
import sa.sauditourism.employee.components.RequestsListComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.services.model.RequestsType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestsTypesScreen(
    navController: NavController,
    serviceId: String,
    serviceTitle: String,
    viewModel: RequestsTypesViewModel = hiltViewModel(),
) {

    val requestsTypesFlow by viewModel.requestsTypes.collectAsState()
    val requestsTypes = remember { mutableStateListOf<RequestsType>() }
    val requestsTypesList = requestsTypes.filter { !it.comingSoon }
    val comingSoonList = requestsTypes.filter { it.comingSoon }

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = if(requestsTypes.isNotEmpty()) isSearching else false,
        onRefresh = {
            if (searchQuery.isNotEmpty())
                viewModel.filterRequestsType(searchQuery)
            else
                viewModel.searchRequestsTypes(serviceId = serviceId, searchQuery)
        }
    )

    LaunchedEffect(key1 = requestsTypesFlow) {
        isSearching = requestsTypesFlow is UiState.Loading
        if (requestsTypesFlow is UiState.Success) {
            requestsTypesFlow.data?.let {
                requestsTypes.clear()
                if (searchQuery.isNotEmpty()) {
                    requestsTypes.addAll(it.filterRequestsTypeList)
                } else {
                    requestsTypes.addAll(it.requestsTypeList)
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.searchRequestsTypes(serviceId = serviceId, "")
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty())
            viewModel.filterRequestsType(searchQuery)
        else {
            if (requestsTypesFlow is UiState.Success) {
                requestsTypesFlow.data?.let {
                    requestsTypes.clear()
                    if (searchQuery.isNotEmpty()) {
                        requestsTypes.addAll(it.filterRequestsTypeList)
                    } else {
                        requestsTypes.addAll(it.requestsTypeList)
                    }

                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ChangeStatusBarIconsColors(Color.White)
        if (requestsTypesList.isEmpty() && comingSoonList.isEmpty() && searchQuery.isNotEmpty()) {
            EmptyViewComponent(
                model = EmptyViewModel(
                    title = LanguageConstants.NO_RESULT_FOUND.localizeString(),
                    description = LanguageConstants.NO_RESULT_FOUND_MESSAGE.localizeString(),
                    imageResId = R.mipmap.ic_no_data
                )
            )
        }
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            val model = HeaderModel(
                title = serviceTitle,
                showSearch = true,
                subTitle = "",
                showBackIcon = true,
                searchValue = searchQuery,
            )
            HeaderComponent(
                model,
                onSearchValueChange = { searchQuery = it },
                isStandAlone = true,
                titleMaxLines = 2,
                backHandler = { navController.popBackStack() })

            Column(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState())
            ) {
                if (isSearching) {
                    RequestsTypesShimmerSkeleton()
                } else {
                    if (requestsTypesList.isNotEmpty()) {
                        RequestsListComponent(
                            list = requestsTypesList,
                            modifier = Modifier
                                .padding(all = 24.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = true
                                ),
                            onClick = { request ->
                                navController.navigate(
                                    "${Routes.REQUESTS_FORM}/${request.serviceId}/${request.id}/${
                                        request.title.replace(
                                            "/",
                                            "$"
                                        )
                                    }"
                                )
                            }
                        )
                    }
                    if (comingSoonList.isNotEmpty()) {
                        RequestsListComponent(
                            list = comingSoonList,
                            enabled = false,
                            offsetValue = (-8).dp,
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 16.dp)
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

        PullRefreshIndicator(
            refreshing = isSearching,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )

        if (requestsTypesFlow is UiState.Error) {
            viewModel.HandleError(
                error = requestsTypesFlow.networkError,
                isDataEmpty = requestsTypesFlow.data?.requestsTypeList.isNullOrEmpty(),
                showBack = false,
                withTab = true,
                dismissCallback = {
                    navController.popBackStack()
                },
            ) {
                viewModel.searchRequestsTypes(serviceId = serviceId, searchQuery)
            }
        }
    }
}

@Composable
private fun RequestsTypesShimmerSkeleton() = (List(4) { RequestsType() }).forEach { item ->
    RequestTypeRowComponent(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.inverseSurface,
            shape = RoundedCornerShape(16.dp),
        ),
        enabled = false,
        model = item,
        showSeparator = false,
        isLoading = true

    )
}