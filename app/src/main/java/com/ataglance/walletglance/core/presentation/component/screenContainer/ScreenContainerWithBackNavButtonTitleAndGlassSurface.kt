package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilitySpacer
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.NotoSans
import com.ataglance.walletglance.core.presentation.utils.getImeBottomInset

@Composable
fun ScreenContainerWithBackNavButtonTitleAndGlassSurface(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    backButtonText: String,
    @DrawableRes backButtonImageRes: Int? = null,
    title: String,
    glassSurfaceContent: @Composable BoxScope.() -> Unit,
    fillGlassSurface: Boolean = false,
    glassSurfaceFilledWidths: FilledWidthByScreenType = FilledWidthByScreenType(compact = .86f),
    buttonBlockUnderGlassSurface: @Composable (() -> Unit)? = null,
    bottomButtonBlock: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val imeBottomInset by getImeBottomInset()
    val bottomPadding by animateDpAsState(imeBottomInset.coerceAtLeast(24.dp))
    val keyboardInFocus = bottomPadding > 50.dp

    ScreenContainer(
        screenPadding = screenPadding,
        padding = PaddingValues(top = 8.dp, bottom = bottomPadding),
        modifier = Modifier.clickable { focusManager.clearFocus() }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = fillGlassSurface)
        ) {
            KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
                GlassSurfaceTopNavButtonBlock(
                    text = backButtonText,
                    imageRes = backButtonImageRes,
                    filledWidths = FilledWidthByScreenType(.96f, .75f, .54f),
                    onClick = onNavigateBack
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
                Text(
                    text = title,
                    style = GlanciTypography.titleLarge,
                    color = GlanciColors.onSurface,
                    fontFamily = NotoSans,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth(FilledWidthByScreenType(compact = .86f)
                            .getByType(CurrWindowType))
                        .padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GlassSurface(
                    modifier = Modifier.weight(1f, fill = fillGlassSurface),
                    filledWidths = glassSurfaceFilledWidths,
                    content = glassSurfaceContent
                )
                buttonBlockUnderGlassSurface?.invoke()
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        KeyboardTypingAnimatedVisibilitySpacer(isVisible = !keyboardInFocus, height = 16.dp)

        KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
            bottomButtonBlock()
        }

    }
}