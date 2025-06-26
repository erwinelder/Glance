package com.ataglance.walletglance.core.presentation.component.picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.animation.scaleFadeInAnimation
import com.ataglance.walletglance.core.presentation.animation.scaleFadeOutAnimation
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.component.divider.SmallDivider
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun <T> PopupPicker(
    selectedItem: T,
    itemList: List<T>,
    itemToStringMapper: @Composable (T) -> String,
    onItemSelect: (T) -> Unit,
    outerPadding: PaddingValues = PaddingValues(),
    fontSize: TextUnit = 19.sp,
    buttonGradientColor: Pair<Color, Color> = GlanciColors.glassGradientPair,
    enabled: Boolean = true
) {
    val isExpandedState = remember { MutableTransitionState(false) }
    val selectedColor by animateColorAsState(
        targetValue = if (isExpandedState.targetState)
            GlanciColors.primary else GlanciColors.onSurface
    )
    val buttonLighterColor by animateColorAsState(targetValue = buttonGradientColor.first)
    val buttonDarkerColor by animateColorAsState(targetValue = buttonGradientColor.second)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(outerPadding)
    ) {
        PickerButton(
            text = itemToStringMapper(selectedItem),
            isExpanded = isExpandedState.targetState,
            selectedColor = selectedColor,
            fontSize = fontSize,
            gradientColor = listOf(buttonDarkerColor, buttonLighterColor),
            enabled = enabled
        ) {
            isExpandedState.targetState = true
        }
        Column {
            if (isExpandedState.targetState || isExpandedState.currentState) {
                Popup(
                    alignment = Alignment.TopCenter,
                    onDismissRequest = { isExpandedState.targetState = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    AnimatedVisibility(
                        visibleState = isExpandedState,
                        enter = scaleFadeInAnimation(TransformOrigin(.5f, 0f)),
                        exit = scaleFadeOutAnimation(TransformOrigin(.5f, 0f))
                    ) {
                        PopupContent(
                            selectedItem = selectedItem,
                            itemList = itemList,
                            itemToStringMapper = itemToStringMapper,
                            onItemSelect = {
                                onItemSelect(it)
                                isExpandedState.targetState = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PickerButton(
    text: String,
    isExpanded: Boolean,
    selectedColor: Color,
    fontSize: TextUnit = 19.sp,
    gradientColor: List<Color> = GlanciColors.glassGradientOnGlass,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val scaleY by animateFloatAsState(targetValue = if (isExpanded) -1F else 1F)

    GlassSurfaceOnGlassSurface(
        onClick = onClick,
        clickEnabled = enabled,
        cornerSize = 16.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        gradientColor = gradientColor
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(
                targetState = text,
                modifier = Modifier.weight(1f, fill = false)
            ) { targetText ->
                Text(
                    text = targetText,
                    color = selectedColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.W400,
                    fontFamily = Manrope,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Icon(
                painter = painterResource(R.drawable.short_arrow_down_icon),
                contentDescription = "expanded picker list icon",
                tint = selectedColor,
                modifier = Modifier
                    .scale(scaleX = 1F, scaleY = scaleY)
                    .width(18.dp)
            )
        }
    }
}

@Composable
private fun <T> PopupContent(
    selectedItem: T,
    itemToStringMapper: @Composable (T) -> String,
    itemList: List<T>,
    onItemSelect: (T) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp, 12.dp),
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .background(GlanciColors.surface)
    ) {
        itemsIndexed(items = itemList) { index, item ->
            val itemText = itemToStringMapper(item)

            if (index != 0) {
                SmallDivider(modifier = Modifier.padding(bottom = 8.dp))
            }
            Text(
                text = itemText,
                color = if (itemText == itemToStringMapper(selectedItem))
                    GlanciColors.primary else GlanciColors.onSurface,
                fontSize = 19.sp,
                fontWeight = FontWeight.W400,
                fontFamily = Manrope,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .bounceClickEffect {
                        onItemSelect(item)
                    }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun PopupPickerPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewContainer(appTheme = appTheme) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PickerButton(
                text = "Selected Item",
                isExpanded = true,
                selectedColor = GlanciColors.onSurface,
                onClick = {}
            )
            PopupContent(
                selectedItem = "Selected Item",
                itemList = listOf(
                    "Item 1",
                    "Item 2 Item 2 Item 2",
                    "Item 3",
                    "Item 4"
                ),
                itemToStringMapper = { it },
                onItemSelect = {}
            )
        }
    }
}
