package com.example.telnetquiz.features.auth.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun ForgotPasswordDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Lupa Kata Sandi?",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.Bold,
                color = LitecartesColor.Secondary
            )
        },
        text = {
            Text(
                text = "Kontak tim admin apabila lupa password",
                fontFamily = nunitosFontFamily,
                color = LitecartesColor.DarkBrown
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = LitecartesColor.Secondary
                )
            ) {
                Text(
                    text = "OK",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = LitecartesColor.Surface
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewForgotPasswordDialog() {
    LitecartesNativeTheme {
        ForgotPasswordDialog(onDismiss = {})
    }
}
