package sa.sauditourism.employee.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.mirror
import sa.sauditourism.employee.resources.AppFonts

data class ProfileDetailsHeaderComponentModel(
    var imageUri: Uri? = null,
    var isAvatar: Boolean = true
)

@Composable
fun ProfileDetailsHeaderComponent(
    data: ProfileDetailsHeaderComponentModel,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
) {
    val navController = LocalNavController.current

    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                data.imageUri
            )
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Image(
                painter = painterResource(id = R.drawable.edit_profile_orliment),
                modifier = Modifier.align(Alignment.BottomEnd),
                contentDescription = "saudi shape",
                contentScale = ContentScale.None
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 62.dp)
                .fillMaxWidth()
        ) {
            MediaComponent(
                mediaContent = mediaContent,
                modifier = Modifier
                    .size(145.dp)
                    .align(Alignment.Center)
            )
            ButtonComponent(
                text = if (data.isAvatar) LanguageConstants.ADD_IMAGE.localizeString() else LanguageConstants.EDIT.localizeString(),
                clickListener = { onEditClick.invoke() },
                modifier = Modifier
                    .padding(top = 140.dp)
                    .align(Alignment.Center),
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.SMALL,
                startIcon = R.drawable.ic_camera,
            )
        }

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            IconButton(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                    .size(35.dp)
                    .align(Alignment.TopStart)
                    .mirror(),
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left_tail),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        CustomTextComponent(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            text = LanguageConstants.ACCOUNT_TAB.localizeString(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = AppFonts.headerMedium,
            color = Color.White
        )
    }

}


@Composable
@Preview
fun ProfileDetailsHeaderComponentPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {

        ProfileDetailsHeaderComponent(
            ProfileDetailsHeaderComponentModel(
                imageUri = Uri.parse("https://i.ibb.co/41WYzBD/Avatar-3x.png"),
                isAvatar = true,
            )
        ) {

        }
    }
}

