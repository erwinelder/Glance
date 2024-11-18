package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun SecondaryButton(
    text: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 18.sp,
    onClick: () -> Unit
) {
    val modifier = if (WindowTypeIsCompact) {
        Modifier.fillMaxWidth(.82f)
    } else {
        Modifier.width(400.dp)
    }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = GlanceTheme.surface.copy(.08f),
            contentColor = GlanceTheme.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = GlanceTheme.outline
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.large_button_corners)),
        border = BorderStroke(
            2.dp,
            if (enabled) GlanceTheme.primary
            else GlanceTheme.outline
        ),
        contentPadding = PaddingValues(vertical = 18.dp),
        modifier = modifier
            .bounceClickEffect(.97f)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.large_button_corners)))
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = Manrope
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun PreviewSecondaryButton() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SecondaryButton(onClick = {}, text = "Apply")
    }
}