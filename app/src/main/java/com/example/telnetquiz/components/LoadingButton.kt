package com.example.telnetquiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.LitecartesNativeTheme

@Composable
fun LoadingButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = LitecartesColor.Secondary,
    textColor: Color = LitecartesColor.Surface,
    borderColor: Color = LitecartesColor.DarkBrown
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            text = if (isLoading) "" else text,
            borderColor = backgroundColor,
            color = textColor,
            backgroundColor = backgroundColor,
            shadowEnabled = !isLoading,
            shadowHeight = 55.dp,
            shadowColor = borderColor,
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (!isLoading && enabled) onClick() },
            textModifier = Modifier.padding(8.dp)
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingButton() {
    LitecartesNativeTheme {
        LoadingButton(
            text = "MASUK",
            isLoading = false,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingButtonLoading() {
    LitecartesNativeTheme {
        LoadingButton(
            text = "MASUK",
            isLoading = true,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
