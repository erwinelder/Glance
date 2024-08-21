package com.ataglance.walletglance.ui.theme.uielements.pickers

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.animation.popupEnterToBottomAnimation
import com.ataglance.walletglance.ui.theme.animation.popupExitToTopAnimation
import com.ataglance.walletglance.ui.theme.uielements.dividers.SmallDivider

@Composable
fun <T>PopupFloatingPicker(
    selectedItemText: String,
    itemList: List<T>,
    itemToString: (T) -> String,
    onItemSelect: (T) -> Unit,
) {
    val isExpandedState = remember {
        MutableTransitionState(false)
    }
    val selectedColor by animateColorAsState(
        targetValue = if (isExpandedState.targetState)
            GlanceTheme.primary else GlanceTheme.onSurface,
        label = "selected color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        PickerButton(
            text = selectedItemText,
            isExpanded = isExpandedState.targetState,
            selectedColor = selectedColor
        ) {
            isExpandedState.targetState = true
        }
        Column {
            if (isExpandedState.targetState || isExpandedState.currentState) {
                Popup(
                    alignment = Alignment.TopCenter,
                    onDismissRequest = {
                        isExpandedState.targetState = false
                    },
                    properties = PopupProperties(
                        focusable = true
                    )
                ) {
                    AnimatedVisibility(
                        visibleState = isExpandedState,
                        enter = popupEnterToBottomAnimation,
                        exit = popupExitToTopAnimation
                    ) {
                        PopupContent(
                            selectedItemText = selectedItemText,
                            itemList = itemList,
                            itemToString = itemToString,
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
    onClick: () -> Unit
) {
    val scaleY by animateFloatAsState(
        targetValue = if (isExpanded) -1F else 1F,
        label = "expanded scaleY"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bounceClickEffect(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .background(GlanceTheme.surface)
            .padding(16.dp, 8.dp)
    ) {
        AnimatedContent(
            targetState = text,
            label = "selected item text"
        ) { targetText ->
            Text(
                text = targetText,
                fontSize = 20.sp,
                color = selectedColor,
                fontWeight = FontWeight.Light
            )
        }
        Icon(
            painter = painterResource(R.drawable.short_arrow_down_icon),
            contentDescription = "expanded collection list icon",
            tint = selectedColor,
            modifier = Modifier
                .scale(scaleX = 1F, scaleY = scaleY)
                .width(20.dp)
        )
    }
}

@Composable
private fun <T>PopupContent(
    selectedItemText: String,
    itemToString: (T) -> String,
    itemList: List<T>,
    onItemSelect: (T) -> Unit,
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
            .background(GlanceTheme.surface)
    ) {
        itemsIndexed(items = itemList) { index, item ->
            val itemText = itemToString(item)

            if (index != 0) {
                SmallDivider(
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = GlanceTheme.outline.copy(.5f)
                )
            }
            Text(
                text = itemText,
                color = if (itemText == selectedItemText) GlanceTheme.primary
                    else GlanceTheme.onSurface,
                fontSize = 19.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.bounceClickEffect {
                    onItemSelect(item)
                }
            )
        }
    }
}