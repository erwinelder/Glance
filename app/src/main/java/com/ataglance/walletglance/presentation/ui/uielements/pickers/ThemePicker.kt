package com.ataglance.walletglance.presentation.ui.uielements.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.settings.ThemeUiState
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.uielements.switches.SwitchWithLabel
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurface

@Composable
fun ThemePicker(
    onSetUseDeviceTheme: (Boolean) -> Unit,
    onChooseLightTheme: (String) -> Unit,
    onChooseDarkTheme: (String) -> Unit,
    themeUiState: ThemeUiState
) {
    GlassSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                ThemeReprElement(
                    onClick = { onChooseLightTheme(AppTheme.LightDefault.name) },
                    firstAccountColor = Color(8, 8, 8),
                    primaryColor = Color(177, 100, 145),
                    backgroundColor = Color(240, 240, 240),
                    surfaceColor = Color(247, 247, 247),
                    borderColor = Color(195, 195, 195)
                )
                ThemeReprElement(
                    onClick = { onChooseDarkTheme(AppTheme.DarkDefault.name) },
                    firstAccountColor = Color(235, 235, 235),
                    primaryColor = Color(177, 94, 139),
                    backgroundColor = Color(25, 25, 25),
                    surfaceColor = Color(31, 31, 31),
                    borderColor = Color(40, 40, 40)
                )
            }
            SwitchWithLabel(
                checked = themeUiState.useDeviceTheme,
                onCheckedChange = onSetUseDeviceTheme,
                labelText = stringResource(R.string.use_device_theme)
            )
//            CustomSwitch()
        }
    }
}

@Composable
fun ThemeReprElement(
    onClick: () -> Unit,
    firstAccountColor: Color,
    primaryColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    borderColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .bounceClickEffect(shrinkScale = .98f, onClick = onClick)
            .width(110.dp)
            .clip(RoundedCornerShape(55f))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(55f))
            .padding(horizontal = 10.dp, vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth(.92f)
        ) {
            AccountReprElement(firstAccountColor)
            AccountReprElement(primaryColor)
        }
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(40f))
                .fillMaxWidth(.92f)
                .height(85.dp)
                .background(surfaceColor)
        )
        ButtonReprElement(primaryColor)
    }
}

@Composable
private fun AccountReprElement(background: Color) {
    Spacer(
        modifier = Modifier
            .clip(RoundedCornerShape(20f))
            .background(background)
            .size(32.dp, 13.dp)
    )
}

@Composable
private fun ButtonReprElement(color: Color) {
    Spacer(
        modifier = Modifier
            .clip(RoundedCornerShape(22f))
            .background(color)
            .fillMaxWidth(.92f)
            .size(80.dp, 17.dp)
    )
}