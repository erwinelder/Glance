package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.component.field.FieldLabel

@Composable
fun ColorButton(
    color: Color,
    onClick: () -> Unit
) {
    val buttonColor by animateColorAsState(
        targetValue = color,
        label = "account color picker button color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldLabel(text = stringResource(R.string.color))
        Spacer(
            modifier = Modifier
                .bounceClickEffect(.97f, onClick = onClick)
                .clip(RoundedCornerShape(15.dp))
                .background(buttonColor)
                .size(70.dp, 42.dp)
        )
    }
}