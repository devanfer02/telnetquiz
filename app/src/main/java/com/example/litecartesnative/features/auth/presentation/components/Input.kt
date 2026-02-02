package com.example.litecartesnative.features.auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun Input(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    leadingIcon: Painter? = null
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontFamily = nunitosFontFamily,
            color = LitecartesColor.DarkBrown,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        label = {
            Text(
                text = label,
                color = LitecartesColor.Secondary,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    painter = leadingIcon,
                    contentDescription = null,
                    tint = LitecartesColor.Secondary
                )
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LitecartesColor.Secondary,
            unfocusedBorderColor = LitecartesColor.Secondary,
            focusedContainerColor = LitecartesColor.DarkerSurface.copy(alpha = 0.5f),
            unfocusedContainerColor = LitecartesColor.DarkerSurface.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}
