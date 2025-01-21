package sa.sauditourism.employee.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableOnce

data class AvatarData(
    val id: Int,
    val imageUrl: String,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AvatarComponent(
    avatarList: List<AvatarData>,
    selectedAvatar: AvatarData?,
    modifier: Modifier = Modifier,
    maxItemInEachRow: Int = 2,
    onAvatarSelected: (AvatarData) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxWidth(),
        maxItemsInEachRow = maxItemInEachRow,
    ) {
        var selectedId by remember(selectedAvatar?.id) { mutableStateOf(selectedAvatar?.id) }
        avatarList.forEach { avatarData ->
            val mediaContent by remember {
                mutableStateOf(
                    MediaContent(
                        GalleryType.IMAGE,
                        avatarData.imageUrl
                    )
                )
            }
            Box(
                modifier = Modifier
                    .width(85.dp)
                    .height(85.dp),
                contentAlignment = Alignment.Center
            ) {
                MediaComponent(
                    mediaContent = mediaContent,
                    modifier = Modifier
                        .clickableOnce {
                            selectedId = avatarData.id
                            onAvatarSelected(avatarData)
                        }
                        .fillMaxSize()
                        .border(
                            3.dp,
                            if (selectedId == avatarData.id) MaterialTheme.colorScheme.secondary else Color.Transparent
                        )
                )
                if (selectedId == avatarData.id) {
                    Image(
                        modifier = Modifier
                            .padding(7.dp)
                            .size(16.dp)
                            .align(Alignment.TopEnd),
                        painter = painterResource(id = R.drawable.ic_check_circle_white),
                        contentDescription = "check box",
                        alignment = Alignment.TopEnd,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AvatarComponentPreview() {

    val avatars = listOf(
        AvatarData(1, "https://i.ibb.co/41WYzBD/Avatar-3x.png"),
        AvatarData(2, "https://i.ibb.co/41WYzBD/Avatar-3x.png"),
        AvatarData(3, "https://i.ibb.co/41WYzBD/Avatar-3x.png"),
        AvatarData(4, "https://i.ibb.co/41WYzBD/Avatar-3x.png"),
    )

    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {

        AvatarComponent(
            avatarList = avatars,
            selectedAvatar = avatars[0],
            onAvatarSelected = {

            }
        )

    }
}