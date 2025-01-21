package sa.sauditourism.employee.components

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.extensions.mirror
import sa.sauditourism.employee.extensions.openExternalBrowser
import sa.sauditourism.employee.extensions.openMail
import sa.sauditourism.employee.extensions.openPhone
import sa.sauditourism.employee.extensions.openWhatsapp
import sa.sauditourism.employee.ui.theme.AppFonts

@SuppressLint("SetJavaScriptEnabled", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewComponent(
    url: String,
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    shouldRedirectToDeeplink: Boolean = true,
    onCloseButtonClicked: () -> Unit = {}
) {
    val webViewRef = remember { WebViewRef() }
    val showLoadingProgress = remember { mutableFloatStateOf(0.2f) }
    val animatedProgress = animateFloatAsState(
        targetValue = showLoadingProgress.floatValue,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    val navHostController = LocalNavController.current

    Scaffold(
        topBar = {
            // Header with title and back/close button
            CenterAlignedTopAppBar(
                title = {
                    CustomTextComponent(
                        title,
                        style = AppFonts.heading4Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(modifier = Modifier
                            .padding(end = 16.dp)
                            .mirror(),
                            onClick = {
                                if (webViewRef.webView?.canGoBack() == true) {
                                    webViewRef.webView?.goBack()
                                    return@IconButton
                                }
                                onCloseButtonClicked()
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left_tail),
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        IconButton(
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = { onCloseButtonClicked() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.allowContentAccess = true
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.defaultTextEncodingName = "utf-8"
                            settings.allowContentAccess = true
                            settings.useWideViewPort = true
                            settings.loadsImagesAutomatically = true
                            settings.loadWithOverviewMode = true

                            webViewClient =
                                CustomWebViewClient(
                                    onCloseButtonClicked,
                                    navHostController,
                                    shouldRedirectToDeeplink
                                )
                            webChromeClient = CustomChromeClient(showLoadingProgress)

                            loadUrl(url)
                            webViewRef.webView = this
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (showLoadingProgress.floatValue < 1) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.Transparent,
            )
        }
    }



    DisposableEffect(Unit) {
        onDispose {
            webViewRef.webView?.destroy()
        }
    }
}

//fun addTokenAndAppToUrl(url: String): String {
//    val modifiedUrl = if (URLUtil.isValidUrl(url) && url.contains("visitsaudi.com")) {
//        val token =
//            SharedPreferencesManager(EmployeeApplication.instance.applicationContext).token
//        if (token == null) {
//            if (url.contains("www.visitsaudi.com")) {
//                if (url.contains("?")) {
//                    val splitUrl = url.split("?", limit = 2)
//                    "${splitUrl[0]}.app?${splitUrl[1]}"
//                } else {
//                    "$url.app"
//                }
//            } else {
//                url
//            }
//        } else {
//            if (url.contains("?")) {
//                val splitUrl = url.split("?", limit = 2)
//                if (splitUrl[1].contains("token=")) {
//                    if (url.contains("www.visitsaudi.com")) {
//                        "${splitUrl[0]}.app?${splitUrl[1]}"
//                    } else {
//                        url
//                    }
//                } else {
//                    if (url.contains("www.visitsaudi.com")) {
//                        "${splitUrl[0]}.app?${splitUrl[1]}&token=${token.accessToken.encryptToken()}"
//                    } else {
//                        "$url&token=${token.accessToken.encryptToken()}"
//                    }
//                }
//            } else {
//                if (url.contains("www.visitsaudi.com")) {
//                    "$url?token=${token.accessToken.encryptToken()}.app"
//                } else {
//                    "$url?token=${token.accessToken.encryptToken()}"
//                }
//            }
//        }
//    } else {
//        url
//    }
//
//    return modifiedUrl
//}

class WebViewRef {
    var webView: WebView? = null
}

class CustomChromeClient(private val showLoadingProgress: MutableFloatState) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (newProgress.toFloat() > 10f) {
            showLoadingProgress.floatValue = newProgress.toFloat() / 100f
        }
    }
}

class CustomWebViewClient(
    private val onCloseButtonClicked: () -> Unit = {},
    private val navHostController: NavHostController,
    private val shouldRedirectToDeeplink: Boolean = true
) :
    WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        webResourceRequester: WebResourceRequest?
    ): Boolean {
        if (webResourceRequester == null || webResourceRequester.url == null) {
            return false
        }
        var url = webResourceRequester.url.toString()
//        url = addTokenAndAppToUrl(url)

        if (url.startsWith("tel:")) {
            val phoneNumber = url.substringAfter("tel:")
            view?.context?.openPhone(phoneNumber)
            return true
        } else if (url.startsWith("mailto:") && url.contains("?subject")) {
            view?.context?.openMail(url.split("?subject")[0].substringAfter("mailto:"))
            return true
        } else if (url.startsWith("whatsapp:") && url.contains("phone=")) {
            view?.context?.openWhatsapp(url.split("phone=")[1])
            return true
        } else if (url.contains("m.facebook.com") || url.contains("twitter.com") ||
            url.contains("instagram.com")
        ) {
            EmployeeApplication.instance.applicationContext.openExternalBrowser(url)
            return true
        } /*else {
            if (shouldRedirectToDeeplink) {
                val deepLinksManager =
                    DeepLinksManager()
                deepLinksManager.initWithIntent(Intent().setDataAndNormalize(Uri.parse(url)))
                val notificationModel = deepLinksManager.getDeepLinkNotificationModel()
                val sharedPreferencesManager =
                    SharedPreferencesManager(EmployeeApplication.instance.applicationContext)
                notificationModel?.let {
                    sharedPreferencesManager.notificationModel =
                        it
                }
                view?.context?.let {
                    deepLinksManager.handleLink(
                        it,
                        navHostController,
                        sharedPreferencesManager = sharedPreferencesManager,
                        loadWebViewUrl = {
                            view.loadUrl(url)
                        },
                        webViewLinkHandledAction = {
                            onCloseButtonClicked()
                        })
                }
                return true
            }
        }*/
        return false
    }
}
