package com.ataglance.walletglance.categoryCollection.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.R
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun RowScope.CategoryCollectionPicker(
    collectionList: List<CategoryCollectionWithIds>,
    selectedCollection: CategoryCollectionWithIds,
    onCollectionSelect: (Int) -> Unit,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    val selectedColor by animateColorAsState(
        targetValue = if (expandedState.targetState) GlanciColors.primary else GlanciColors.onSurface
    )

    Column(
        modifier = Modifier.weight(1f, fill = false)
    ) {
        PickerButton(
            selectedCollection = selectedCollection,
            expanded = expandedState.targetState,
            selectedColor = selectedColor
        ) {
            onDimBackgroundChange(it)
            expandedState.targetState = it
        }
        Box {
            if (expandedState.targetState || expandedState.currentState || !expandedState.isIdle) {
                Popup(
                    properties = PopupProperties(
                        focusable = true
                    ),
                    onDismissRequest = {
                        onDimBackgroundChange(false)
                        expandedState.targetState = false
                    }
                ) {
                    PopupContent(
                        collectionList = collectionList,
                        selectedCollection = selectedCollection,
                        onCollectionSelect = onCollectionSelect,
                        expandedState = expandedState,
                        onExpandedChange = {
                            onDimBackgroundChange(it)
                            expandedState.targetState = it
                        },
                        selectedColor = selectedColor,
                        onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen
                    )
                }
            }
        }
    }
}

@Composable
private fun PickerButton(
    selectedCollection: CategoryCollectionWithIds,
    expanded: Boolean,
    selectedColor: Color,
    onExpandedChange: (Boolean) -> Unit
) {
    val scaleY by animateFloatAsState(
        targetValue = if (expanded) -1F else 1F,
        label = "expanded scaleY"
    )

    Button(
        onClick = {
            onExpandedChange(true)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier
            .bounceClickEffect()
            .border(1.dp, GlanciColors.glassGradientOnGlassBorder, RoundedCornerShape(40))
            .clip(RoundedCornerShape(40))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanciColors.glassButtonGradient.reversed(),
                    start = Offset(75f, 210f),
                    end = Offset(95f, -10f)
                )
            )
    ) {
        AnimatedContent(
            targetState = selectedCollection.name,
            label = "collection name",
            modifier = Modifier.weight(1f, fill = false)
        ) { collectionName ->
            Text(
                text = collectionName,
                color = selectedColor,
                fontSize = 17.sp,
                fontFamily = Manrope,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
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
private fun PopupContent(
    collectionList: List<CategoryCollectionWithIds>,
    selectedCollection: CategoryCollectionWithIds,
    onCollectionSelect: (Int) -> Unit,
    expandedState: MutableTransitionState<Boolean>,
    onExpandedChange: (Boolean) -> Unit,
    selectedColor: Color,
    onNavigateToEditCollectionsScreen: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val itemAppearanceAnimSpeed = 300 / (collectionList.size
        .takeIf { it != 0 } ?: 1)
        .let { it.takeUnless { it == 1 } ?: 2 }
    val itemAppearanceAnimationFloat: (Int) -> FiniteAnimationSpec<Float> = { orderNum ->
        tween(
            durationMillis = 300,
            delayMillis = itemAppearanceAnimSpeed * orderNum
        )
    }
    val itemAppearanceAnimationOffset: (Int) -> FiniteAnimationSpec<IntOffset> = { orderNum ->
        tween(
            durationMillis = 300,
            delayMillis = itemAppearanceAnimSpeed * orderNum
        )
    }

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.screen_horizontal_padding),
            vertical = 12.dp
        )
    ) {
        items(
            items = collectionList,
            key = { it.id }
        ) { collection ->
            AnimatedVisibility(
                visibleState = expandedState,
                enter = fadeIn(itemAppearanceAnimationFloat(collection.orderNum)) +
                        scaleIn(itemAppearanceAnimationFloat(collection.orderNum)) +
                        slideInVertically(
                            animationSpec = itemAppearanceAnimationOffset(collection.orderNum),
                            initialOffsetY = { -it }
                        ),
                exit = fadeOut(tween(300)) +
                        scaleOut(tween(300)) +
                        slideOutVertically(
                            animationSpec = tween(300),
                            targetOffsetY = { -it }
                        )
            ) {
                Text(
                    text = collection.name,
                    color = GlanciColors.onSurface.takeUnless {
                        collection.name == selectedCollection.name
                    } ?: selectedColor,
                    fontSize = 20.sp,
                    fontFamily = Manrope,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .bounceClickEffect {
                            onExpandedChange(false)
                            onCollectionSelect(collection.id)
                        }
                        .shadow(
                            elevation = 0.dp,
                            shape = RoundedCornerShape(40)
                        )
                        .clip(RoundedCornerShape(40))
                        .background(
                            brush = Brush.linearGradient(
                                colors = GlanciColors.surfaceGradient,
                                start = Offset(75f, 210f),
                                end = Offset(95f, -10f)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        item {
            if (collectionList.size == 1) {
                AnimatedVisibility(
                    visibleState = expandedState,
                    enter = fadeIn(itemAppearanceAnimationFloat(2)) +
                            scaleIn(itemAppearanceAnimationFloat(2)) +
                            slideInVertically(
                                animationSpec = itemAppearanceAnimationOffset(2),
                                initialOffsetY = { it / 2 }
                            ),
                    exit = fadeOut(tween(300)) +
                            scaleOut(tween(300)) +
                            slideOutVertically(
                                animationSpec = tween(300),
                                targetOffsetY = { it / 2 }
                            )
                ) {
                    SmallPrimaryButton(text = stringResource(R.string.add_collection)) {
                        onExpandedChange(false)
                        onNavigateToEditCollectionsScreen()
                    }
                }
            }
        }
    }
}



@Preview
@Composable
private fun Preview() {
    val collectionList = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Expense,
            name = "Collection 1",
            categoriesIds = listOf(1, 2, 3)
        ),
        CategoryCollectionWithIds(
            id = 2,
            orderNum = 2,
            type = CategoryCollectionType.Expense,
            name = "Collection 2",
            categoriesIds = listOf(1, 2, 3)
        ),
        CategoryCollectionWithIds(
            id = 3,
            orderNum = 3,
            type = CategoryCollectionType.Expense,
            name = "Collection 3",
            categoriesIds = listOf(1, 2, 3)
        ),
    )

    PreviewContainer {
        Row {
            CategoryCollectionPicker(
                collectionList = collectionList,
                selectedCollection = collectionList[0],
                onCollectionSelect = {},
                onDimBackgroundChange = {},
                onNavigateToEditCollectionsScreen = {}
            )
        }
    }
}