package com.ataglance.walletglance.core.presentation.components.screenContainers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface

@Composable
fun GlassSurfaceScreenContainer(
    topPadding: Dp? = null,
    topButton: @Composable (() -> Unit)? = null,
    topBar: @Composable (() -> Unit)? = null,
    glassSurfaceContent: @Composable BoxScope.() -> Unit,
    fillGlassSurface: Boolean = true,
    glassSurfaceFilledWidths: FilledWidthByScreenType = FilledWidthByScreenType(compact = .86f),
    smallPrimaryButton: @Composable (() -> Unit)? = null,
    secondaryBottomButton: @Composable (() -> Unit)? = null,
    primaryBottomButton: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = (if (topButton != null || topBar == null) 24.dp else 16.dp) +
                        (topPadding ?: 0.dp),
                bottom = 24.dp
            )
    ) {

        topButton?.let { it() }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                topBar?.let { it() }
                GlassSurface(
                    modifier = Modifier.weight(1f, fill = fillGlassSurface),
                    filledWidths = glassSurfaceFilledWidths,
                    content = glassSurfaceContent
                )
                smallPrimaryButton?.let { it() }
            }
        }

        if (!WindowTypeIsExpanded) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                secondaryBottomButton?.let { it() }
                primaryBottomButton()
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                secondaryBottomButton?.let { it() }
                Spacer(modifier = Modifier.width(16.dp))
                primaryBottomButton()
            }
        }

    }
}