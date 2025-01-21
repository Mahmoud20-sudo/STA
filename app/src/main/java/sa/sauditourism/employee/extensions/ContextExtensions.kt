package sa.sauditourism.employee.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Browser
import android.provider.Settings
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.TypedValue
import android.view.Window
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.activity.WebViewActivity
import sa.sauditourism.employee.constants.CommonConstants
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.run {
        getNetworkCapabilities(activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
}

fun Context.isGooglePlayServiceAvailable(): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
    return resultCode == ConnectionResult.SUCCESS
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
        key,
        T::class.java,
    )

    else -> {
        @Suppress("DEPRECATION")
        getParcelableExtra(key) as? T
    }
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableArrayList(key)
    }


fun Context.openStoreForApp(appPackageName: String) {
    if (isHmsAvailable()) {
        openHuaweiGalleryStoreForApp(appPackageName)
    } else {
        openGooglePlayStoreForApp(appPackageName)
    }
}

private fun Context.openGooglePlayStoreForApp(appPackageName: String) {
    try {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        if (this !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.setPackage("com.android.vending")
        startActivity(intent)
    } catch (e: android.content.ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"),
        )
        if (this !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}

private fun Context.openHuaweiGalleryStoreForApp(appPackageName: String) {
    try {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        if (this !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.setPackage("com.huawei.appmarket")
        startActivity(intent)
    } catch (e: android.content.ActivityNotFoundException) {
        val visitSaudiAppId = "101699859"
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://appgallery.cloud.huawei.com/marketshare/app/C$visitSaudiAppId"),
        )
        if (this !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}

@SuppressLint("DiscouragedApi")
fun Context.getStringResourceByName(resName: String, args: Any? = null): String? {
    return try {
        val resId = resources.getIdentifier(resName, "string", packageName)
        if (args == null) {
            getString(resId)
        } else {
            getString(resId, args)
        }
    } catch (e: Exception) {
        null
    }
}

@SuppressLint("DiscouragedApi")
fun Context.getResourceByName(resName: String, resourceType: String = "drawable"): Int? {
    return try {
        return resources.getIdentifier(resName, resourceType, packageName)
    } catch (e: Exception) {
        null
    }
}

fun Context.openWhatsapp(number: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        var url = number
        if (!url.startsWith("https://wa.me/")) {
            url = "https://wa.me/$number"
        }
        intent.data = Uri.parse(url)
        if (this !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Open mail
fun Context.openMail(email: String?) {
    email?.let {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        startActivity(
            Intent.createChooser(intent, "").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}

// Open phone intent
fun Context.openPhone(phoneNumber: String?) {
    phoneNumber?.let {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$it"))
            if (this !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(it))
                if (this !is Activity) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun Context.openBrowser(
    url: String,
    external:Boolean = false,
    headerTitle: String? = null,
    shouldRedirectToDeeplink: Boolean = true
) {
    if (url.isEmpty()) {
        return
    }
   // var webpage = Uri.parse(url)

//    if (!url.startsWith("http://") && !url.startsWith("https://")) {
//        webpage = Uri.parse("https://$url")
//    }
    if (!external) {
        val intent = Intent(
            this,
            WebViewActivity::class.java,
        )
        if (!headerTitle.isNullOrEmpty()) {
            intent.putExtra(CommonConstants.OPEN_URL_HEADER_INTENT_EXTRA, headerTitle)
        }
        intent.putExtra(CommonConstants.OPEN_URL_WITH_REDIRECTION, shouldRedirectToDeeplink)
        intent.putExtra(CommonConstants.OPEN_URL_INTENT_EXTRA, url)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        if (this is Activity) {
            if (Build.VERSION.SDK_INT < 34) {
                overridePendingTransition(
                    R.anim.slide_in_from_bottom,
                    R.anim.hold
                )
            }
        }
    } else {
        openExternalBrowser(url)
    }
}

fun Context.openExternalBrowser(url: String?, headerParam: Pair<String, String>? = null) {
    if (url.isNullOrEmpty()) {
        return
    }
    var webpage = Uri.parse(url)

    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        webpage = Uri.parse("http://$url")
    }
    val intent = Intent(Intent.ACTION_VIEW, webpage)

    val activities: List<ResolveInfo> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(0),
            )
        } else {
            packageManager.queryIntentActivities(intent, 0)
        }
    val filteredActivities = java.util.ArrayList<ResolveInfo>()

    // Filter out your own app
    for (activity in activities) {
        if (activity.activityInfo.packageName != packageName) {
            filteredActivities.add(activity)
        }
    }
    if (filteredActivities.size == 1) {
        val selectedActivity: ResolveInfo = filteredActivities[0]
        intent.setClassName(
            selectedActivity.activityInfo.packageName,
            selectedActivity.activityInfo.name,
        )
        EmployeeApplication.instance.currentActivity!!.startActivity(intent)
        return
    }
    if (headerParam != null) {
        val bundle = Bundle()
        bundle.putString(headerParam.first, headerParam.second)
        intent.putExtra(Browser.EXTRA_HEADERS, bundle)
    }

    val dialog = Dialog(EmployeeApplication.instance.currentActivity!!)
    // Set Dialog theme
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
//    dialog.setContentView(R.layout.browser_pop_up)
//    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    dialog.window?.attributes?.windowAnimations = R.style.AlertDialogTheme
//
//    // init layout
//    val popupTitle = dialog.findViewById(R.id.popupTitle) as TextView
//    val popupDescription = dialog.findViewById(R.id.popupDescription) as TextView
//    val popupRecyclerView = dialog.findViewById(R.id.browser_apps_rv) as RecyclerView
//    popupRecyclerView.vertical()
//
//    // Set Title Text
//    popupTitle.text = (LanguageConstants.CHOOSE_AN_APP.localizeString())
//    // Set Description
//    popupDescription.text = "${LanguageConstants.CHOOSE_AN_APP_DESC.localizeString()}\n$url"

//    val adapter = BrowsersAdapter(filteredActivities)
//    popupRecyclerView.adapter = adapter
//    adapter.browserListClick = { info, pos ->
//        val selectedActivity: ResolveInfo = filteredActivities[pos]
//        intent.setClassName(
//            selectedActivity.activityInfo.packageName,
//            selectedActivity.activityInfo.name,
//        )
//        EmployeeApplication.instance.currentActivity!!.startActivity(intent)
//        dialog.dismiss()
//    }

    dialog.show()
}


//fun RecyclerView.grid2SpanHorizontal() {
//    setHasFixedSize(true)
//    itemAnimator = null
//    layoutManager = GridLayoutManager(
//        context, // Context
//        2,// Number
//        GridLayoutManager.HORIZONTAL,
//        false
//    )
//}

//fun RecyclerView.vertical(snappy: Boolean = false) {
//    setHasFixedSize(true)
//    itemAnimator = null
//    isVerticalScrollBarEnabled = false
//    isHorizontalScrollBarEnabled = false
//    overScrollMode = View.OVER_SCROLL_NEVER
//    layoutManager = LinearLayoutManager(
//        this.context,
//        RecyclerView.VERTICAL,
//        false,
//    )
//    if (snappy) {
//        this.onFlingListener = null
//        val helper: SnapHelper = LinearSnapHelper()
//        helper.attachToRecyclerView(this)
//    }
//}

// for grid
//class SpacingItemDecorator(private val padding: Int) : RecyclerView.ItemDecoration() {
//    override fun getItemOffsets(
//        outRect: Rect,
//        view: View,
//        parent: RecyclerView,
//        state: RecyclerView.State
//    ) {
//        super.getItemOffsets(outRect, view, parent, state)
//        outRect.top = padding
////        outRect.bottom = padding
//        outRect.left = padding
//        outRect.right = padding
//    }
//}

//fun Context.showMaterialDialog(
//    title: String? = "",
//    message: String? = "",
//    positiveButton: String? = "",
//    negativeButton: String? = "",
//    positiveButtonCallBack: () -> Unit,
//) {
//    MaterialAlertDialogBuilder(this)
//        .setTitle(title)
//        .setMessage(message)
//        .setPositiveButton(positiveButton) { dialog, _ ->
//            dialog.dismiss()
//            positiveButtonCallBack.invoke()
//        }
//        .setNegativeButton(negativeButton) { dialog, _ ->
//            dialog.cancel()
//        }
//        .show()
//}

fun Context.openAppSettings() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    } catch (exception: Exception) {
        //exception.print()
    }
}

fun Context.openLocationSettings() {
    try {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    } catch (exception: Exception) {
       // exception.print()
    }
}

fun Context.spToPx(sp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        resources.displayMetrics,
    ).toInt()
}

fun Context.isHmsAvailable(): Boolean {
    return com.huawei.hms.api.ConnectionResult.SUCCESS ==
        com.huawei.hms.api.HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(this)
}

fun Context.openDirectionApp(latitude: String, longitude: String) {
    try {
        if (isHmsAvailable()) {
            val contentUrl =
                Uri.parse("petalmaps://navigation?daddr=$latitude,$longitude&type=drive")
            val intent = Intent(Intent.ACTION_VIEW, contentUrl)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        } else {

            // Display a label at the location of Google's Sydney office
            val gmmIntentUri =
                Uri.parse("google.navigation:q=$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (mapIntent.resolveActivity(packageManager) != null) {
                try {
                    startActivity(mapIntent)
                } catch (ex: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude")
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        Log.e("Exception", "Map OPEN")
    }
}

fun Context.shareText(text: String) {
    try {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "Send To"))
    } catch (ignored: Exception) {
    }
}

//fun ImageView.setSTAImage(link: String?) {
//    Glide.with(this).setDefaultRequestOptions(customRequestOption())
//        .load(link?.let { GlobalFunctions.getSTAImage(it) }).into(this)
//}

fun TextView.toList(strings: java.util.ArrayList<String>) {
    this.text = strings.toString().removePrefix("[").removeSuffix("]")
}


//val Context.navigationBarHeight: Int
//    @RequiresApi(Build.VERSION_CODES.R)
//    get() {
//        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//
//        return if (Build.VERSION.SDK_INT >= 30) {
//            windowManager
//                .currentWindowMetrics
//                .windowInsets
//                .getInsets(WindowInsets.Type.navigationBars())
//                .bottom
//
//        } else {
//            val currentDisplay = try {
//                display
//            } catch (e: NoSuchMethodError) {
//                windowManager.defaultDisplay
//            }
//
//            val appUsableSize = Point()
//            val realScreenSize = Point()
//            currentDisplay?.apply {
//                getSize(appUsableSize)
//                getRealSize(realScreenSize)
//            }
//
//            // navigation bar on the side
//            if (appUsableSize.x < realScreenSize.x) {
//                return realScreenSize.x - appUsableSize.x
//            }
//
//            // navigation bar at the bottom
//            return if (appUsableSize.y < realScreenSize.y) {
//                realScreenSize.y - appUsableSize.y
//            } else 0
//        }
//    }

fun TextView.setHtml(html: String?) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
    } else {
        HtmlCompat.fromHtml(html.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    movementMethod = LinkMovementMethod.getInstance()
}

inline fun <reified T : Parcelable> Intent.getParcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListE(key: String): ArrayList<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelableArrayList(key, T::class.java)
    else
        @Suppress("DEPRECATION") getParcelableArrayList(key)

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

fun Context.isTablet(): Boolean {
    return (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

fun Context.prepareImageUri(): Uri? {
    val sixDigitString = Random.nextInt(100000, 1000000).toString()
    val file = File.createTempFile(
        "JPEG_${sixDigitString}_",
        ".jpg",
        externalCacheDir
    )
    return FileProvider.getUriForFile(
        this,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    )
}