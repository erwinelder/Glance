package com.ataglance.walletglance.account.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.viewmodel.ActiveAccountCardViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.SmallFilledIconButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.Manrope
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ActiveAccountCard(
    activeAccount: Account?,
    onChangeHideActiveAccountBalance: () -> Unit,
) {
    val viewModel = koinViewModel<ActiveAccountCardViewModel>()

    LaunchedEffect(activeAccount) {
        activeAccount?.id?.let(viewModel::applyActiveAccount)
    }

    val todayExpenses by viewModel.todayTotalExpenses.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = activeAccount,
        transitionSpec = {
            (scaleIn(initialScale = .6f, animationSpec = spring(stiffness = 220f)) +
                    fadeIn(animationSpec = spring(stiffness = 110f)) +
                    slideInVertically(animationSpec = spring(stiffness = 220f)) {
                        (-it * 0.2).roundToInt()
                    }
            ).togetherWith(
                scaleOut(targetScale = .6f, animationSpec = spring(stiffness = 220f)) +
                        fadeOut(animationSpec = spring(stiffness = 110f)) +
                        slideOutVertically(animationSpec = spring(stiffness = 220f)) {
                            (it * 0.2).roundToInt()
                        }
            )
        },
        contentKey = { it?.id },
        modifier = Modifier.padding(bottom = 16.dp)
    ) { account ->
        if (account != null) {
            ActiveAccountCardContent(
                account = account,
                todayExpenses = todayExpenses,
                onHideBalanceButton = onChangeHideActiveAccountBalance
            )
        }
    }
}

@Composable
fun ActiveAccountCardContent(
    account: Account,
    todayExpenses: Double,
    onHideBalanceButton: () -> Unit = {}
) {
    val (accountColor, onAccountColor) = account.color.getColorAndColorOnByTheme(CurrAppTheme)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.account_card_corner_size)))
            .background(
                brush = Brush.linearGradient(
                    colors = accountColor.asListDarkToLight(),
                    start = Offset(-30f, 230f),
                    end = Offset(40f, -100f)
                )
            )
            .fillMaxWidth(FilledWidthByScreenType(.9f, .6f, .42f).getByType(CurrWindowType))
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(.96f)
        ) {
            Text(
                text = account.name,
                fontSize = 20.sp,
                color = onAccountColor,
                fontWeight = FontWeight.ExtraLight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
            SmallFilledIconButton(
                iconRes = if (account.hideBalance) R.drawable.hide_icon else R.drawable.show_icon,
                iconContendDescription = "show or hide balance",
                containerColor = onAccountColor.copy(.1f),
                contentColor = onAccountColor,
                enabled = !account.withoutBalance,
                onClick = onHideBalanceButton
            )
        }
    }
}

@Composable
private fun BalanceRow(account: Account, onAccountColor: Color) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (account.withoutBalance) {
                stringResource(R.string.currency)
            } else {
                stringResource(R.string.balance)
            },
            fontSize = 16.sp,
            color = onAccountColor.copy(.5f),
            fontFamily = Manrope
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.horizontalScroll(scrollState)
        ) {
            AnimatedContent(
                targetState = account.getFormattedBalanceBeforeDecimalSeparator()
            ) { balanceBeforeDecimalSeparator ->
                Text(
                    text = balanceBeforeDecimalSeparator,
                    color = onAccountColor,
                    fontSize = 30.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Manrope
                )
            }
            AnimatedContent(
                targetState = account.getFormattedBalanceAfterDecimalSeparator()
            ) { balanceAfterDecimalSeparator ->
                Text(
                    text = balanceAfterDecimalSeparator,
                    color = onAccountColor,
                    fontSize = 19.sp,
                    lineHeight = 33.sp,
                    letterSpacing = .7.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Manrope
                )
            }
            Text(
                text = account.currency,
                color = onAccountColor,
                fontSize = if (account.withoutBalance) 30.sp else 22.sp,
                lineHeight = 35.sp,
                fontWeight = if (account.withoutBalance) FontWeight.Bold else FontWeight.Normal,
                fontFamily = Manrope,
                modifier = Modifier.padding(start = if (account.withoutBalance) 0.dp else 10.dp)
            )
        }
    }
}

@Composable
private fun TodayStatistic(todayExpenses: Double, currency: String, onAccountColor: Color) {
    AnimatedContent(
        targetState = if (todayExpenses == 0.0) {
            stringResource(R.string.greetings_empty_message)
        } else {
            stringResource(
                R.string.greetings_expenses_message,
                "%.2f".format(Locale.US, todayExpenses),
                currency
            )
        }
    ) { targetContent ->
        Text(
            text = targetContent,
            fontSize = 16.sp,
            color = onAccountColor
        )
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun AccountCardPreview() {
    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        ActiveAccountCardContent(
            account = Account(
                color = AccountColors.Blue,
                balance = 5160.0,
                withoutBalance = false
            ),
            todayExpenses = 160.0
        )
    }
}