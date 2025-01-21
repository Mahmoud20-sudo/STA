package sa.sauditourism.employee.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.random.Random


@MainThread
fun <T> LiveData<T?>.observeNonNull(owner: LifecycleOwner, callback: ((value: T) -> Unit)) {
    observe(owner) {
        if (it.isNotNull()) {
            callback(it)
        }
    }
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

fun Int.toPriceFormat(): String {
    return String.format("%,d SAR", this)
}

fun Boolean?.safe(): Boolean {
    return this ?: false
}

fun Float?.safe(): Float {
    return this ?: 0f
}

fun Int?.safe(): Int {
    return this ?: 0
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

//fun RecyclerView.horizontal(snappy: Boolean = false) {
//    setHasFixedSize(true)
//    isVerticalScrollBarEnabled = false
//    isHorizontalScrollBarEnabled = false
//    itemAnimator = null
//    overScrollMode = View.OVER_SCROLL_NEVER
//    layoutManager = LinearLayoutManager(
//        VisitSaudiApplication.instance.applicationContext,
//        RecyclerView.HORIZONTAL,
//        false
//    )
//    if (snappy) {
//        this.onFlingListener = null
//        val helper: SnapHelper = LinearSnapHelper()
//        helper.attachToRecyclerView(this)
//    }
//}


fun View.setMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val params = (layoutParams as? ViewGroup.MarginLayoutParams)
    params?.setMargins(
        left ?: params.leftMargin,
        top ?: params.topMargin,
        right ?: params.rightMargin,
        bottom ?: params.bottomMargin
    )
    layoutParams = params
}

//fun RecyclerView.grid2Span(spanCount: Int = 2) {
//    setHasFixedSize(true)
//    itemAnimator = null
//    layoutManager = GridLayoutManager(
//        context, // Context
//        spanCount, // Number
//    )
//}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun Uri.getFilename(context: Context): String? {
    val cursor = context.contentResolver?.query(this, null, null, null, null)
    var filename: String? = null

    cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)?.let { nameIndex ->
        cursor.moveToFirst()

        filename = cursor.getString(nameIndex)
        cursor.close()
    }

    return filename
}

fun Picture.createBitmapFromPicture(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.width,
        this.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawPicture(this)
    return bitmap
}

fun Bitmap.getImageUri(inContext: Context): Uri {
    val bytes = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, bytes)

    val path =
        MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            this,
            "sta_${System.currentTimeMillis()}",
            null
        )
    return Uri.parse(path)
}

@Composable
fun <T> T.useDebounce(
    delayMillis: Long = 500L,
    // 1. couroutine scope
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onChange: (T) -> Unit
): T {
    // 2. updating state
    val state by rememberUpdatedState(this)

    // 3. launching the side-effect handler
    DisposableEffect(state) {
        val job = coroutineScope.launch {
            delay(delayMillis)
            onChange(state)
        }
        onDispose {
            job.cancel()
        }
    }
    return state
}

fun Uri.getFileFromUri(context: Context): File? {
    if (!isUriValid(context)) {
        return null
    }
    return try {
        when (this.scheme) {
            "file" -> File(this.path!!)
            "content" -> {
                val cursor = context.contentResolver.query(this, null, null, null, null)
                cursor?.use {
                    val nameIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    it.moveToFirst()
                    val name = it.getString(nameIndex)
                    val file = File(context.cacheDir, name)

                    context.contentResolver.openInputStream(this)?.use { inputStream ->
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    file
                }
            }

            else -> null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun Uri.isUriValid(context: Context): Boolean {
    return try {
        context.contentResolver.openInputStream(this)?.close()
        true
    } catch (e: Exception) {
        false
    }
}

fun Activity.toggleFullScreen(isFullScreen: Boolean? = false) {
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    insetsController.apply {
        if (isFullScreen == true) {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        else{
            show(WindowInsetsCompat.Type.statusBars())
            show(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }
}