package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsMedium
import com.ataglance.walletglance.ui.theme.modifiers.innerShadow

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    filledWidth: Float? = null,
    cornerSize: Dp = dimensionResource(R.dimen.widget_corner_size),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth(
                filledWidth ?:
                when {
                    WindowTypeIsCompact -> .9f
                    WindowTypeIsMedium -> .67f
                    else -> .44f
                }
            )
            .clip(RoundedCornerShape(cornerSize))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceTheme.glassSurfaceGradient,
                    start = Offset(0f, 1400f),
                    end = Offset(600f, 0f)
                )
            )
            .innerShadow(
                shape = RoundedCornerShape(cornerSize),
                color = GlanceTheme.glassSurfaceLightAndDarkShadow.first,
                offsetX = -(5).dp,
                offsetY = 5.dp,
                blur = 13.dp,
                spread = 3.dp,
            )
            .innerShadow(
                shape = RoundedCornerShape(cornerSize),
                color = GlanceTheme.glassSurfaceLightAndDarkShadow.second,
                offsetX = 5.dp,
                offsetY = -(5).dp,
                blur = 13.dp,
                spread = 3.dp,
            )
            .border(
                2.dp,
                GlanceTheme.glassSurfaceBorder,
                RoundedCornerShape(cornerSize)
            )
    ) { content() }
}

@Preview(showSystemUi = true)
@Composable
private fun GlassSurfacePreview() {
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
        /*AccountCard(
            account = Account(color = AccountColors.Blue(AppTheme.LightDefault).name.name),
            appTheme = AppTheme.LightDefault,
            todayExpenses = 0.0
        )*/
        GlassSurface {
            Box(modifier = Modifier.size(300.dp))
        }
    }

}