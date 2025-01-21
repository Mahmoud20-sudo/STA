package sa.sauditourism.employee.modules.account.myRequests.comonents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequest
import sa.sauditourism.employee.ui.theme.AppColors

@Composable
fun MyRequestList(
    modifier: Modifier = Modifier,
    list: List<MyRequest>,
    onItemClick: (MyRequest) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 4.dp, start = 24.dp, end = 24.dp)
            .fillMaxSize(),
    ) {
        LazyColumn(
            Modifier.padding(bottom = 26.dp)
        ) {
            items(list.size) { index ->
                MyRequestItem(list[index]) {
                    onItemClick.invoke(list[index])
                }

                if (index == list.size - 1) {
                    Spacer(modifier = Modifier.padding(bottom = 50.dp))
                }
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun PreviewMyRequestList() {
    MyRequestList(
        list = getDummyData(),
        onItemClick = {}

    )
}


private fun getDummyData() = listOf(
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),
    MyRequest(
        "123",
        "Stationery Request",
        "https://icon.com/icon2.jpg",
        "Rejected",
        "2024-10-26",
        "10:34"
    ),

    )