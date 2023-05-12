package com.fphoenixcorneae.happyjoke.mvi.ui.page.web

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.clickableNoRipple
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.Toolbar
import com.fphoenixcorneae.happyjoke.tool.WebViewHelper
import com.fphoenixcorneae.happyjoke.tool.WebViewManager
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.web.*
import com.google.accompanist.web.WebView as AccompanistWebView

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun WebViewScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    url: String = "https://www.baidu.com/",
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    val state = rememberWebViewState(url)
    val navigator = rememberWebViewNavigator()
    var progress by remember { mutableStateOf(0f) }
    //用于判断当前是否是预览模式
    val runningInPreview = LocalInspectionMode.current
    SystemUiScaffold {
        Column {
            Toolbar(navController = navController)


            val chromeClient = object : AccompanistWebChromeClient() {

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progress = (newProgress / 100f).coerceIn(0f, 1f)
                }
            }
            AccompanistWebView(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                captureBackPresses = false,
                navigator = navigator,
                onCreated = { webView ->
                    if (runningInPreview) {
                        // webView.settings breaks the preview 有这东西才能让你预览起来
                        return@AccompanistWebView
                    }
                    webView.settings.javaScriptEnabled = true
                    webView.addJavascriptInterface(OnJsInterface(), "android")
                    WebViewHelper.setDownloadListener(webView)
                    WebViewHelper.setOnLongClickListener(webView)
                },
                onDispose = { webView ->
                    // 释放
                    webView.removeJavascriptInterface("android")
                    WebViewManager.recycle(webView)
                },
                client = object : AccompanistWebViewClient() {
                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): WebResourceResponse? {
                        if (view != null && request != null) {
                            when {
                                WebViewHelper.isAssetsResource(request) -> {
                                    return WebViewHelper.assetsResourceRequest(view.context, request)
                                }
                                WebViewHelper.isCacheResource(request) -> {
                                    return WebViewHelper.cacheResourceRequest(view.context, request)
                                }
                            }
                        }
                        return super.shouldInterceptRequest(view, request)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): Boolean {
                        if (view != null && request != null && request.url != null) {
                            if ("http" != request.url.scheme && "https" != request.url.scheme) {
                                try {
                                    view.context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                return true
                            }
                        }
                        return false
                    }
                },
                chromeClient = chromeClient,
                factory = { context -> WebViewManager.obtain(context).also { webView = it } }
            )
            //进度条
            AnimatedVisibility(visible = (progress > 0f && progress < 1f)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSecondary,
                    backgroundColor = Color.White
                )
            }
            Divider(
                color = GreyLine, thickness = 0.5.dp
            )
            //自定义导航栏
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(48.dp)
            ) {
                //返回按钮
                Image(
                    painter = painterResource(com.fphoenixcorneae.happyjoke.R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(17.dp)
                        .clickableNoRipple {
                            webView?.let {
                                if (!WebViewHelper.goBack(it, url)) {
                                    navController.navigateUp()
                                }
                            }
                        }
                )
                //前进按钮
                Image(
                    painter = painterResource(R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(17.dp)
                        .clickable {
                            if (navigator.canGoForward) {
                                navigator.navigateForward()
                            }
                        }
                )
                //刷新按钮
                Image(
                    painter = painterResource(R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(17.dp)
                        .clickable {
                            navigator.reload()
                        }
                )
                //从外部浏览器打开按钮
                Image(
                    painter = painterResource(R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(17.dp)
                        .clickable {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(state.content.getCurrentUrl())
                                )
                                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                )
            }
        }
    }
}

/**
 * @desc：JavascriptInterface
 * @date：2023/05/12 17:45
 */
class OnJsInterface {
    @JavascriptInterface
    fun anyMethod(message: String) {

    }
}