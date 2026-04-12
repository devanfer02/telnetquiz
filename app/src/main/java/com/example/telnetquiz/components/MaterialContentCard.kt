package com.example.telnetquiz.components

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.telnetquiz.components.TtsPulsingDots
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun MaterialContentCard(
    title: String,
    content: String,
    imageLink: String?,
    onSpeakClick: () -> Unit,
    isTtsLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(LitecartesColor.Primary)
            .padding(
                vertical = 10.dp,
                horizontal = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (isTtsLoading) {
                TtsPulsingDots(modifier = Modifier.size(32.dp))
            } else {
                IconButton(
                    onClick = onSpeakClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Baca materi",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Text(
            text = title,
            fontFamily = nunitosFontFamily,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        if (!imageLink.isNullOrEmpty()) {
            AsyncImage(
                model = imageLink,
                contentDescription = title,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
        }
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = false
                    settings.setSupportZoom(false)
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
            },
            update = { webView ->
                val html = """
                    <html>
                    <head><meta name="viewport" content="width=device-width, initial-scale=1.0"></head>
                    <body style="color:white;font-family:sans-serif;margin:0;padding:0;">
                    ${sanitizeHtml(content)}
                    </body>
                    </html>
                """.trimIndent()
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMaterialContentCard() {
    MaterialContentCard(
        title = "Materi: Luas Lingkaran",
        content = "<p>Rumus luas lingkaran adalah <b>πr²</b></p>",
        imageLink = null,
        onSpeakClick = {}
    )
}

fun sanitizeHtml(input: String): String {
    return input
        .replace(Regex("^\\s*<h[12][^>]*>[\\s\\S]*?</h[12]>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<script[^>]*>[\\s\\S]*?</script>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<script[^>]*/>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<iframe[^>]*>[\\s\\S]*?</iframe>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<iframe[^>]*/>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<form[^>]*>[\\s\\S]*?</form>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<object[^>]*>[\\s\\S]*?</object>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<embed[^>]*>[\\s\\S]*?</embed>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<embed[^>]*/>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<link[^>]*>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("<style[^>]*>[\\s\\S]*?</style>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("on\\w+\\s*=", RegexOption.IGNORE_CASE), "data-removed=")
}
