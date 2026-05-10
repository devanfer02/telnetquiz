package com.example.telnetquiz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun Input(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    onTouched: (() -> Unit)? = null,
    textStyle: TextStyle = TextStyle(
        fontFamily = nunitosFontFamily,
        color = LitecartesColor.DarkBrown,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = LitecartesColor.Secondary,
        unfocusedBorderColor = LitecartesColor.Secondary,
        focusedContainerColor = LitecartesColor.DarkerSurface.copy(alpha = 0.5f),
        unfocusedContainerColor = LitecartesColor.DarkerSurface.copy(alpha = 0.3f)
    )
) {
    var hasFocused by remember { mutableStateOf(false) }
    val focusModifier = if (onTouched != null) {
        Modifier.onFocusChanged { state ->
            if (state.isFocused) hasFocused = true
            else if (hasFocused) onTouched()
        }
    } else Modifier

    OutlinedTextField(
        modifier = modifier.fillMaxWidth().then(focusModifier),
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
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
        colors = colors,
        shape = RoundedCornerShape(16.dp),
        singleLine = singleLine,
        isError = isError,
        supportingText = errorMessage?.takeIf { isError }?.let {
            {
                Text(
                    text = it,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    )
}
