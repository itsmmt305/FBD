package com.itsmmt305.fbd

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
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

    @SuppressLint("SetJavaScriptEnabled")
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


        fun urlHandler(urlToInject: String) {
            webView.webViewClient = object : WebViewClient() {
                // This code runs after the page has finished loading
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // We must be on the correct page before trying to inject JavaScript
                    if (url == "https://fdown.net/") {
                        // Create the JavaScript code to find the input and set its value
                        // The replace("'", "\\'") handles URLs containing single quotes
                        val jsCode = """
                        (function() {
                            var input = document.querySelector('input[name="URLz"]');
                            var btn = document.querySelector('input[name="URLz"]')
                            ?.closest('form')
                            ?.querySelector('button[type="submit"]');

                            if (input) {
                                input.value = '${urlToInject.replace("'", "\\'")}';
                                setTimeout(function () { // why why WHY
                                    if (btn) {
                                        btn.click();
                                    }
                                }, 150); // understanding this delay
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

        fun isFacebookUrl(url: String): Boolean {
            val u = url.lowercase()
            return u.contains("facebook.com") ||
                    u.contains("m.facebook.com") ||
                    u.contains("fb.watch")
        }

        if (sharedUrl != null) {
            if (isFacebookUrl(sharedUrl)) {
                urlHandler(sharedUrl)
                Toast.makeText(this, "Pasted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid Link", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
