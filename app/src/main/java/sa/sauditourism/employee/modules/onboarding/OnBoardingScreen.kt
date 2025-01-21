package sa.sauditourism.employee.modules.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import kotlinx.coroutines.launch
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.onboarding.model.OnBoarding
import sa.sauditourism.employee.modules.onboarding.model.OnBoardingModel
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun OnboardingScreen(onBoarding: OnBoarding?, onOnboardingFinished: (String?) -> Unit) {

    val onBoardingModel = onBoarding?.list

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { onBoardingModel?.size ?: 0 })

    val buttonText =
        if (pagerState.currentPage == 0) LanguageConstants.SKIP.localizeString() else LanguageConstants.BACK.localizeString()

    val configuration = LocalConfiguration.current
    val imageHeight = (configuration.screenHeightDp * 0.6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ChangeStatusBarIconsColors()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
        ) { page ->
            val image by remember {
                mutableStateOf(
                    MediaContent(
                        GalleryType.IMAGE,
                        onBoardingModel?.get(page)?.image
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                MediaComponent(
                    mediaContent = image,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight.dp),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    ),
                )

                Spacer(modifier = Modifier.height(24.dp))


                CustomTextComponent(
                    text = onBoardingModel?.get(page)?.title,
                    color = AppColors.ColorTextSubdued,
                    style = AppFonts.heading2Medium,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(20.dp))


                CustomTextComponent(
                    text = onBoardingModel?.get(page)?.description,
                    color = AppColors.ColorTextSubdued,
                    style = AppFonts.body2Regular,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        PageIndicator(
            numberOfPages = pagerState.pageCount,
            selectedPage = pagerState.currentPage,
            defaultRadius = 12.dp,
            selectedLength = 24.dp,
            space = 8.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start,
        ) {

            ButtonComponent(
                text = buttonText,
                clickListener = {
                    scope.launch {
                        if (pagerState.currentPage == 0) {
                            pagerState.animateScrollToPage(pagerState.pageCount - 1)
                            return@launch
                        }

                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                modifier = Modifier.wrapContentSize(),
                buttonType = ButtonType.WHITE_BACKGROUND,
                buttonSize = ButtonSize.MEDIUM
            )

            Spacer(modifier = Modifier.weight(1f))

            ButtonComponent(
                if (pagerState.currentPage == pagerState.pageCount - 1) LanguageConstants.DONE_BUTTON_TITLE.localizeString() else LanguageConstants.NEXT.localizeString(),
                clickListener = {
                    if (pagerState.currentPage == pagerState.pageCount - 1) {
                        onOnboardingFinished.invoke(onBoarding?.version)
                        return@ButtonComponent
                    }

                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .height(40.dp),
                buttonType = ButtonType.PRIMARY,
                buttonSize = ButtonSize.MEDIUM
            )

        }
        Spacer(modifier = Modifier.height(60.dp))

    }
}


@Composable
fun PageIndicator(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    selectedColor: Color = AppColors.OnPrimaryColor,
    defaultColor: Color = AppColors.OnPrimaryColor.copy(alpha = 0.4f),
    defaultRadius: Dp = 30.dp,
    selectedLength: Dp = 30.dp,
    defaultHeight: Dp = 4.dp,
    space: Dp = 30.dp,
    animationDurationInMillis: Int = 300,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
        modifier = modifier,
    ) {
        for (i in 0 until numberOfPages) {
            val isSelected = i == selectedPage
            PageIndicatorView(
                isSelected = isSelected,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                defaultRadius = defaultRadius,
                selectedLength = selectedLength,
                defaultHeight = defaultHeight,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
fun PageIndicatorView(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    defaultHeight: Dp,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
) {

    val color: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedColor
        } else {
            defaultColor
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        ), label = ""
    )
    val width: Dp by animateDpAsState(
        targetValue = if (isSelected) {
            selectedLength
        } else {
            defaultRadius
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        ), label = ""
    )

    Canvas(
        modifier = modifier
            .size(
                width = width,
                height = defaultHeight,
            ),
    ) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(
                width = width.toPx(),
                height = defaultHeight.toPx(),
            ),
            cornerRadius = CornerRadius(
                x = defaultRadius.toPx(),
                y = defaultRadius.toPx(),
            ),
        )
    }
}