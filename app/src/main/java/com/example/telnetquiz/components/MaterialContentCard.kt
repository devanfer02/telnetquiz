package com.example.telnetquiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun MaterialContentCard(
    title: String,
    content: String,
    imageLink: String?,
    onSpeakClick: () -> Unit,
    isTtsLoading: Boolean = false,
    isTtsPlaying: Boolean = false,
    onStopClick: () -> Unit = onSpeakClick,
    level: Int = 0,
    modifier: Modifier = Modifier,
    audioButtonModifier: Modifier = Modifier
) {
    val blocks = remember(content) { parseStudyBlocks(content) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        IntroHero(
            title = title,
            level = level,
            imageLink = imageLink,
            isTtsLoading = isTtsLoading,
            isTtsPlaying = isTtsPlaying,
            onSpeakClick = onSpeakClick,
            onStopClick = onStopClick,
            audioButtonModifier = audioButtonModifier
        )

        Spacer(modifier = Modifier.height(8.dp))

        var featureIndex = 0
        blocks.forEachIndexed { i, block ->
            val isLast = i == blocks.lastIndex
            when (block) {
                is StudyBlock.Body -> {
                    BodyParagraphCard(block.annotated)
                }
                is StudyBlock.Heading -> {
                    SectionHeading(block.text)
                }
                is StudyBlock.FeatureList -> {
                    block.items.forEachIndexed { j, item ->
                        FeatureCard(index = featureIndex, item = item)
                        if (j != block.items.lastIndex) {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        featureIndex++
                    }
                }
                is StudyBlock.Conclusion -> {
                    ConclusionCard(block.annotated)
                }
            }
            if (!isLast) {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        if (blocks.isEmpty()) {
            BodyParagraphCard(parseInline(content))
        }
    }
}

@Composable
private fun IntroHero(
    title: String,
    level: Int,
    imageLink: String?,
    isTtsLoading: Boolean,
    isTtsPlaying: Boolean,
    onSpeakClick: () -> Unit,
    onStopClick: () -> Unit,
    audioButtonModifier: Modifier
) {
    val hasImage = !imageLink.isNullOrEmpty()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(LitecartesColor.Primary)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MenuBook,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (level > 0) "MATERI SINGKAT • LEVEL $level" else "MATERI SINGKAT",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                if (isTtsLoading) {
                    TtsPulsingDots(modifier = audioButtonModifier.size(28.dp))
                } else {
                    IconButton(
                        onClick = if (isTtsPlaying) onStopClick else onSpeakClick,
                        modifier = audioButtonModifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isTtsPlaying) Icons.Filled.Stop else Icons.Filled.VolumeUp,
                            contentDescription = if (isTtsPlaying) "Hentikan suara" else "Baca materi",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
                if (hasImage) {
                    AsyncImage(
                        model = imageLink,
                        contentDescription = title,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            if (hasImage) {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = imageLink,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun BodyParagraphCard(annotated: AnnotatedString) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(12.dp))
            .background(LitecartesColor.DarkerSurface)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(LitecartesColor.Primary)
        )
        Text(
            text = annotated,
            color = LitecartesColor.Secondary,
            fontSize = 13.sp,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ChatBubbleOutline,
            contentDescription = null,
            tint = LitecartesColor.Primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text.uppercase(),
            color = LitecartesColor.Primary,
            fontSize = 13.sp,
            fontFamily = nunitosFontFamily,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}

private data class FeatureVisual(val icon: ImageVector, val tint: Color)

private val FeatureRotation = listOf(
    FeatureVisual(Icons.Filled.ChatBubbleOutline, LitecartesColor.Primary),
    FeatureVisual(Icons.Filled.AutoAwesome, LitecartesColor.GreenCactus),
    FeatureVisual(Icons.Filled.AltRoute, LitecartesColor.ScoreOrange),
    FeatureVisual(Icons.Filled.Bolt, LitecartesColor.ScoreYellow),
    FeatureVisual(Icons.Filled.Shield, LitecartesColor.ScoreBlue),
    FeatureVisual(Icons.Filled.CheckCircle, LitecartesColor.ScoreGreen)
)

@Composable
private fun FeatureCard(index: Int, item: FeatureItem) {
    val visual = FeatureRotation[index % FeatureRotation.size]
    CardWithShadow(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = LitecartesColor.DarkerSurface,
        elevation = 3.dp,
        cornerRadius = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(visual.tint.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = visual.icon,
                    contentDescription = null,
                    tint = visual.tint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                if (item.title.isNotBlank()) {
                    Text(
                        text = item.title,
                        color = LitecartesColor.Secondary,
                        fontSize = 13.sp,
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Text(
                    text = item.description,
                    color = LitecartesColor.Secondary.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ConclusionCard(annotated: AnnotatedString) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(12.dp))
            .background(LitecartesColor.Surface)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(LitecartesColor.GreenCactus)
        )
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = LitecartesColor.GreenCactus,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "KESIMPULAN",
                    color = LitecartesColor.GreenCactus,
                    fontSize = 11.sp,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = annotated,
                color = LitecartesColor.Secondary,
                fontSize = 13.sp,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ---- Parser ----

internal sealed class StudyBlock {
    data class Body(val annotated: AnnotatedString) : StudyBlock()
    data class Heading(val text: String) : StudyBlock()
    data class FeatureList(val items: List<FeatureItem>) : StudyBlock()
    data class Conclusion(val annotated: AnnotatedString) : StudyBlock()
}

internal data class FeatureItem(val title: String, val description: String)

private val BlockRegex = Regex(
    "<(p|h[1-6]|ul|ol)[^>]*>([\\s\\S]*?)</\\1>",
    RegexOption.IGNORE_CASE
)
private val ListItemRegex = Regex(
    "<li[^>]*>([\\s\\S]*?)</li>",
    RegexOption.IGNORE_CASE
)
private val StrongRegex = Regex(
    "<strong[^>]*>([\\s\\S]*?)</strong>",
    RegexOption.IGNORE_CASE
)
private val InlineRegex = Regex(
    "<(strong|b|em|i)[^>]*>([\\s\\S]*?)</\\1>",
    RegexOption.IGNORE_CASE
)
private val TagStripper = Regex("<[^>]+>")

internal fun parseStudyBlocks(html: String): List<StudyBlock> {
    val cleaned = html.replace("\n", " ").trim()
    if (cleaned.isEmpty()) return emptyList()

    val matches = BlockRegex.findAll(cleaned).toList()
    val hasList = matches.any { it.groupValues[1].lowercase() in listOf("ul", "ol") }

    val blocks = mutableListOf<StudyBlock>()
    matches.forEachIndexed { i, m ->
        val tag = m.groupValues[1].lowercase()
        val inner = m.groupValues[2]
        when {
            tag == "p" -> {
                val isLast = i == matches.size - 1
                if (isLast && hasList) {
                    blocks.add(StudyBlock.Conclusion(parseInline(inner)))
                } else {
                    blocks.add(StudyBlock.Body(parseInline(inner)))
                }
            }
            tag.matches(Regex("h[1-6]")) ->
                blocks.add(StudyBlock.Heading(stripTags(inner)))
            tag == "ul" || tag == "ol" -> {
                val items = ListItemRegex.findAll(inner)
                    .map { parseListItem(it.groupValues[1]) }
                    .toList()
                blocks.add(StudyBlock.FeatureList(items))
            }
        }
    }
    return blocks
}

private fun parseListItem(html: String): FeatureItem {
    val strong = StrongRegex.find(html)
    val title = strong?.groupValues?.get(1)?.let { stripTags(it).trimEnd(':').trim() } ?: ""
    val rest = if (strong != null) {
        html.replaceRange(strong.range, "").trim().removePrefix(":").trim()
    } else html
    return FeatureItem(title = title, description = stripTags(rest))
}

internal fun parseInline(html: String): AnnotatedString = buildAnnotatedString {
    var cursor = 0
    val text = html.replace("\n", " ")
    for (m in InlineRegex.findAll(text)) {
        if (m.range.first > cursor) {
            append(stripTagsPreservingSpaces(text.substring(cursor, m.range.first)))
        }
        val tag = m.groupValues[1].lowercase()
        val style = when (tag) {
            "strong", "b" -> SpanStyle(
                fontWeight = FontWeight.ExtraBold,
                color = LitecartesColor.Secondary
            )
            "em", "i" -> SpanStyle(fontStyle = FontStyle.Italic)
            else -> SpanStyle()
        }
        withStyle(style) { append(stripTagsPreservingSpaces(m.groupValues[2]).trim()) }
        cursor = m.range.last + 1
    }
    if (cursor < text.length) {
        append(stripTagsPreservingSpaces(text.substring(cursor)))
    }
}

private fun stripTags(html: String): String =
    html.replace(TagStripper, "").replace("&nbsp;", " ").trim()

private fun stripTagsPreservingSpaces(html: String): String =
    html.replace(TagStripper, "").replace("&nbsp;", " ").replace(Regex("\\s+"), " ")

@Preview(showBackground = true)
@Composable
fun PreviewMaterialContentCard() {
    val sample = """
        <p>TCP/IP berfungsi sebagai <strong>standar protokol komunikasi data</strong> yang memungkinkan perangkat-perangkat berbeda untuk saling berkomunikasi dalam suatu jaringan.</p>
        <h3>Peran TCP/IP</h3>
        <ul>
          <li><strong>Komunikasi Data:</strong> Memungkinkan pengiriman dan penerimaan data antar perangkat yang terhubung ke jaringan.</li>
          <li><strong>Interoperabilitas:</strong> Menjadi standar universal sehingga perangkat dari berbagai vendor dapat saling berkomunikasi.</li>
          <li><strong>Routing:</strong> Mengarahkan paket data melalui jalur terbaik menuju tujuan.</li>
        </ul>
        <p>Tanpa TCP/IP, perangkat-perangkat di internet <strong>tidak akan dapat saling bertukar informasi</strong> secara terstruktur dan andal.</p>
    """.trimIndent()
    MaterialContentCard(
        title = "Fungsi TCP/IP dalam Jaringan",
        content = sample,
        imageLink = null,
        level = 1,
        onSpeakClick = {}
    )
}
