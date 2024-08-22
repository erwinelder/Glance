package com.ataglance.walletglance.presentation.theme.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.presentation.theme.GlanceTheme
import com.ataglance.walletglance.presentation.theme.WalletGlanceTheme
import com.ataglance.walletglance.presentation.theme.animation.StartAnimatedContainer
import com.ataglance.walletglance.presentation.theme.animation.bounceClickEffect
import com.ataglance.walletglance.data.app.AppTheme

@Composable
fun SetupStartScreen(
    appTheme: AppTheme?,
    onManualSetupButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp, bottom = 50.dp)
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
            StartAnimatedContainer(appTheme != null, 100) {
                StartButton(onManualSetupButton)
                /*PrimaryButton(
                    onClick = onManualSetupButton,
                    text = stringResource(R.string.start_setup)
                )*/
            }
        }
    }
}

@Composable
private fun StartButton(onClick: () -> Unit = {}) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.bounceClickEffect(.97f)
    ) {
        StartButtonShadow(GlanceTheme.primaryGradientLightToDark.first)
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceTheme.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = GlanceTheme.onPrimary,
            ),
            contentPadding = PaddingValues(22.dp),
            modifier = Modifier
                .rotate(-45f)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GlanceTheme.primaryGradientLightToDark.second,
                            GlanceTheme.primaryGradientLightToDark.first
                        ),
                        start = Offset(-25f, 101f),
                        end = Offset(124f, 30f)
                    )
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.long_right_arrow_rotated_icon),
                contentDescription = "start setup button icon",
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Composable
private fun StartButtonShadow(color: Color) {
    Spacer(
        modifier = Modifier
            .offset(y = -(5).dp)
            .width(24.dp)
            .height(24.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(dimensionResource(R.dimen.large_button_corners)),
                spotColor = color,
                ambientColor = Color.Transparent
            )
    )
}

@Composable
@Preview
fun SetupStartScreenPreview() {
    BoxWithConstraints {
        WalletGlanceTheme(
            useDeviceTheme = false,
            lastChosenTheme = AppTheme.LightDefault.name,
            boxWithConstraintsScope = this
        ) {
            Image(
                painter = painterResource(R.drawable.main_background_light),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            SetupStartScreen(
                appTheme = AppTheme.LightDefault,
                onManualSetupButton = {}
            )
        }
    }
}
