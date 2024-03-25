package com.ataglance.walletglance.ui.theme.uielements.accounts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.model.AccountColors
import com.ataglance.walletglance.model.AccountController
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsMedium
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@Composable
fun AccountCard(
    account: Account,
    appTheme: AppTheme?,
    todayExpenses: Double,
    onHideBalanceButton: () -> Unit = {}
) {
    val accountAndOnAccountColor = AccountController().getAccountAndOnAccountColor(account.color, appTheme)
    val accountColorLighter by animateColorAsState(
        targetValue = accountAndOnAccountColor.first.lighter,
        label = "account background lighter"
    )
    val accountColorDarker by animateColorAsState(
        targetValue = accountAndOnAccountColor.first.darker,
        label = "account background darker"
    )
    val onAccountColor by animateColorAsState(
        targetValue = accountAndOnAccountColor.second,
        label = "account color"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .shadow(
                elevation = 25.dp,
                shape = RoundedCornerShape(dimensionResource(R.dimen.account_card_corner_size)),
                spotColor = accountColorDarker
            )
            .clip(RoundedCornerShape(dimensionResource(R.dimen.account_card_corner_size)))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(accountColorDarker, accountColorLighter),
                    start = Offset(-30f, 230f),
                    end = Offset(40f, -100f)
                )
            )
            .fillMaxWidth(
                when {
                    WindowTypeIsCompact -> .9f
                    WindowTypeIsMedium -> .6f
                    else -> .42f
                }
            )
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(.96f)
        ) {
            AnimatedContent(
                targetState = account.name,
                label = "account name"
            ) { targetContent ->
                Text(
                    text = targetContent,
                    fontSize = 20.sp,
                    color = onAccountColor,
                    fontWeight = FontWeight.ExtraLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                BalanceRow(account, onAccountColor)
                TodayStatistic(todayExpenses, account.currency, onAccountColor)
            }
            FilledIconButton(
                onClick = onHideBalanceButton,
                shape = RoundedCornerShape(13.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = onAccountColor.copy(.1f),
                    contentColor = onAccountColor
                ),
                modifier = Modifier.bounceClickEffect(.96f)
            ) {
                Icon(
                    painter = painterResource(
                        if (account.hideBalance) R.drawable.hide_icon else R.drawable.show_icon
                    ),
                    contentDescription = "show or hide balance",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun BalanceRow(account: Account, onAccountColor: Color) {

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.balance),
            fontSize = 16.sp,
            color = onAccountColor.copy(.5f)
        )
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            AnimatedContent(
                targetState = account.getFormattedBalanceBeforeDecimalSeparatorOrHiddenBalance(),
                label = "account balance before decimal separator"
            ) { targetContent ->
                Text(
                    text = targetContent,
                    color = onAccountColor,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            AnimatedContent(
                targetState = account.getFormattedBalanceAfterDecimalSeparatorOrEmptyString(),
                label = "account balance after decimal separator"
            ) { targetContent ->
                Text(
                    text = targetContent,
                    color = onAccountColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 33.sp,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            AnimatedContent(
                targetState = account.currency,
                label = "account currency"
            ) { targetContent ->
                Text(
                    text = targetContent,
                    color = onAccountColor,
                    fontSize = 22.sp,
                    lineHeight = 35.sp,
                )
            }
        }
    }
}

@Composable
private fun TodayStatistic(todayExpenses: Double, currency: String, onAccountColor: Color) {
    AnimatedContent(
        targetState = if (todayExpenses == 0.0) {
            stringResource(R.string.greetings_empty_message)
        } else {
            stringResource(R.string.greetings_expenses_message, todayExpenses, currency)
        },
        label = "account today statistics"
    ) { targetContent ->
        Text(
            text = targetContent,
            fontSize = 16.sp,
            color = onAccountColor
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AccountCardPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.main_background_dark),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        AccountCard(
            account = Account(color = AccountColors.Blue(AppTheme.DarkDefault).name.name),
            appTheme = AppTheme.DarkDefault,
            todayExpenses = 0.0
        )
    }
}