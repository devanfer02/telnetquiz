package com.example.telnetquiz.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.telnetquiz.components.Navbar
import com.example.telnetquiz.components.ProfileTopBar

@Composable
fun AppLayout(
    navController: NavController,
    topBar: @Composable () -> Unit = { ProfileTopBar() },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar =  {
            Navbar(navController = navController)
        },
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        content(paddingValues)
    }
}