package com.ataglance.walletglance.request.presentation.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.text.Title
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.request.presentation.modelNew.ResultState

@Composable
fun ResultStateButtonComponent(
    state: ResultState.ButtonState,
    usePrimaryButtonInstead: Boolean = true,
    onButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth(FilledWidthByScreenType().get(CurrWindowType))
    ) {
        Title(
            text = stringResource(state.titleRes),
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType().get(CurrWindowType))
        )
        state.messageRes?.let {
            Text(
                text = stringResource(it),
                color = GlanciColors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                fontFamily = Manrope,
                textAlign = TextAlign.Center
            )
        }
        if (usePrimaryButtonInstead) {
            SmallPrimaryButton(
                text = stringResource(state.buttonTextRes),
                iconRes = state.buttonIconRes,
                onClick = onButtonClick
            )
        } else {
            SmallSecondaryButton(
                text = stringResource(state.buttonTextRes),
                iconRes = state.buttonIconRes,
                onClick = onButtonClick
            )
        }
    }
}