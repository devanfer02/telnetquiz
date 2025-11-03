package com.example.litecartesnative.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.litecartesnative.ui.theme.LitecartesColor
import com.example.litecartesnative.ui.theme.nunitosFontFamily

@Composable
fun Button(
    text: String,
    color: Color,
    backgroundColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    shadowEnabled: Boolean = false,
    shadowColor: Color? = null,
    shadowHeight: Dp = 55.dp,
    icon: Painter? = null,
    onClick: () -> Unit = {},
    textModifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Box {
        if (shadowEnabled) {
            OutlinedButton(
                modifier = modifier
                    .padding(5.dp)
                    .heightIn(min = shadowHeight)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = false
                    ),
                onClick = onClick,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, borderColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = shadowColor!!
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = horizontalArrangement,
                    modifier = modifier
                ){
                    if (icon != null) {
                        Image(
                            painter = icon,
                            contentDescription = ""
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(
                                    end = 18.dp
                                )
                        )
                    }
                    Text(
                        modifier = textModifier,
                        text = text,
                        color = color,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = nunitosFontFamily,
                        fontSize = fontSize
                    )
                }
            }
        }
        OutlinedButton(
            modifier = modifier
                .padding(5.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = false
                ),
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalArrangement,
                modifier = modifier
            ){
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = ""
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(
                                end = 18.dp
                            )
                    )
                }
                Text(
                    modifier = textModifier,
                    text = text,
                    color = color,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = nunitosFontFamily,
                    fontSize = fontSize
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewButton() {
    Button(
        text = "Yuk Main".uppercase(),
        color = LitecartesColor.Secondary,
        backgroundColor = LitecartesColor.Surface,
        borderColor = LitecartesColor.DarkBrown,
        shadowEnabled = true,
        shadowColor = LitecartesColor.DarkBrown,
    )
}