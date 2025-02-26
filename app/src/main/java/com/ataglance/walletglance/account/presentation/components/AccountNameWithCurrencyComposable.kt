package com.ataglance.walletglance.account.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.LighterDarkerColors
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun AccountNameWithCurrencyComposable(
    account: Account?,
    fontSize: TextUnit = 21.sp,
    roundedCornerSize: Dp = 14.dp,
    horizontalPadding: Dp = 9.dp,
    verticalPadding: Dp = 4.dp,
    outerPadding: PaddingValues = PaddingValues(0.dp),
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val accountAndOnAccountColor = account?.let {
        account.color.getColorAndColorOnByTheme(CurrAppTheme)
    } ?: Pair(LighterDarkerColors(), Color.White)
    val transparency by animateFloatAsState(
        targetValue = if (!enabled && account != null) 0.5f else 1f,
        label = "account transparency"
    )
    val accountGradientColor = accountAndOnAccountColor.first
    val onAccountColor = accountAndOnAccountColor.second

    Box(
        modifier = Modifier
            .padding(outerPadding)
            .alpha(transparency)
    ) {
        Box(
            modifier = Modifier
                .bounceClickEffect(.97f, enabled = onClick != null && enabled) {
                    onClick?.let { it() }
                }
                .clip(RoundedCornerShape(roundedCornerSize + 1.dp))
                .background(GlanceColors.accountSemiTransparentBackground)
                .padding(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(roundedCornerSize))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accountGradientColor.darker,
                                accountGradientColor.lighter
                            ),
                            start = Offset(0f, 200f),
                            end = Offset(100f, 0f)
                        )
                    )
            ) {
                Text(
                    text = account?.name ?: "???",
                    color = onAccountColor,
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(
                            top = verticalPadding - 1.dp, bottom = verticalPadding,
                            start = horizontalPadding,
                            end = horizontalPadding - 1.dp
                        )
                )
                Text(
                    text = account?.currency ?: "???",
                    color = onAccountColor,
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clip(RoundedCornerShape(roundedCornerSize))
                        .background(
                            if (account?.color?.name == AccountColors.Default.name) {
                                GlanceColors.background.copy(.07f)
                            } else {
                                Color.White.copy(.09f)
                            }
                        )
                        .padding(
                            top = verticalPadding - 1.dp, bottom = verticalPadding,
                            start = horizontalPadding, end = horizontalPadding
                        )
                )
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun AccountNameWithCurrencyComposablePreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        AccountNameWithCurrencyComposable(Account(balance = 516.41))
    }
}