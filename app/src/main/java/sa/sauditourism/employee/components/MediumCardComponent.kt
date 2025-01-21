package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import ir.kaaveh.sdpcompose.sdp
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.resources.AppColors.ColorsPrimitivesThree
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun MediumCardComponent(
     servicesModel: ServicesModel,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val lang =
        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext).preferredLocale

    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                if (lang == "ar") R.drawable.comsing_soon_ar else R.drawable.coming_soon_en
            )
        )
    }

    val mediaContentUrl by remember {
        mutableStateOf(
            MediaContent(
                type = GalleryType.IMAGE,
                source = servicesModel.image
            )
        )
    }

    var isCoilLoading by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(all = 10.dp)
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .zIndex(1f)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .shimmerModifier(isLoading)
            .padding(horizontal = 16.dp)
            .size(150.dp)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            if (servicesModel.comingSoon)
                MediaComponent(
                    mediaContent = mediaContent,
                    modifier = Modifier
                        .offset(y = (-7).dp, x = (20).dp)
                        .zIndex(2f)
                        .align(Alignment.TopEnd)
                        .height(61.dp),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillBounds,
                        alignment = Alignment.TopCenter
                    ),
                )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                MediaComponent(
                    mediaContent = mediaContentUrl,
                    isLoading = isLoading || isCoilLoading,
                    modifier = Modifier
                        .size(40.dp)
                        .shimmerModifier(isLoading = isLoading),
                    onImageStateChanged = {
                        if (it is CoilImageState.Success || it is CoilImageState.Failure) {
                            isCoilLoading = false
                        }
                    },
                    imageOptions = ImageOptions(
                        colorFilter = if (servicesModel.comingSoon) ColorFilter.tint(
                            AppColors.ColorsPrimitivesTwo
                        ) else null
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextComponent(
                    text = servicesModel.title,
                    style = AppFonts.body1Medium.copy(color = if (servicesModel.comingSoon) AppColors.ColorsPrimitivesTwo else AppColors.Black),
                    lineHeight = 24.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    isLoading = isLoading,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediumCardComponentPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MediumCardComponent(
                servicesModel = ServicesModel(
                    id = "1",
                    title = "Test",
                    image = "https://s3.eu-central-1.amazonaws.com/elciqlo-production-public/users/324888/pictures/DDD-706B556A0E33.jpg",
                    requestsTypes = emptyList(),
                    comingSoon = true
                )
            )
        }
    }
}

