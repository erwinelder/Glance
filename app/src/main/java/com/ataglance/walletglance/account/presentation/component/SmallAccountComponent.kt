package com.ataglance.walletglance.account.presentation.component

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
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun SmallAccountComponent(
    account: Account?,
    fontSize: TextUnit = 21.sp,
    roundedCornerSize: Dp = 14.dp,
    horizontalPadding: Dp = 10.dp,
    verticalPadding: Dp = 4.dp,
    outerPadding: PaddingValues = PaddingValues(0.dp),
    adjustStyleByActiveStatus: Boolean = false,
    showBalance: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val accountAndOnAccountColor = account?.let {
        account.color.getColorAndColorOnByTheme(CurrAppTheme)
    } ?: Pair(LighterDarkerColors(), Color.White)
    val alpha by animateFloatAsState(
        targetValue =
            if (adjustStyleByActiveStatus && account != null && !account.isActive) 0.6f else 1f
    )
    val accountGradientColor = accountAndOnAccountColor.first
    val onAccountColor = accountAndOnAccountColor.second
    val showBalanceBlock = account != null && !account.withoutBalance && showBalance

    Box(
        modifier = Modifier
            .padding(outerPadding)
            .alpha(alpha)
    ) {
        Box(
            modifier = Modifier
                .bounceClickEffect(
                    shrinkScale = .97f,
                    enabled = onClick != null,
                    onClick = { onClick?.let { it() } }
                )
                .clip(RoundedCornerShape(roundedCornerSize + 1.dp))
                .background(GlanciColors.accountSemiTransparentBackground)
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
                    fontSize = fontSize,
                    fontFamily = Manrope,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(
                            top = verticalPadding - 1.dp, bottom = verticalPadding,
                            start = horizontalPadding,
                            end = horizontalPadding - if (showBalanceBlock) 1.dp else 0.dp
                        )
                )
                if (account != null && showBalanceBlock) {
                    Text(
                        text = account.getFormattedBalance(),
                        color = onAccountColor,
                        fontSize = fontSize,
                        fontFamily = Manrope,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clip(RoundedCornerShape(roundedCornerSize))
                            .background(
                                if (account.color.name == AccountColors.Default.name) {
                                    GlanciColors.background.copy(.07f)
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
}

@Preview(showSystemUi = false)
@Composable
private fun SmallAccountPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SmallAccountComponent(Account(balance = 516.41))
    }
}