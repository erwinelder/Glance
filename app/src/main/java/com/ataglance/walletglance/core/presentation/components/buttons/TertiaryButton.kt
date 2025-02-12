package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer

@Composable
fun TertiaryButton(
    text: String,
    fontSize: TextUnit = 20.sp,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = GlanceColors.primary
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = Manrope,
            textDecoration = TextDecoration.Underline
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun PreviewTertiaryButton() {
    PreviewContainer {
        TertiaryButton(onClick = {}, text = "Apply")
    }
}