package com.example.litecartesnative.features.quiz.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.litecartesnative.ui.theme.LitecartesColor

@Composable
fun OptionButton(
    text: String,
    isActive: Boolean = false,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            ,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = LitecartesColor.Secondary
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isActive) {
                LitecartesColor.Secondary
            } else {
                LitecartesColor.DarkerSurface
            }
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp) // Add elevation here
    ) {
        Text(
            text = text,
            color = if(isActive) {
                LitecartesColor.Surface
            } else {
                LitecartesColor.Secondary
            }
        )
    }
}

@Preview
@Composable
fun PreviewOptionButton() {
    OptionButton(
        text = "Ini Opsi A"
    )
}