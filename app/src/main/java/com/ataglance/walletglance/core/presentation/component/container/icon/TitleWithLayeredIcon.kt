package com.ataglance.walletglance.core.presentation.component.container.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.icon.LayeredIcon
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.NotoSans

@Composable
fun TitleWithLayeredIcon(
    title: String,
    @DrawableRes iconRes: Int,
    visible: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LayeredIcon(iconRes = iconRes, visible = visible)
        Text(
            text = title,
            style = GlanciTypography.titleLarge,
            color = GlanciColors.onSurface,
            fontFamily = NotoSans,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType(compact = .86f)
                    .getByType(CurrWindowType))
                .padding(vertical = 16.dp)
        )

    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun TransformedLargeIconPreviewDarkDefault(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewContainer(appTheme = appTheme) {
        TitleWithLayeredIcon(
            title = "Choose app language",
            iconRes = R.drawable.language_layer_icon,
            visible = true
        )
    }
}