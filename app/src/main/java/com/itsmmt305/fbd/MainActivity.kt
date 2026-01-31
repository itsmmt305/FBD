package com.itsmmt305.fbd

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.webkit.*
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb(),
                )
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        setupWebView()
        setupDownloadListener()
        handleIntent(intent)

        //DEBUG
        //webView.loadUrl("https://fdown.net")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mediaPlaybackRequiresUserGesture = false
        }

        webView.webChromeClient = WebChromeClient()
    }

    private fun handleIntent(intent: Intent?) {
        val url =
            if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
                intent.getStringExtra(Intent.EXTRA_TEXT)
            } else null

        if (url == null) return

        if (!isFacebookUrl(url)) {
            Toast.makeText(this, "Invalid Link", Toast.LENGTH_SHORT).show()
            return
        }

        // HARD RESET WebView every time
        webView.stopLoading()
        webView.clearHistory()
        webView.clearCache(true)

        // ONE-TIME WebViewClient FOR THIS SHARE ONLY
        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, currentUrl: String?) {
                super.onPageFinished(view, currentUrl)
                if (view == null || currentUrl == null) return

                // Always hide navbar
                hideNavbar(view)

                // Homepage -> paste & submit
                if (currentUrl == "https://fdown.net/") {
                    pasteAndSubmit(view, url)
                }

                // Download page -> click hdlink
                if (currentUrl == "https://fdown.net/download.php") {
                    clickHdLink(view)
                }
            }
        }

        webView.loadUrl("https://fdown.net")
        Toast.makeText(this, "Pasted", Toast.LENGTH_SHORT).show()
    }

    private fun setupDownloadListener() {
        webView.setDownloadListener { url, userAgent, _, mimeType, _ ->

            val request = DownloadManager.Request(url.toUri())
                .setTitle("FBD")
                .setDescription("Downloading…")
                .setMimeType(mimeType)
                .addRequestHeader("User-Agent", userAgent)
                .setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "fbd_${System.currentTimeMillis()}.mp4"
                )

            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)

            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()

            // exit app on download start
            webView.postDelayed({
                finish()
            }, 800)
        }
    }

    private fun isFacebookUrl(url: String): Boolean {
        val u = url.lowercase()
        return u.contains("facebook.com") ||
                u.contains("m.facebook.com") ||
                u.contains("fb.watch")
    }

    // ---------------- JS ----------------

    private fun hideNavbar(view: WebView) {
        val js = """
            (function () {
                var style = document.createElement('style');
                style.innerHTML = `
                    .navbar.navbar-default.navbar-static-top {
                        display: none !important;
                    }
                `;
                document.head.appendChild(style);
            })();
        """.trimIndent()

        view.evaluateJavascript(js, null)
    }

    private fun pasteAndSubmit(view: WebView, url: String) {
        val safeUrl = url.replace("'", "\\'")

        val js = """
            (function () {
                var input = document.querySelector('input[name="URLz"]');
                if (!input) return;

                var btn = input.closest('form')?.querySelector('button[type="submit"]');

                input.value = '$safeUrl';
                input.dispatchEvent(new Event('input', { bubbles: true }));
                input.dispatchEvent(new Event('change', { bubbles: true }));
                input.focus();

                setTimeout(function () {
                    if (btn) btn.click();
                }, 150);
            })();
        """.trimIndent()

        view.evaluateJavascript(js, null)
    }

    private fun clickHdLink(view: WebView) {
        val js = """
            (function () {
                var btn = document.getElementById('hdlink');
                if (btn) btn.click();
            })();
        """.trimIndent()

        view.evaluateJavascript(js, null)
    }
}


//learner's notes