package com.itsmmt305.fbd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb(),
            )
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val btn = findViewById<Button>(R.id.app_open)
        btn.setOnClickListener {
            openApp()
        // DEBUG
        //startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun openApp() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "https://www.facebook.com".toUri()
        ).apply {
            setPackage("com.facebook.katana")
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://www.facebook.com".toUri()
                )
            )
        }
    }
}
