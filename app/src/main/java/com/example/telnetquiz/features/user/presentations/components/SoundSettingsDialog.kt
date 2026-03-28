package com.example.telnetquiz.features.user.presentations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.telnetquiz.data.local.AudioSettings
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun SoundSettingsDialog(
    audioSettings: AudioSettings,
    onDismiss: () -> Unit,
    onMutedChange: (Boolean) -> Unit,
    onGlobalVolumeChange: (Float) -> Unit,
    onSfxVolumeChange: (Float) -> Unit,
    onBgMusicVolumeChange: (Float) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = LitecartesColor.Surface
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Pengaturan Suara",
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = LitecartesColor.Secondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Suara",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = LitecartesColor.Secondary
                    )
                    Switch(
                        checked = !audioSettings.isMuted,
                        onCheckedChange = { onMutedChange(!it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LitecartesColor.Surface,
                            checkedTrackColor = LitecartesColor.Primary,
                            uncheckedThumbColor = LitecartesColor.Surface,
                            uncheckedTrackColor = LitecartesColor.Secondary.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                VolumeSliderRow(
                    label = "Volume Utama",
                    icon = Icons.Default.VolumeUp,
                    value = audioSettings.globalVolume,
                    enabled = !audioSettings.isMuted,
                    onValueChangeFinished = onGlobalVolumeChange
                )

                Spacer(modifier = Modifier.height(8.dp))

                VolumeSliderRow(
                    label = "Efek Suara",
                    icon = Icons.Default.VolumeUp,
                    value = audioSettings.sfxVolume,
                    enabled = !audioSettings.isMuted,
                    onValueChangeFinished = onSfxVolumeChange
                )

                Spacer(modifier = Modifier.height(8.dp))

                VolumeSliderRow(
                    label = "Musik Latar",
                    icon = Icons.Default.MusicNote,
                    value = audioSettings.bgMusicVolume,
                    enabled = !audioSettings.isMuted,
                    onValueChangeFinished = onBgMusicVolumeChange
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Tutup",
                            fontFamily = nunitosFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = LitecartesColor.Primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VolumeSliderRow(
    label: String,
    icon: ImageVector,
    value: Float,
    enabled: Boolean,
    onValueChangeFinished: (Float) -> Unit
) {
    var localValue by remember(value) { mutableFloatStateOf(value) }
    val alpha = if (enabled) 1f else 0.4f

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = LitecartesColor.Secondary.copy(alpha = alpha),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = label,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = LitecartesColor.Secondary.copy(alpha = alpha)
                )
            }
            Text(
                text = "${(localValue * 100).toInt()}%",
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = LitecartesColor.Primary.copy(alpha = alpha)
            )
        }
        Slider(
            value = localValue,
            onValueChange = { localValue = it },
            onValueChangeFinished = { onValueChangeFinished(localValue) },
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = LitecartesColor.Primary,
                activeTrackColor = LitecartesColor.Primary,
                inactiveTrackColor = LitecartesColor.Secondary.copy(alpha = 0.2f),
                disabledThumbColor = LitecartesColor.Secondary.copy(alpha = 0.3f),
                disabledActiveTrackColor = LitecartesColor.Secondary.copy(alpha = 0.2f),
                disabledInactiveTrackColor = LitecartesColor.Secondary.copy(alpha = 0.1f)
            )
        )
    }
}
