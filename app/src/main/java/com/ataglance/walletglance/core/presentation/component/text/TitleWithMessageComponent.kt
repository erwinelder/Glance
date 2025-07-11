package com.ataglance.walletglance.core.presentation.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun TitleWithMessageComponent(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title(
            text = title,
            modifier = modifier
                .fillMaxWidth(FilledWidthByScreenType(.84f).get(CurrWindowType))
        )
        message?.let {
            Text(
                text = it,
                color = GlanciColors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                fontFamily = Manrope,
                textAlign = TextAlign.Center
            )
        }
    }
}