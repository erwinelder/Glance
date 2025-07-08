package com.ataglance.walletglance.transfer.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.transaction.presentation.component.RecentRecordsWidgetPreview
import com.ataglance.walletglance.transaction.presentation.model.TransferUiState
import com.ataglance.walletglance.transaction.presentation.screen.TransactionsScreenPreview

@Composable
fun TransferGlassComponent(
    uiState: TransferUiState,
    onClick: (Long) -> Unit
) {
    GlassSurface(
        filledWidths = null,
        cornerSize = 26.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.bounceClickEffect(.98f) {
            onClick(uiState.id)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TransferComponentContent(uiState = uiState)
        }
    }
}

@Composable
fun TransferOnGlassComponent(
    uiState: TransferUiState,
    onClick: (Long) -> Unit
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onClick(uiState.id) }
    ) {
        TransferComponentContent(uiState = uiState)
    }
}

@Composable
private fun TransferComponentContent(
    uiState: TransferUiState
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiState.date,
            color = GlanciColors.outline,
            fontSize = 16.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.Light
        )
        Text(
            text = stringResource(R.string.transfer),
            color = GlanciColors.primary,
            fontSize = 16.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.Light
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiState.secondAccountCompanionText,
            color = GlanciColors.onSurface,
            fontSize = 18.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.Light
        )
        Text(
            text = uiState.secondAccount.name,
            color = uiState.secondAccount.color.colorOn.getByTheme(CurrAppTheme),
            fontSize = 18.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .clip(RoundedCornerShape(42))
                .background(
                    uiState.secondAccount.color.color.getByTheme(CurrAppTheme).lighter
                )
                .padding(7.dp, 3.dp)
        )
    }
    Text(
        text = uiState.amount,
        color = GlanciColors.onSurface,
        fontSize = 20.sp,
        fontFamily = Manrope,
        fontWeight = FontWeight.Light
    )
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun TransferGlassComponentPreview() {
    TransactionsScreenPreview()
}

@Preview(device = PIXEL_7_PRO)
@Composable
private fun TransferOnGlassComponentPreview() {
    RecentRecordsWidgetPreview()
}
