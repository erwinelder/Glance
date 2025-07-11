package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun LoadingStateComponent(
    message: String,
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        Text(
            text = message,
            color = GlanciColors.outline,
            fontSize = 18.sp,
            fontWeight = FontWeight.W300,
            fontFamily = Manrope
        )
        onCancel?.let {
            SmallSecondaryButton(
                text = stringResource(R.string.cancel),
                iconRes = R.drawable.close_icon,
                onClick = onCancel
            )
        }
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun LoadingStateComponentPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        LoadingStateComponent(
            message = "Loading, please wait...",
            onCancel = {}
        )
    }
}