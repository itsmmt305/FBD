package com.itsmmt305.fbd

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val btn = findViewById<Button>(R.id.app_open)
        btn.setOnClickListener {
            openApp()
        }
    }

    private fun openApp() {
        val pm : PackageManager = packageManager

        try {
            // Try to open FBA
            pm.getPackageInfo("com.facebook.katana", 0)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = "fb://feed".toUri()
                setPackage("com.facebook.katana")
            }
            startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {

            // FBA not installed -> open in browser
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://www.facebook.com".toUri()
            )
            startActivity(intent)
        }
    }
}
