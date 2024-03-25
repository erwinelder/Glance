package com.ataglance.walletglance.ui.theme.uielements.buttons

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.Manrope

@Composable
fun TertiaryButton(
    onClick: () -> Unit,
    text: String,
    fontSize: TextUnit = 20.sp
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = GlanceTheme.primary
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


@Preview(showSystemUi = true)
@Composable
private fun PreviewTertiaryButton() {
    TertiaryButton(onClick = {}, text = "Apply")
}