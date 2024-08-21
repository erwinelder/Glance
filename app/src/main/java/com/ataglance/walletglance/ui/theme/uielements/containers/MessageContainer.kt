package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.ui.theme.GlanceTheme

@Composable
fun MessageContainer(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = message,
            color = GlanceTheme.onSurface.copy(.75f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
private fun Preview() {
    PreviewContainer {
        MessageContainer(message = "There is no data message")
    }
}