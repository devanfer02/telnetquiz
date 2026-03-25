package com.example.telnetquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.telnetquiz.components.Navigation
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_LitecartesNative)
        enableEdgeToEdge()
        setContent {
            LitecartesNativeTheme {
                Navigation()
            }
        }
    }
}
