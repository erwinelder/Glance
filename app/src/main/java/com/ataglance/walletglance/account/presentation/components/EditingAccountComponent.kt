package com.ataglance.walletglance.account.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun EditingAccountComponent(
    account: Account,
    onAccountClick: (Account) -> Unit,
    onUpButtonClick: () -> Unit,
    upButtonEnabled: Boolean,
    onDownButtonClick: () -> Unit,
    downButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val accountBackgroundAndColor = account.color.getColorAndColorOnByTheme(CurrAppTheme)
    val accountColor = accountBackgroundAndColor.first
    val onAccountColor = accountBackgroundAndColor.second

    Box(
        modifier = modifier
            .bounceClickEffect(shrinkScale = .98f) {
                onAccountClick(account)
            }
            .clip(RoundedCornerShape(26.dp))
            .background(GlanceColors.accountSemiTransparentBackground)
            .padding(2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = accountColor.asListDarkToLight(),
                        start = Offset(0f, 200f),
                        end = Offset(100f, 0f)
                    )
                )
                .height(IntrinsicSize.Min)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f, fill = false)
            ) {
                TextWithLabel(
                    labelText = stringResource(R.string.name),
                    text = account.name,
                    color = onAccountColor,
                    labelFontSize = if (account.withoutBalance) 18.sp else 16.sp,
                    textFontSize = if (account.withoutBalance) 22.sp else 20.sp
                )
                if (!account.withoutBalance) {
                    TextWithLabel(
                        labelText = stringResource(R.string.balance),
                        text = account.getFormattedBalanceWithCurrency(),
                        color = onAccountColor,
                        labelFontSize = 18.sp,
                        textFontSize = 22.sp
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight(.85f)
            ) {
                IconButton(
                    onClick = onUpButtonClick,
                    enabled = upButtonEnabled,
                    modifier = Modifier.size(30.dp, 18.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.short_arrow_up_icon),
                        contentDescription = "move account up",
                        tint = if (upButtonEnabled) {
                            onAccountColor
                        } else {
                            GlanceColors.outlineSemiTransparent
                        }
                    )
                }
                IconButton(
                    onClick = onDownButtonClick,
                    enabled = downButtonEnabled,
                    modifier = Modifier.size(30.dp, 18.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.short_arrow_down_icon),
                        contentDescription = "move account down",
                        tint = if (downButtonEnabled) {
                            onAccountColor
                        } else {
                            GlanceColors.outlineSemiTransparent
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TextWithLabel(
    labelText: String,
    text: String,
    color: Color = GlanceColors.onSurface,
    labelFontSize: TextUnit = 16.sp,
    textFontSize: TextUnit = 20.sp,
) {
    Column {
        Text(
            text = labelText,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = color.copy(.6f)
        )
        Text(
            text = text,
            fontSize = textFontSize,
            fontWeight = FontWeight.Light,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = color
        )
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun MediumAccountSetupPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        EditingAccountComponent(
            account = Account(
                balance = 516.41,
                name = "Main USD",
                color = AccountColors.Default,
                withoutBalance = false
            ),
            onAccountClick = {},
            onUpButtonClick = {},
            upButtonEnabled = true,
            onDownButtonClick = {},
            downButtonEnabled = true
        )
    }
}