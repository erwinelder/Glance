package com.ataglance.walletglance.account.domain.model.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.component.ActiveAccountCardContent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorByTheme
import com.ataglance.walletglance.core.domain.color.LighterDarkerColors
import com.ataglance.walletglance.core.domain.color.LighterDarkerColorsByTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer

sealed class AccountColors(
    val name: AccountColorName,
    val color: LighterDarkerColorsByTheme,
    val colorOn: ColorByTheme
) {

    data object Default : AccountColors(
        name = AccountColorName.Default,
        color = LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(52, 52, 52), Color(32, 32, 32)
            ),
            darkDefault = LighterDarkerColors(
                Color(220, 220, 220), Color(200, 200, 200)
            )
        ),
        colorOn = ColorByTheme(
            lightDefault = Color(240, 240, 240),
            darkDefault = Color(18, 18, 18)
        )
    )

    data object Pink : AccountColors(
        name = AccountColorName.Pink,
        color = LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(179, 101, 146), Color(133, 77, 110)
            ),
            darkDefault = LighterDarkerColors(
                Color(163, 88, 130), Color(116, 63, 92)
            )
        ),
        colorOn = ColorByTheme(
            lightDefault = Color(240, 240, 240),
            darkDefault = Color(240, 240, 240)
        )
    )

    data object Blue : AccountColors(
        name = AccountColorName.Blue,
        color = LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(102, 88, 177), Color(83, 71, 143)
            ),
            darkDefault = LighterDarkerColors(
                Color(87, 75, 150), Color(69, 59, 119)
            )
        ),
        colorOn = ColorByTheme(
            lightDefault = Color(240, 240, 240),
            darkDefault = Color(240, 240, 240)
        )
    )

    data object Camel : AccountColors(
        name = AccountColorName.Camel,
        color = LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(190, 134, 93), Color(162, 114, 78)
            ),
            darkDefault = LighterDarkerColors(
                Color(173, 122, 84), Color(139, 98, 67)
            )
        ),
        colorOn = ColorByTheme(
            lightDefault = Color(240, 240, 240),
            darkDefault = Color(240, 240, 240)
        )
    )

    data object Red : AccountColors(
        name = AccountColorName.Red,
        color = LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(206, 78, 78, 255), Color(162, 61, 61, 255)
            ),
            darkDefault = LighterDarkerColors(
                Color(180, 68, 68), Color(138, 52, 52)
            )
        ),
        colorOn = ColorByTheme(
            lightDefault = Color(240, 240, 240),
            darkDefault = Color(240, 240, 240)
        )
    )

    data object Green : AccountColors(
        name = AccountColorName.Green,
        color = LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(91, 194, 136), Color(73, 155, 108)
            ),
            darkDefault = LighterDarkerColors(
                Color(75, 158, 111, 255), Color(56, 119, 83, 255)
            )
        ),
        colorOn = ColorByTheme(
            lightDefault = Color(240, 240, 240),
            darkDefault = Color(240, 240, 240)
        )
    )


    companion object {

        fun fromName(name: String): AccountColors {
            return when (name) {
                AccountColorName.Default.name -> Default
                AccountColorName.Pink.name -> Pink
                AccountColorName.Blue.name -> Blue
                AccountColorName.Camel.name -> Camel
                AccountColorName.Red.name -> Red
                AccountColorName.Green.name -> Green
                else -> Default
            }
        }

        fun asList(): List<AccountColors> {
            return listOf(
                Default,
                Pink,
                Blue,
                Camel,
                Red,
                Green
            )
        }

    }

    fun getNameValue(): String {
        return name.name
    }

    fun getColorAndColorOnByTheme(theme: AppTheme): Pair<LighterDarkerColors, Color> {
        return color.getByTheme(theme) to colorOn.getByTheme(theme)
    }

}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun AccountCardPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        ActiveAccountCardContent(
            account = Account(color = AccountColors.Red),
            todayExpenses = 0.0
        )
    }
}
