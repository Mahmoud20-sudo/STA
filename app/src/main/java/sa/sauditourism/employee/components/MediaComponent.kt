package sa.sauditourism.employee.components

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.size.Precision
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.coil.CoilImageState
import com.skydoves.landscapist.components.ImageComponent
import com.skydoves.landscapist.components.rememberImageComponent
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.resources.AppColors

/**
 * [source] could be url string or image resource integer */
data class MediaContent(
    val type: GalleryType,
    val source: Any?,
    val contentDescription: String? = null,
    val videoAspectRatio: Float? = null,
    val thumbnail: String? = null,
    var text: String? = null,
    val height: Dp? = null,
    val locationTitle: String? = null,
    val lat: String? = null,
    val lng: String? = null,
)

/**
 * Default value of [imageOptions] is ImageOptions(
 *                         contentScale = ContentScale.Crop,
 *                         alignment = Alignment.Center,
 *                         contentDescription = mediaContent.contentDescription
 *                     )
 * */
@Composable
fun MediaComponent(
    mediaContent: MediaContent,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onImageStateChanged: ((CoilImageState) -> Unit)? = null,
    imageOptions: ImageOptions? = null,
    imageComponent: ImageComponent? = null,
    placeholderSize: Dp? = null,
    placeHolder: Int? = null,
    enabled: Boolean = true,
    isBlur: Boolean = false,
    requestListener: (() -> coil.request.ImageRequest.Listener)? = null,
    player: ExoPlayer? = null,
    loading: @Composable (BoxScope.(imageState: CoilImageState.Loading) -> Unit)? = null,
    showFailure: Boolean = true,
    isSeparatedMedia: Boolean = true,
    precision: Precision = Precision.AUTOMATIC,
    showPlaceHolder: Boolean = true,
    tint: Color? = AppColors.ColorsPrimitivesTwo,
    contentScale: ContentScale? = ContentScale.Crop
) {
//    val sharedPreferencesManager: SharedPreferencesManager =
//        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

    when (mediaContent.type) {
        GalleryType.IMAGE -> {
            val context = LocalContext.current
            CoilImage(
                imageModel = { if (isLoading) null else mediaContent.source },
                imageOptions = imageOptions
                    ?: ImageOptions(
                        contentScale = contentScale?:ContentScale.Crop,
                        alignment = Alignment.Center,
                        contentDescription = mediaContent.contentDescription,
                        colorFilter = if (enabled) null else ColorFilter.tint(tint!!),
                    ),
                imageLoader = {
                    ImageLoader.Builder(context).components { add(SvgDecoder.Factory()) }
                        .precision(precision).build()
                },
                component = imageComponent ?: rememberImageComponent {},
                onImageStateChanged = {
                    onImageStateChanged?.invoke(it)
                },

                requestListener = requestListener,
                failure = if (showFailure) {
                    {
                        Box(
                            modifier = modifier
                                .background(
                                    color = AppColors.Transparent
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            val placeholderImage = when (placeHolder) {
                                null -> R.drawable.ic_thumbnal_image
                                else -> placeHolder
                            }

                            Image(
                                painter = painterResource(id = placeholderImage),
                                contentDescription = mediaContent.contentDescription,
                                contentScale = ContentScale.Inside,
                                modifier = if (placeholderSize == null) {
                                    modifier
                                } else {
                                    Modifier.size(
                                        placeholderSize,
                                    )
                                },
                            )
                        }
                    }
                } else null,
                modifier = modifier.blur(if (isBlur) 2.dp else 0.dp),
                loading = loading ?: {
                    if (showPlaceHolder) {
                        Box(
                            modifier = modifier
                                .background(color = AppColors.Transparent),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(
                                    id = placeHolder ?: R.drawable.ic_thumbnal_image
                                ),
                                contentDescription = mediaContent.contentDescription,
                                contentScale = ContentScale.Inside,
                                modifier = if (placeholderSize == null) {
                                    modifier
                                } else {
                                    Modifier.size(
                                        placeholderSize,
                                    )
                                },
                            )
                        }
                    }
                },
            )
        }

        GalleryType.VIDEO -> {
            VideoComponent(
                videoAspectRatio = mediaContent.videoAspectRatio,
                videoThumbnail = mediaContent.thumbnail,
                modifier = modifier,
                player = player,
                separatedVideo = isSeparatedMedia
            )
        }

//        GalleryType.THREE_SIXTY -> {
//            ThreeSixtyView(source = mediaContent.source as String, modifier = modifier)
//        }

        else -> {
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun VideoComponent(
    videoAspectRatio: Float?,
    videoThumbnail: String?,
    modifier: Modifier = Modifier,
    player: ExoPlayer? = null,
    mediaViewModel: MediaViewModel = hiltViewModel(),
    separatedVideo: Boolean = true
) {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    var showThumbnail by remember { mutableStateOf(true) }

    val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying && showThumbnail) {
                showThumbnail = false
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (!separatedVideo) {
                player?.removeListener(playerListener)
            }
            player?.release()
        }
    }

    var orientation by remember {
        mutableIntStateOf(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }
    if (!separatedVideo) {
        player?.addListener(playerListener)
    }
    if (mediaViewModel.isPlaying) {
        player?.prepare()
        player?.playWhenReady = true
        player?.seekTo(mediaViewModel.currentPosition)
    } else {
        player?.seekTo(mediaViewModel.currentPosition)
    }

    Box(
        modifier = modifier.clickableWithoutRipple {
            if (player == null || separatedVideo)
                return@clickableWithoutRipple

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }

        },
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).also {
                    it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    it.player = player
                    it.useController = separatedVideo
                    it.setFullscreenButtonClickListener { _ ->
                        mediaViewModel.isPlaying = it.player?.isPlaying ?: false
                        mediaViewModel.currentPosition = it.player?.currentPosition ?: 0
                        orientation =
                            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            } else {
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            }
                    }
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                        it.player?.pause()
                        it.onPause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                        CoroutineScope(Dispatchers.Main).launch {
                            it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                            delay(10L)
                            it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    }

                    else -> {
                        // not required
                    }
                }
                when (orientation) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
                        it.setFullscreenButtonClickListener(null)
                        it.setFullscreenButtonClickListener { _ ->
                            mediaViewModel.isPlaying = it.player?.isPlaying ?: false
                            mediaViewModel.currentPosition = it.player?.currentPosition ?: 0
                            orientation =
                                if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                } else {
                                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .then(if (videoAspectRatio.isNotNull()) Modifier.aspectRatio(videoAspectRatio) else Modifier),
        )

        videoThumbnail?.let {
            if (showThumbnail) {
                val mediaContent by remember {
                    mutableStateOf(
                        MediaContent(
                            type = GalleryType.IMAGE,
                            source = it,
                        )
                    )
                }
                MediaComponent(
                    mediaContent = mediaContent,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillHeight,
                        alignment = Alignment.Center,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
                if (separatedVideo) {
                    Box(
                        modifier = Modifier.size(50.dp) then Modifier
                            .background(
                                Color.Black.copy(alpha = 0.5f),
                                shape = CircleShape,
                            )
                            .align(Alignment.Center)
                            .clickableOnce {
                                showThumbnail = false
                                player?.play()
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "icon",
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            }
        }
    }

    if (orientation != LocalConfiguration.current.orientation) {
        LockScreenOrientation(orientation)
    }

//    BackHandler(enabled = orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//        mediaViewModel.isPlaying = player?.isPlaying ?: false
//        mediaViewModel.currentPosition = player?.currentPosition ?: 0
//        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context as Activity
        val originalOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

//@Composable
//fun ThreeSixtyView(source: String, modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier.fillMaxSize(),
//    ) {
//        val context = LocalContext.current
//        val panoramaView = VrPanoramaView(context)
//        LaunchedEffect(Unit) {
//            withContext(Dispatchers.IO) {
//                val bitmap = Glide.with(context)
//                    .asBitmap()
//                    .load(source)
//                    .submit().get()
//                val options = VrPanoramaView.Options()
//                options.inputType = VrPanoramaView.Options.TYPE_MONO
//                panoramaView.loadImageFromBitmap(bitmap, options)
//            }
//        }
//        AndroidView(
//            factory = {
//                panoramaView.also {
//                    it.setOnTouchListener { _, _ ->
//                        // Consume the touch event to disable touch interaction
//                        true
//                    }
//                    it.setInfoButtonEnabled(false)
//                    it.setStereoModeButtonEnabled(false)
//                    it.setFullscreenButtonEnabled(false)
//                }
//            },
//            update = {
//            },
//            modifier = Modifier
//                .fillMaxSize()
//                .align(Alignment.Center),
//        )
//        Box(
//            modifier = Modifier.size(50.dp) then Modifier
//                .background(
//                    Color.Black.copy(alpha = 0.5f),
//                    shape = CircleShape,
//                )
//                .align(Alignment.Center),
//            contentAlignment = Alignment.Center,
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_360),
//                contentDescription = "icon",
//                modifier = Modifier.size(32.dp),
//            )
//        }
//    }
//}