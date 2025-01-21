package sa.sauditourism.employee.components

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun <T> ListComponent(
    items: List<T>,
    onItemClick: (T) -> Unit,
    itemContent: @Composable (T) -> Unit,
    @SuppressLint("ModifierParameter") itemModifier: Modifier = Modifier,
    isNestedVertically: Boolean = true,
    listComponentType: ListComponentType = ListComponentType.Vertical,
    listLimitSize: Int = 5,
    verticalPadding: Dp = 40.dp,
    paddingValue: Dp = 24.dp
) {
    val listState = rememberLazyListState()
    val keyFunction: (item: T) -> Any = { item ->
        // get id using reflection
            val reflectionObj = item as Any
            var id: String? = null
            reflectionObj::class.members.forEach {
                if (it.name == "listId" && it.call(reflectionObj) != null) {
                    id = it.call(reflectionObj).toString()
                    return@forEach
                }
            }
            if (id.isNullOrEmpty()) {
                item.hashCode()
            } else {
                id!!
            }

    }

    if (listComponentType == ListComponentType.Vertical) {
        if (isNestedVertically) {
            FlowRow(
                modifier = Modifier.padding(horizontal = paddingValue),
                verticalArrangement = Arrangement.spacedBy(verticalPadding),
                horizontalArrangement = Arrangement.spacedBy(paddingValue),
                maxItemsInEachRow = 2,
            ) {
                items.forEach { item ->
                    Box(
                        modifier = itemModifier
                            .clickableWithoutRipple { onItemClick(item) },
                        contentAlignment = Alignment.Center
                    ) {
                        itemContent(item)
                    }
                }
            }
        } else {
            val mutableItems = mutableStateListOf<T>().apply {
                addAll(items)
            }
            LazyColumn(
                contentPadding = PaddingValues(vertical = paddingValue),
                verticalArrangement = Arrangement.spacedBy(40.dp),
                state = listState
            ) {
                items(mutableItems, key = keyFunction) { item ->
                    itemModifier
                        .clickableWithoutRipple { onItemClick(item) }
                    Box(
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                    ) {
                        itemContent(item)
                    }
                }
            }
        }
    } else if (listComponentType == ListComponentType.Horizontal) {
        val filteredItems = if (items.size > listLimitSize) {
            items.subList(0, listLimitSize)
        } else {
            items
        }
        val mutableItems = mutableStateListOf<T>().apply {
            addAll(filteredItems)
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = paddingValue),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
        ) {
            items(mutableItems, key = keyFunction) { item ->
                Box(
                    modifier = itemModifier
                        .clickableWithoutRipple { onItemClick(item) }
                ) {
                    itemContent(item)
                }
            }
        }
    }
}

enum class ListComponentType {
    Vertical,
    Horizontal,
}

@Preview(showBackground = true, heightDp = 120)
@Composable
fun ListComponentPreview() {
    val itemList =
        listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8")
    var selectedItem: String? by remember { mutableStateOf(null) }

    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column {
            ListComponent(
                items = itemList,
                onItemClick = { selectedItem = it },
                listComponentType = ListComponentType.Horizontal,
                itemContent = { item ->
                    CustomTextComponent(
                        text = item,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier.padding(all = 8.dp),
                    )
                },
            )

            Spacer(modifier = Modifier.height(40.dp))

            ListComponent(
                items = itemList,
                onItemClick = { selectedItem = it },
                listComponentType = ListComponentType.Vertical,
                itemContent = { item ->
                    CustomTextComponent(
                        text = item,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier.padding(all = 8.dp),
                    )
                },
            )
        }
    }
}

@Composable
fun LazyListState.OnBottomReached(
    // tells how many items before we reach the bottom of the list
    // to call onLoadMore function
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    // Buffer must be positive.
    // Or our list will never reach the bottom.
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            // subtract buffer from the total items
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) onLoadMore() }
    }
}

@Composable
fun LazyStaggeredGridState.OnBottomReached(
    // tells how many items before we reach the bottom of the list
    // to call onLoadMore function
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    // Buffer must be positive.
    // Or our list will never reach the bottom.
    require(buffer >= 0) { "buffer cannot be negative, but was $buffer" }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            // subtract buffer from the total items
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - buffer
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { if (it) onLoadMore() }
    }
}
