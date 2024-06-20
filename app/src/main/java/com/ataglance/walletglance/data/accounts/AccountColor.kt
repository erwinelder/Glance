package com.ataglance.walletglance.data.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors
import com.ataglance.walletglance.ui.theme.uielements.accounts.AccountCard
import com.ataglance.walletglance.data.app.Colors

enum class AccountColorName {
    Default, // CONNECTED WITH ROOM DATABASE, ACCOUNT CLASS DEFAULT COLOR
    Pink, Blue, Camel, Red, Green
}

data class AccountColor(
    val default: LighterDarkerColors,
    val pink: LighterDarkerColors,
    val blue: LighterDarkerColors,
    val camel: LighterDarkerColors,
    val red: LighterDarkerColors,
    val green: LighterDarkerColors
)

data class AccountColorsByTheme(
    val light: AccountColor = AccountColor(
        default = LighterDarkerColors(
            Color(52, 52, 52), Color(32, 32, 32)
        ),
        pink = LighterDarkerColors(
            Color(179, 101, 146), Color(133, 77, 110)
        ),
        blue = LighterDarkerColors(
            Color(102, 88, 177), Color(83, 71, 143)
        ),
        camel = LighterDarkerColors(
            Color(190, 134, 93), Color(162, 114, 78)
        ),
        red = LighterDarkerColors(
            Color(206, 78, 78, 255), Color(162, 61, 61, 255)
        ),
        green = LighterDarkerColors(
            Color(91, 194, 136), Color(73, 155, 108)
        )
    ),
    val dark: AccountColor = AccountColor(
        default = LighterDarkerColors(
            Color(220, 220, 220), Color(200, 200, 200)
        ),
        pink = LighterDarkerColors(
            Color(163, 88, 130), Color(116, 63, 92)
        ),
        blue = LighterDarkerColors(
            Color(87, 75, 150), Color(69, 59, 119)
        ),
        camel = LighterDarkerColors(
            Color(173, 122, 84), Color(139, 98, 67)
        ),
        red = LighterDarkerColors(
            Color(180, 68, 68), Color(138, 52, 52)
        ),
        green = LighterDarkerColors(
            Color(75, 158, 111, 255), Color(56, 119, 83, 255)
        )
    ),
)

sealed class AccountColors(val color: Colors) {
    data class Default(val appTheme: AppTheme) : AccountColors(
        Colors(
            AccountColorName.Default.name,
            when(appTheme) {
                AppTheme.LightDefault -> AccountColorsByTheme().light.default
                AppTheme.DarkDefault -> AccountColorsByTheme().dark.default
            }
        )
    )
    data class Pink(val appTheme: AppTheme) : AccountColors(
        Colors(
            AccountColorName.Pink.name,
            when(appTheme) {
                AppTheme.LightDefault -> AccountColorsByTheme().light.pink
                AppTheme.DarkDefault -> AccountColorsByTheme().dark.pink
            }
        )
    )
    data class Blue(val appTheme: AppTheme) : AccountColors(
        Colors(
            AccountColorName.Blue.name,
            when(appTheme) {
                AppTheme.LightDefault -> AccountColorsByTheme().light.blue
                AppTheme.DarkDefault -> AccountColorsByTheme().dark.blue
            }
        )
    )
    data class Camel(val appTheme: AppTheme) : AccountColors(
        Colors(
            AccountColorName.Camel.name,
            when(appTheme) {
                AppTheme.LightDefault -> AccountColorsByTheme().light.camel
                AppTheme.DarkDefault -> AccountColorsByTheme().dark.camel
            }
        )
    )
    data class Green(val appTheme: AppTheme) : AccountColors(
        Colors(
            AccountColorName.Green.name,
            when(appTheme) {
                AppTheme.LightDefault -> AccountColorsByTheme().light.green
                AppTheme.DarkDefault -> AccountColorsByTheme().dark.green
            }
        )
    )
    data class Red(val appTheme: AppTheme) : AccountColors(
        Colors(
            AccountColorName.Red.name,
            when(appTheme) {
                AppTheme.LightDefault -> AccountColorsByTheme().light.red
                AppTheme.DarkDefault -> AccountColorsByTheme().dark.red
            }
        )
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AccountCardPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.main_background_light),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        AccountCard(
            account = Account(color = AccountColors.Red(AppTheme.LightDefault).color.name),
            appTheme = AppTheme.LightDefault,
            todayExpenses = 0.0
        )
    }
}
