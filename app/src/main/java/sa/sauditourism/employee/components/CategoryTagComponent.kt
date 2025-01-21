package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun CategoryTagComponent(
    text: String,
    modifier: Modifier = Modifier,
    withStartIcon: Boolean = false,
    readOnly: Boolean = false,
    withEndIcon: Boolean = false,
    itemState: ItemState = ItemState.Active,
    itemSize: ItemSize = ItemSize.Small,
    isLoading: Boolean = false,
    icon: Any? = null,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .height(
                if (itemSize == ItemSize.Small) {
                    32.dp
                } else {
                    40.dp
                },
            )
            .background(
                color = when (itemState) {
                    ItemState.Inactive -> {
                        MaterialTheme.colorScheme.inverseSurface
                    }

                    ItemState.Active -> {
                        AppColors.OnPrimaryColor
                    }

                    ItemState.DropDown -> {
                        if(readOnly) AppColors.ColorsPrimitivesFive else Color.White
                    }

                    else -> {
                        Color.Transparent
                    }
                },
                shape = RoundedCornerShape(30.dp),
            )
            .border(
                width = if (isLoading) 0.dp else 1.dp,
                color = if (isLoading) Color.Transparent else when (itemState) {
                    ItemState.Disabled -> {
                        AppColors.ColorsPrimitivesThree
                    }

                    ItemState.Inactive -> {
                        AppColors.ColorsPrimitivesThree
                    }

                    ItemState.DropDown -> {
                        AppColors.ColorTextSubdued
                    }

                    else -> {
                        Color.Transparent
                    }
                },
                shape = RoundedCornerShape(30.dp),
            )
            .clickableWithoutRipple(onClick = {
                if (itemState == ItemState.Inactive) {
                    return@clickableWithoutRipple
                }
                onClick()
            })
            .then(Modifier.shimmerModifier(isLoading, shape = RoundedCornerShape(30.dp))),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    horizontal = 24.dp,
                ),
        ) {
            if (withStartIcon && icon != null) {
                CategoryTagImage(itemState, icon)
                Spacer(modifier = Modifier.width(3.dp))
            }
            CustomTextComponent(
                text = text,
                color = when (itemState) {
                    ItemState.Inactive -> {
                        MaterialTheme.colorScheme.inverseOnSurface
                    }

                    ItemState.Active -> {
                        Color.White
                    }

                    ItemState.DropDown -> {
                        Color.Black
                    }

                    else -> {
                        MaterialTheme.colorScheme.inverseSurface
                    }
                },
                style = AppFonts.tagsSemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentWidth()
            )
            if (withEndIcon && icon != null) {
                Spacer(modifier = Modifier.width(3.dp))
                CategoryTagImage(itemState, icon)
            }
        }
    }
}

@Composable
private fun CategoryTagImage(
    itemState: ItemState,
    icon: Any,
) {
    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                type = GalleryType.IMAGE,
                source = icon
            )
        )
    }
    MediaComponent(
        mediaContent = mediaContent,
        imageOptions = ImageOptions(
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            colorFilter = when (itemState) {
                ItemState.Inactive -> {
//                ColorFilter.tint(MaterialTheme.colorScheme.inverseOnSurface)
                    null
                }

                ItemState.Active -> {
                    ColorFilter.tint(Color.White)
                }

                ItemState.DropDown -> {
                    ColorFilter.tint(AppColors.OnPrimaryColor)
                }

                else -> {
                    ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
                }
            }
        ),
        showPlaceHolder = false,
        modifier = Modifier.size(16.dp),
    )
}

enum class ItemState {
    Active,
    Inactive,
    Disabled,
    DropDown
}

enum class ItemSize {
    Small,
    Large,
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun CategoryTagComponentPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        val birthdayIcon =
            "https://drive.google.com/uc?export=view&id=1pAFSt1eo2tA5kmhcT5FcVAvVlb4ah-f6"

        Column {
            Row {
                CategoryTagComponent(text = "Text")
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(text = "Text", withStartIcon = true)
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(text = "Text", withEndIcon = true)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                CategoryTagComponent(text = "Text", itemState = ItemState.Inactive)
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withStartIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Inactive,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withEndIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Inactive,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                CategoryTagComponent(text = "Text", itemState = ItemState.Disabled)
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withStartIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Disabled,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withEndIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Disabled,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                CategoryTagComponent(text = "Text", itemSize = ItemSize.Large)
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withStartIcon = true,
                    icon = birthdayIcon,
                    itemSize = ItemSize.Large,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withEndIcon = true,
                    itemSize = ItemSize.Large,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                CategoryTagComponent(
                    text = "Text",
                    itemState = ItemState.Inactive,
                    itemSize = ItemSize.Large,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withStartIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Inactive,
                    itemSize = ItemSize.Large,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withEndIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Inactive,
                    itemSize = ItemSize.Large,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                CategoryTagComponent(
                    text = "Text",
                    itemState = ItemState.Disabled,
                    itemSize = ItemSize.Large,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withStartIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Disabled,
                    itemSize = ItemSize.Large,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CategoryTagComponent(
                    text = "Text",
                    withEndIcon = true,
                    icon = birthdayIcon,
                    itemState = ItemState.Disabled,
                    itemSize = ItemSize.Large,
                )
            }
        }
    }
}
