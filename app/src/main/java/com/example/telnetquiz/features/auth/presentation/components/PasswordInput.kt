package com.example.telnetquiz.features.auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.R
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@Composable
fun PasswordInput(
    value: String,
    label: String = "Kata Sandi",
    onValueChange: (String) -> Unit,
    leadingIcon: Painter? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    onTouched: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
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
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) {
                            R.drawable.ic_visibility_off
                        } else {
                            R.drawable.ic_visibility
                        }
                    ),
                    contentDescription = if (passwordVisible) {
                        "Sembunyikan kata sandi"
                    } else {
                        "Tampilkan kata sandi"
                    },
                    tint = LitecartesColor.Secondary
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LitecartesColor.Secondary,
            unfocusedBorderColor = LitecartesColor.Secondary,
            focusedContainerColor = LitecartesColor.DarkerSurface.copy(alpha = 0.5f),
            unfocusedContainerColor = LitecartesColor.DarkerSurface.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
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
