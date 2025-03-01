package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun MessageContainer(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = message,
            color = GlanceColors.onSurface.copy(.6f),
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
private fun Preview() {
    PreviewContainer {
        MessageContainer(message = "Oops, it looks like there is no data")
    }
}