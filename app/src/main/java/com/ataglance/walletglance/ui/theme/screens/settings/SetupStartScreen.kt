package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.StartAnimatedContainer
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton

@Composable
fun SetupStartScreen(
    appTheme: AppTheme?,
    onManualSetupButton: () -> Unit,
    onImportDataButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 32.dp)
    ) {
        StartAnimatedContainer(appTheme != null, 200) {
            Text(
                text = "WalletGlance",
                color = GlanceTheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = -(.5).sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        StartAnimatedContainer(appTheme != null) {
            Text(
                text = stringResource(R.string.hello) + "!",
                color = GlanceTheme.onBackground,
                fontSize = 55.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = -(1).sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            /*SecondaryButton(onClick = onImportDataButton, text = stringResource(R.string.import_data))
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.or),
                color = GlanceTheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))*/
            StartAnimatedContainer(appTheme != null, 100) {
                PrimaryButton(
                    onClick = onManualSetupButton,
                    text = stringResource(R.string.start_setup)
                )
            }
        }
    }
}