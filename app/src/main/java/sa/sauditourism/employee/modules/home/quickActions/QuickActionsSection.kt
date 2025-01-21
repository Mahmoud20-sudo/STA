package sa.sauditourism.employee.modules.home.quickActions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.home.model.Action
import sa.sauditourism.employee.modules.home.model.Announcement
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.login.Routes.BOOK_MEETING_ROOM
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier


@Composable
fun QuickActionsSection(
    navController: NavController = rememberNavController(),
    isLoading: Boolean = false,
    actions: List<Action>? = listOf()
) {
    var isCoilLoading by remember { mutableStateOf(true) }

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

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(269.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        actions?.take(2)?.onEach { action ->
            if (action.orderIndex == 1)
                Column(
                    modifier = Modifier
                        .background(
                            if (action.commingSoon) AppColors.ColorsPrimitivesFour else
                                AppColors.OnPrimaryDark, shape = RoundedCornerShape(16.dp)
                        )
                        .shimmerModifier(isLoading, shape = RoundedCornerShape(16.dp))
                        .zIndex(100f)
                        .weight(1f)
                        .fillMaxHeight()
                        .clickableOnce {
                            if (action.commingSoon) return@clickableOnce
                            if (action.deepLink == BOOK_MEETING_ROOM) {
                                navController.navigate(BOOK_MEETING_ROOM)
                            } else {
                                //todo "ESLAM" check with Ios how they handled it
                            }
//                            navController.navigate(
//                                "${Routes.REQUESTS_FORM}/${action.serviceId}/${action.requestType}/${
//                                    action.title.replace(
//                                        "/",
//                                        "$"
//                                    )
//                                }"
//                            )
                        },
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (action.commingSoon)
                            MediaComponent(
                                mediaContent = mediaContent,
                                modifier = Modifier
                                    .offset(y = (-2).dp, x = (2).dp)
                                    .zIndex(2f)
                                    .size(61.dp)
                                    .align(Alignment.TopEnd),
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Fit,
                                    alignment = Alignment.TopCenter
                                ),
                            )

                        CustomTextComponent(
                            modifier = Modifier
                                .shimmerModifier(isLoading)
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                                .alpha(if (action.commingSoon) 0.4f else 1f),
                            text = action.title,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            color = if (action.commingSoon) AppColors.ColorTextSubdued else AppColors.WHITE,
                            style = AppFonts.subtitle1Bold,
                            lineHeight = 23.76.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    MediaComponent(
                        mediaContent = MediaContent(
                            GalleryType.IMAGE,
                            action.picToGram
                        ),
                        modifier = Modifier
                            .padding(bottom = 13.dp, start = 15.dp, end = 15.dp)
                            .fillMaxWidth()
                            .shimmerModifier(isLoading)
                            .height(146.dp)
                            .alpha(if (action.commingSoon) 0.5f else 1f),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.FillBounds,
                            alignment = Alignment.TopCenter
                        ),
                        loading = {
                            MediaComponent(
                                mediaContent = MediaContent(
                                    GalleryType.IMAGE,
                                    source = R.drawable.ic_thumbnal_image
                                ),
                                modifier = Modifier
                                    .shimmerModifier(isCoilLoading || isLoading)
                                    .fillMaxSize(),
                            )
                        }
                    )
                }
            else Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (i in 1 until actions.size) {
                    var lines by remember { mutableStateOf(0) }
                    val innerAction = actions[i]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(if (i == 1) 1.5f else 1f)
                            .background(
                                if (innerAction.commingSoon) AppColors.ColorsPrimitivesFour else if (i == 1) AppColors.OnSecondaryDark else AppColors.OnAccentBlue,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .shimmerModifier(isLoading, shape = RoundedCornerShape(16.dp))
                            .zIndex(100f)
                            .clickableOnce {
                                if (innerAction.commingSoon || i == 1) return@clickableOnce
                                navController.navigate(
                                    "${Routes.REQUESTS_FORM}/${innerAction.serviceCategoryId}/${innerAction.requestType}/${
                                        innerAction.title.replace(
                                            "/",
                                            "$"
                                        )
                                    }"
                                )
                            }
                    ) {

                        if (innerAction.commingSoon)
                            MediaComponent(
                                mediaContent = mediaContent,
                                modifier = Modifier
                                    .offset(y = (-2).dp, x = (2).dp)
                                    .zIndex(2f)
                                    .size(61.dp)
                                    .align(Alignment.TopEnd),
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Fit,
                                    alignment = Alignment.TopCenter
                                ),
                            )

                        CustomTextComponent(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .shimmerModifier(isLoading)
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = if (lines == 1) 16.dp else 4.dp
                                )
                                .alpha(if (innerAction.commingSoon) 0.4f else 1f),
                            text = innerAction.title,
                            textAlign = TextAlign.Center,
                            onTextLayout = { res -> lines = res.lineCount },
                            maxLines = 2,
                            color = if (innerAction.commingSoon) AppColors.ColorTextSubdued else AppColors.WHITE,
                            style = AppFonts.subtitle1Bold,
                            lineHeight = 23.76.sp
                        )

                        MediaComponent(
                            mediaContent = MediaContent(
                                GalleryType.IMAGE,
                                innerAction.picToGram
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 40.dp)
                                .shimmerModifier(isLoading)
                                .alpha(if (innerAction.commingSoon) 0.5f else 1f),
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.BottomCenter
                            ),
                            loading = {
                                MediaComponent(
                                    mediaContent = MediaContent(
                                        GalleryType.IMAGE,
                                        source = R.drawable.ic_thumbnal_image
                                    ),
                                    modifier = Modifier
                                        .shimmerModifier(isCoilLoading || isLoading)
                                        .fillMaxSize(),
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewQuickActionsSection() {
    QuickActionsSection()
}