package com.example.litecartesnative.features.pretest.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun PretestButton(
    text: String,
    isActive: Boolean = false,
    onClick: () -> Unit = {},
    backgroundColor: Color = LitecartesColor.DarkerSurface,
    textColor: Color = LitecartesColor.DarkBrown
) {
    OutlinedButton(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .padding(
                vertical = 2.dp
            )
        ,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, backgroundColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isActive) {
                textColor
            } else {
                backgroundColor
            }
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp) // Add elevation here
    ) {
        Text(
            text = text,
            color = if(isActive) {
                backgroundColor
            } else {
                textColor
            },
            modifier = Modifier
                .padding(
                    vertical = 4.dp
                )
        )
    }
}

@Preview
@Composable
fun PreviewPretestButton() {
    PretestButton(
        text = "Ini Opsi A"
    )
}