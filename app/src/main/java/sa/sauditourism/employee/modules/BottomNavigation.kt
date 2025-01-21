package sa.sauditourism.employee.modules

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImageState
import com.skydoves.landscapist.rememberDrawablePainter
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.constants.NavConstants
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.resources.AppColors

@Composable
fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = false,
) {
    val screens = BottomNavItem.getTabs()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (currentDestination?.route == null) {
        return
    }
    var selectedItemIndex =
        screens.indexOfFirst { screen -> currentDestination.hierarchy.any { it.route == screen.screenRoute } }
    if (selectedItemIndex < 0) {
        selectedItemIndex =
            screens.indexOfFirst { screen ->
                navController.previousBackStackEntry?.destination?.hierarchy?.any { it.route == screen.screenRoute }
                    ?: false
            }
    }

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val itemWidth by rememberSaveable { mutableFloatStateOf((screenWidthDp.dp / screens.size).value) }
    val initialOffset by rememberSaveable { mutableFloatStateOf((itemWidth - 50.dp.value) / 2) }

    val dividerTranslation = rememberSaveable { mutableFloatStateOf(initialOffset) }

    var targetPosition = itemWidth * selectedItemIndex
    var newPosition by rememberSaveable { mutableFloatStateOf(targetPosition + initialOffset) }

    LaunchedEffect(selectedItemIndex) {
        val animatable = Animatable(dividerTranslation.floatValue)
        animatable.animateTo(newPosition, animationSpec = tween(200)) {
            dividerTranslation.floatValue = value
        }
    }
    targetPosition = itemWidth * selectedItemIndex
    newPosition = (targetPosition + initialOffset)

    AnimatedVisibility(
        showBottomBar,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                screens.forEachIndexed { index, screen ->
                    AddItem(
                        screen = screen,
                        selected = index == selectedItemIndex,
                        tabsCount = screens.size,
                        modifier = Modifier.clickableOnce(onClick = {
                            //todo handle clicks
                            navController.navigate(screen.screenRoute) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(2.dp)
                    .offset(x = dividerTranslation.floatValue.dp),
            ) {
                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun AddItem(
    screen: BottomNavItem,
    selected: Boolean,
    tabsCount: Int,
    modifier: Modifier = Modifier,
) {
    val contentColor =
        if (selected) MaterialTheme.colorScheme.primary else AppColors.ColorsPrimitivesThree
    Box(
        modifier = Modifier
            .height(62.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp / tabsCount)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            val iconDrawable = remember { mutableStateOf<Drawable?>(null) }
            val context = LocalContext.current
            if (iconDrawable.value == null) {

                val previewPlaceHolder = when (screen.screenRoute) {
                    NavConstants.TAB_ID_HOME -> {
                        R.drawable.ic_home
                    }

                    NavConstants.TAB_ID_SERVICES -> {
                        R.drawable.ic_services
                    }

                    NavConstants.TAB_ID_PEOPLE-> {
                        R.drawable.ic_people
                    }

                    NavConstants.TAB_ID_ACCOUNT -> {
                        R.drawable.ic_profile
                    }

                    else -> {
                        null
                    }
                }

                val loading: @Composable (BoxScope.(imageState: CoilImageState.Loading) -> Unit)? =
                    if (previewPlaceHolder == null) {
                        null
                    } else {
                        {
                            Image(
                                painter = painterResource(id = previewPlaceHolder),
                                contentDescription = "placeholder",
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(contentColor),
                                modifier = Modifier.size(25.dp),
                            )
                        }
                    }
                val mediaContent by remember {
                    mutableStateOf(
                        MediaContent(
                            GalleryType.IMAGE,
                            screen.icon
                        )
                    )
                }
                MediaComponent(
                    mediaContent = mediaContent,
                    modifier = Modifier.size(25.dp),
                    imageOptions = ImageOptions(
                        colorFilter = ColorFilter.tint(contentColor),
                        contentScale = ContentScale.Fit,
                    ),
                    showFailure = false,
                    requestListener = {
                        object : ImageRequest.Listener {
                            override fun onError(request: ImageRequest, result: ErrorResult) {
                                if (previewPlaceHolder != null) {
                                    iconDrawable.value =
                                        AppCompatResources.getDrawable(context, previewPlaceHolder)
                                }
                                super.onError(request, result)
                            }

                            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                                iconDrawable.value = result.drawable
                                super.onSuccess(request, result)
                            }
                        }
                    },
                    loading = loading
                )
            } else {
                val painter = rememberDrawablePainter(drawable = iconDrawable.value)
                Icon(
                    painter = painter,
                    contentDescription = "icon",
                    tint = contentColor,
                    modifier = Modifier.size(25.dp),
                )
            }
            CustomTextComponent(
                text = screen.titleLangKey.localizeString(),
                style = AppFonts.menuSemiBold,
                color = contentColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Center

            )
        }
    }
}
