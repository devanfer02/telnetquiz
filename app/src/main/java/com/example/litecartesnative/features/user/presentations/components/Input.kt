package com.example.litecartesnative.features.user.presentations.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun Input(
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    Row(

    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    color = LitecartesColor.Secondary,
                    fontFamily = nunitosFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = LitecartesColor.Surface,
                focusedContainerColor = LitecartesColor.Surface,
                focusedTextColor = LitecartesColor.Secondary,
                unfocusedTextColor = LitecartesColor.Secondary
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Preview
@Composable
fun PreviewInput() {
    Input(
        value = "Maudy",
        label = "Search",
        onValueChange = {}
    )
}