package com.ataglance.walletglance.core.presentation.component.chart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenPreview
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.statistics.ColumnChartColumnUiState
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.animation.scaleFadeInAnimation
import com.ataglance.walletglance.core.presentation.animation.scaleFadeOutAnimation
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.modifier.innerShadow
import com.ataglance.walletglance.core.presentation.modifier.innerVolumeShadow
import com.ataglance.walletglance.core.utils.formatWithSpaces

@Composable
fun GlanceColumnChart(
    uiState: ColumnChartUiState,
    columnsColor: Color? = null,
    title: String? = null,
    bottomNote: String? = null,
    columnPopupDetailsContent: @Composable (Double) -> Unit = { value ->
        Text(text = value.formatWithSpaces())
    }
) {
    val graphHeight = 250.dp

    GlassSurface(
        filledWidths = FilledWidthByScreenType(1f, .8f, .5f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 20.dp)
        ) {
            title?.let {
                Text(
                    text = it,
                    color = GlanceColors.onSurface,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                LinesNamesColumn(linesNames = uiState.rowsNames, graphHeight = graphHeight)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.height(graphHeight)
                    ) {
                        DashedLinesColumn(uiState.rowsNames.size)
                        ChartColumns(
                            columns = uiState.columns,
                            columnsColor = columnsColor ?: GlanceColors.primary,
                            columnPopupDetailsContent = columnPopupDetailsContent
                        )
                    }
                    ColumnsNamesRow(columnsNames = uiState.columnsNames)
                }
            }
            bottomNote?.let {
                Text(
                    text = it,
                    color = GlanceColors.onSurface,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LinesNamesColumn(linesNames: List<String>, graphHeight: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(graphHeight)
            .padding(horizontal = 4.dp)
    ) {
        linesNames.forEach { name ->
            Text(
                text = name,
                color = GlanceColors.onSurface.copy(.5f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ColumnsNamesRow(columnsNames: List<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        columnsNames.forEach { name ->
            Text(
                text = name,
                color = GlanceColors.onSurface.copy(.5f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun DashedLinesColumn(linesCount: Int) {
    val dashedPathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f))
    val dashedLineColor = GlanceColors.outline.copy(.35f)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 10.dp)
    ) {
        repeat(linesCount) {
            Canvas(
                modifier = Modifier.fillMaxWidth()
            ) {
                drawLine(
                    color = dashedLineColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 3f,
                    pathEffect = dashedPathEffect
                )
            }
        }
    }
}

@Composable
private fun ChartColumns(
    columns: List<ColumnChartColumnUiState>,
    columnsColor: Color,
    columnPopupDetailsContent: @Composable (Double) -> Unit
) {
    var selectedColumnIndex by remember {
        mutableStateOf<Int?>(null)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp)
    ) {
        columns.forEachIndexed { index, chartItemUiState ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                ChartColumnWithPopup(
                    columnIndex = index,
                    chartItemUiState = chartItemUiState,
                    columnsColor = columnsColor,
                    selectedColumnIndex = selectedColumnIndex,
                    onColumnSelect = { selectedColumnIndex = it },
                    columnPopupDetailsContent = columnPopupDetailsContent
                )
            }
        }
    }
}

@Composable
private fun ChartColumnWithPopup(
    columnIndex: Int,
    chartItemUiState: ColumnChartColumnUiState,
    columnsColor: Color,
    selectedColumnIndex: Int?,
    onColumnSelect: (Int?) -> Unit,
    columnPopupDetailsContent: @Composable (Double) -> Unit
) {
    val popupVisibilityState = remember {
        MutableTransitionState(columnIndex == selectedColumnIndex)
    }
    val columnColor by animateColorAsState(
        targetValue = columnsColor.takeIf {
            selectedColumnIndex == null || selectedColumnIndex == columnIndex
        } ?: columnsColor.copy(.5f),
        label = "column chart column $columnIndex color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        ChartColumnPopupDetails(
            visibilityState = popupVisibilityState,
            onDismissRequest = {
                onColumnSelect(null)
                popupVisibilityState.targetState = false
            }
        ) {
            columnPopupDetailsContent(chartItemUiState.value)
        }
        ChartColumn(percentage = chartItemUiState.percentageOnGraph, color = columnColor) {
            popupVisibilityState.targetState = true
            onColumnSelect(columnIndex)
        }
    }
}

@Composable
private fun ChartColumnPopupDetails(
    visibilityState: MutableTransitionState<Boolean>,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visibilityState.currentState || visibilityState.targetState
    ) {
        Popup(
            alignment = Alignment.BottomCenter,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                dismissOnBackPress = false
            )
        ) {
            AnimatedVisibility(
                visibleState = visibilityState,
                enter = scaleFadeInAnimation(),
                exit = scaleFadeOutAnimation()
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .innerVolumeShadow(shape = RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(GlanceColors.surface)
                        .padding(16.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun ChartColumn(
    percentage: Float,
    color: Color,
    onClick: () -> Unit
) {
    Spacer(
        modifier = Modifier
            .bounceClickEffect(onClick = onClick)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50),
                spotColor = color
            )
            .innerShadow(
                shape = RoundedCornerShape(30),
                color = Color.Black.copy(.25f),
                offsetX = 2.dp,
                offsetY = (-2).dp,
                blur = 8.dp,
                spread = 0.dp
            )
            .innerShadow(
                shape = RoundedCornerShape(30),
                color = Color.White.copy(.25f),
                offsetX = (-2).dp,
                offsetY = 2.dp,
                blur = 8.dp,
                spread = 0.dp
            )
            .clip(RoundedCornerShape(50))
            .fillMaxWidth(.45f)
            .fillMaxHeight(percentage)
            .background(color)
    )
}


@Preview(
    device = Devices.PIXEL_7_PRO
)
@Composable
private fun GlanceColumnChartPreview() {
    BudgetStatisticsScreenPreview()
}