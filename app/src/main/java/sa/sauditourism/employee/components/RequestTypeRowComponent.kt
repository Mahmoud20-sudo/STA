package sa.sauditourism.employee.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun RequestTypeRowComponent(
    model: RequestsType,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showSeparator: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
) {
    val mediaContent by remember(model.image) {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                model.image
            )
        )
    }
    Column(modifier = Modifier
        .background(Color.White)
        .clickableWithoutRipple(enabled = enabled) {
            onClick.invoke()
        }) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            MediaComponent(
                mediaContent = mediaContent,
                modifier = Modifier
                    .size(24.dp)
                    .shimmerModifier(isLoading),
                enabled = enabled
            )
            Spacer(Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextComponent(
                    text = model.title,
                    style = AppFonts.body1Medium,
                    color = if (enabled) AppColors.ColorsPrimitivesOne else sa.sauditourism.employee.resources.AppColors.ColorsPrimitivesTwo,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 150.dp)
                        .shimmerModifier(isLoading)
                )
                if (!model.subTitle.isNullOrEmpty())
                    CustomTextComponent(
                        text = model.subTitle,
                        style = AppFonts.body1Regular,
                        color = if (enabled) AppColors.ColorsPrimitivesOne else sa.sauditourism.employee.resources.AppColors.ColorsPrimitivesTwo,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 100.dp)
                            .shimmerModifier(isLoading)
                    )
            }
            if (enabled)
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .shimmerModifier(isLoading)
                )
        }

        if (showSeparator) {
            HorizontalDivider(thickness = 2.dp, color = AppColors.ColorsPrimitivesFive)
        }
    }
}

@Composable
fun RequestsListComponent(
    list: List<RequestsType>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    offsetValue: Dp = 24.dp,
    onClick: (requestModel: RequestsType) -> Unit = { },
) {

    val lang = SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext).preferredLocale

    Box {
        val mediaContent by remember {
            mutableStateOf(
                MediaContent(
                    GalleryType.IMAGE,
                    if (lang == "ar") R.drawable.comsing_soon_ar else R.drawable.coming_soon_en
                )
            )
        }

        Column(
            modifier = modifier
                .shadow(elevation = 0.dp, shape = RoundedCornerShape(16.dp), clip = true)
                .background(
                    color = AppColors.ColorsPrimitivesFive,
                    shape = RoundedCornerShape(16.dp),
                )
                .fillMaxWidth(),
        ) {
            if (isLoading) {
                RequestsTypesShimmerSkeleton()
                return@Column
            }

            list.forEachIndexed { index, requestModel ->
                RequestTypeRowComponent(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.inverseSurface,
                        shape = RoundedCornerShape(16.dp),
                    ),
                    enabled = enabled,
                    model = requestModel, showSeparator = list.lastIndex != index
                ) {
                    onClick.invoke(requestModel)
                }
            }
        }

        if (!enabled)
                MediaComponent(
                    mediaContent = mediaContent,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = offsetValue)
                        .padding(20.dp)
                        .height(if(list.size == 1) 55.dp else 61.dp)
                        .shimmerModifier(isLoading),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.TopCenter
                    ),
                )

    }
}

@Composable
private fun RequestsTypesShimmerSkeleton() {
    val list = (List(4) { RequestsType() })
    list.forEachIndexed { index, item ->
        RequestTypeRowComponent(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.inverseSurface,
                shape = RoundedCornerShape(16.dp),
            ),
            enabled = true,
            isLoading = true,
            model = item, showSeparator = 3 != index
        )
    }
}