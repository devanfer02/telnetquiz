package com.example.telnetquiz.features.quiz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.tutorial.LocalTutorialController
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily
import com.example.telnetquiz.ui.theme.scoreColor

@Composable
fun LevelOptionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    score: Int?,
    onLearnFirst: () -> Unit,
    onPlayDirectly: () -> Unit
) {
    val tutorialController = LocalTutorialController.current
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(16.dp)
        )
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .width(200.dp)
                .background(LitecartesColor.Surface)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (score != null) {
                    Text(
                        text = "Skor: $score",
                        color = scoreColor(score),
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Button(
                    onClick = {
                        tutorialController?.notifyTargetClicked("dialog_learn_first_btn")
                        onLearnFirst()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (tutorialController != null) Modifier.onGloballyPositioned {
                                tutorialController.registerTarget("dialog_learn_first_btn", it)
                            } else Modifier
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LitecartesColor.Primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Belajar Dulu",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onPlayDirectly,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LitecartesColor.Secondary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Langsung Main",
                        fontFamily = nunitosFontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
