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
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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

/**
 * @desc：网页
 * @date：2023/05/17 10:37
 */
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun WebScreen(
    navController: NavHostController = rememberAnimatedNavController(),
    url: String = "https://www.baidu.com/",
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    val state = rememberWebViewState(url)
    val navigator = rememberWebViewNavigator()
    var progress by remember { mutableStateOf(0f) }
    // 用于判断当前是否是预览模式
    val runningInPreview = LocalInspectionMode.current
    SystemUiScaffold {
        Column {
            var titleText by remember { mutableStateOf("") }
            Toolbar(navController = navController, titleText = titleText)
            // 进度条
            AnimatedVisibility(visible = (progress > 0f && progress < 1f)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    backgroundColor = Color.White
                )
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
                    // 关闭自动适应
                    webView.settings.useWideViewPort = false
                    webView.settings.loadWithOverviewMode = false
                    // 启用js
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
                chromeClient = object : AccompanistWebChromeClient() {

                    override fun onReceivedTitle(view: WebView, title: String?) {
                        super.onReceivedTitle(view, title)
                        titleText = title.orEmpty()
                    }

                    override fun onProgressChanged(view: WebView, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        progress = (newProgress / 100f).coerceIn(0f, 1f)
                    }
                },
                factory = { context -> WebViewManager.obtain(context).also { webView = it } }
            )
            Divider(color = GreyLine, thickness = 0.5.dp)
            // 自定义导航栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 返回按钮
                Image(
                    painter = painterResource(R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(10.dp)
                        .clickableNoRipple {
                            webView?.let {
                                if (!WebViewHelper.goBack(it, url)) {
                                    navController.navigateUp()
                                }
                            }
                        },
                )
                // 前进按钮
                Image(
                    painter = painterResource(R.mipmap.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(10.dp)
                        .rotate(180f)
                        .clickableNoRipple {
                            if (navigator.canGoForward) {
                                navigator.navigateForward()
                            }
                        },
                )
                // 刷新按钮
                Image(
                    painter = painterResource(R.mipmap.ic_refresh),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(8.dp)
                        .clickableNoRipple {
                            navigator.reload()
                        },
                )
                // 从外部浏览器打开按钮
                Image(
                    painter = painterResource(R.mipmap.ic_browser),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(8.dp)
                        .clickableNoRipple {
                            runCatching {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.content.getCurrentUrl()))
                                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                context.startActivity(intent)
                            }.onFailure {
                                it.printStackTrace()
                            }
                        },
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