package com.itsmmt305.fbd

import android.os.Bundle
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        setupWebView()


        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun setupWebView(){
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mediaPlaybackRequiresUserGesture = false
        }

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        webView.loadUrl("https://fdown.net")
    }


    private fun handleIntent(intent: Intent?) {
        val sharedUrl = if (intent?.action == Intent.ACTION_SEND && "text/plain" == intent.type) {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }

        if (sharedUrl != null) {
            // A URL was shared, so load the main page and inject the URL via JavaScript
            loadUrlAndInject(sharedUrl)
        } else if (webView.url == null) {
            // No URL was shared (normal app launch), so just load the default page
            webView.loadUrl("https://fdown.net")
        }
    }


    private fun loadUrlAndInject(urlToInject: String) {
        webView.webViewClient = object : WebViewClient() {
            // This code runs after the page has finished loading
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // We must be on the correct page before trying to inject JavaScript
                if (url == "https://fdown.net/") {
                    // Create the JavaScript code to find the input and set its value
                    // The replace("'", "\\'") handles URLs containing single quotes
                    val jsCode = """
                        javascript:(function() {
                            var input = document.querySelector('input[name="URLz"]');
                            if (input) {
                                input.value = '${urlToInject.replace("'", "\\'")}';
                            }
                        })()
                    """
                    // Execute the JavaScript
                    view?.evaluateJavascript(jsCode, null)
                }
            }
        }

        // Start by loading the base URL
        webView.loadUrl("https://fdown.net")
    }
}
