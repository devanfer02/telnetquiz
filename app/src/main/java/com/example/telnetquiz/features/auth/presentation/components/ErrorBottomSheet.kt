package com.example.telnetquiz.features.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telnetquiz.components.Button
import com.example.telnetquiz.ui.theme.LitecartesColor
import com.example.telnetquiz.ui.theme.nunitosFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorBottomSheet(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = LitecartesColor.Surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = nunitosFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = LitecartesColor.Secondary
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = message,
                fontFamily = nunitosFontFamily,
                fontSize = 14.sp,
                color = LitecartesColor.DarkBrown,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Button(
                text = "OK",
                borderColor = LitecartesColor.Secondary,
                color = LitecartesColor.Surface,
                backgroundColor = LitecartesColor.Secondary,
                shadowEnabled = true,
                shadowHeight = 45.dp,
                shadowColor = LitecartesColor.DarkBrown,
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss,
                textModifier = Modifier.padding(8.dp)
            )
        }
    }
}
