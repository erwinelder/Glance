package com.ataglance.walletglance.recordCreation.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun RecordCreationWidget(
    onMakeRecord: () -> Unit,
    onMakeTransfer: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(FilledWidthByScreenType().getByType(CurrWindowType))
    ) {
        RecordCreationWidgetButton(
            iconRes = R.drawable.make_record_dark_default,
            contentDescription = "Make record",
            text = stringResource(R.string.create_record),
            onClick = onMakeRecord
        )
        RecordCreationWidgetButton(
            iconRes = R.drawable.make_transfer_dark_default,
            contentDescription = "Make transfer",
            text = stringResource(R.string.make_transfer),
            onClick = onMakeTransfer
        )
    }
}

@Composable
private fun RowScope.RecordCreationWidgetButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    text: String,
    onClick: () -> Unit
) {
    val cornerSize = 24.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .bounceClickEffect(onClick = onClick)
            .weight(1f)
            .clip(RoundedCornerShape(cornerSize))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanciColors.primaryGlassGradient,
                    start = Offset(1100f, 0f),
                    end = Offset(0f, 1200f)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = GlanciColors.primaryGlassBorderGradient,
                    start = Offset(10f, 0f),
                    end = Offset(0f, 100f)
                ),
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(1.dp)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = text,
            color = GlanciColors.onPrimary,
            fontSize = 18.sp,
            fontFamily = Manrope,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}



@Preview(device = PIXEL_7_PRO, locale = "en")
@Composable
private fun RecordCreationWidgetPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        RecordCreationWidget(
            onMakeRecord = {},
            onMakeTransfer = {}
        )
    }
}