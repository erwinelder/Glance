package com.ataglance.walletglance.ui.theme.uielements.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.accounts.color.AccountColors
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.utils.toAccountColorWithName

@Composable
fun MediumAccountSetup(
    account: Account,
    appTheme: AppTheme?,
    modifier: Modifier = Modifier,
    fontSize: Int = 20,
    roundedCornerSize: Dp = 18.dp,
    onAccountClick: (Account) -> Unit = {},
    onUpButtonClick: () -> Unit = {},
    upButtonEnabled: Boolean = false,
    onDownButtonClick: () -> Unit = {},
    downButtonEnabled: Boolean = false
) {
    val accountBackgroundAndColor = account.color.getColorAndColorOnByTheme(appTheme)
    val accountColor = accountBackgroundAndColor.first
    val onAccountColor = accountBackgroundAndColor.second

    Box(
        modifier = modifier
            .bounceClickEffect(shrinkScale = .98f)
            .clip(RoundedCornerShape(roundedCornerSize + 5.dp))
            .background(GlanceTheme.onSurface.copy(alpha = .2f))
            .padding(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(roundedCornerSize + 4.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(accountColor.darker, accountColor.lighter),
                        start = Offset(0f, 200f),
                        end = Offset(100f, 0f)
                    )
                )
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .clickable { onAccountClick(account) }
                    .width(IntrinsicSize.Max)
                    .weight(1f)
                    .padding(vertical = if (account.withoutBalance) 6.dp else 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.name),
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    fontSize = (fontSize - 6).sp,
                    color = onAccountColor.copy(.65f)
                )
                Text(
                    text = account.name,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = onAccountColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                if (!account.withoutBalance) {
                    Text(
                        text = stringResource(R.string.balance),
                        fontSize = (fontSize - 6).sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        color = onAccountColor.copy(.65f)
                    )
                    Text(
                        text = account.getFormattedBalanceWithCurrency(),
                        fontSize = (fontSize + 4).sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = onAccountColor
                    )
                }
            }
            Spacer(modifier = Modifier.requiredWidth(12.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .requiredWidth(30.dp)
                    .fillMaxHeight(.85f)
            ) {
                IconButton(
                    onClick = onUpButtonClick,
                    enabled = upButtonEnabled,
                    modifier = Modifier.size(30.dp, 18.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.short_arrow_up_icon),
                        contentDescription = "arrow up",
                        tint = if (upButtonEnabled) {
                            onAccountColor
                        } else {
                            GlanceTheme.outline.copy(.5f)
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
                        contentDescription = "arrow down",
                        tint = if (downButtonEnabled) {
                            onAccountColor
                        } else {
                            GlanceTheme.outline.copy(.5f)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MediumAccountSetupPreview() {
    BoxWithConstraints {
        WalletGlanceTheme(
            useDeviceTheme = false,
            lastChosenTheme = AppTheme.LightDefault.name,
            boxWithConstraintsScope = this
        ) {
            MediumAccountSetup(
                account = Account(
                    balance = 516.41,
                    name = "Main USD",
                    color = AccountColors.Default.toAccountColorWithName()
                ),
                appTheme = AppTheme.LightDefault
            )
        }
    }
}