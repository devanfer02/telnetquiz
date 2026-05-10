package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

private fun isConnectionError(message: String): Boolean {
    val lower = message.lowercase()
    return listOf("terhubung", "koneksi", "jaringan", "internet", "server")
        .any { it in lower }
}

@Composable
fun ErrorRetryBox(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String? = null,
) {
    val connectionError = isConnectionError(message)
    val resolvedTitle = title ?: if (connectionError) {
        "Tidak dapat terhubung ke server"
    } else {
        "Yah, terjadi kesalahan"
    }
    val resolvedHint = hint ?: if (connectionError) {
        "Pastikan kamu terhubung ke internet, lalu tekan tombol di bawah untuk mencoba lagi."
    } else {
        null
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.chap2),
                contentDescription = "Error mascot",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = resolvedTitle,
                color = LitecartesColor.Secondary,
                textAlign = TextAlign.Center,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = LitecartesColor.Secondary,
                textAlign = TextAlign.Center,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            if (resolvedHint != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = resolvedHint,
                    color = LitecartesColor.Secondary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    text = "Coba Lagi",
                    color = LitecartesColor.Surface,
                    backgroundColor = LitecartesColor.Secondary,
                    borderColor = LitecartesColor.DarkBrown,
                    onClick = onRetry
                )
            }
        }
    }
}
