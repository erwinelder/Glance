package com.ataglance.walletglance.record.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.category.presentation.component.RecordCategory
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.transaction.presentation.component.RecentRecordsWidgetPreview
import com.ataglance.walletglance.transaction.presentation.model.RecordUiState
import com.ataglance.walletglance.transaction.presentation.screen.RecordsScreenPreview

@Composable
fun RecordGlassComponent(
    uiState: RecordUiState,
    onRecordClick: (Long) -> Unit
) {
    GlassSurface(
        filledWidths = null,
        cornerSize = 26.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.bounceClickEffect(.98f) {
            onRecordClick(uiState.id)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RecordComponentContent(uiState = uiState)
        }
    }
}

@Composable
fun RecordOnGlassComponent(
    uiState: RecordUiState,
    onRecordClick: (Long) -> Unit
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onRecordClick(uiState.id) }
    ) {
        RecordComponentContent(uiState = uiState)
    }
}

@Composable
private fun RecordComponentContent(
    uiState: RecordUiState
) {
    // date
    Text(
        text = uiState.date,
        color = GlanciColors.outline,
        fontSize = 16.sp,
        fontFamily = Manrope
    )
    // category with note
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        uiState.items.forEach { recordStackItem ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RecordCategory(
                    category = recordStackItem.categoryWithSub?.getSubcategoryOrCategory()
                )
                if (!recordStackItem.note.isNullOrEmpty()) {
                    Text(
                        text = ": ",
                        color = GlanciColors.onSurface,
                        fontSize = 18.sp,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = recordStackItem.note,
                        color = GlanciColors.onSurface.copy(.8f),
                        fontSize = 16.sp,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
        }
    }
    // amount
    Text(
        text = uiState.totalAmount,
        color = GlanciColors.onSurface,
        fontSize = 20.sp,
        fontFamily = Manrope,
        fontWeight = FontWeight.Light
    )
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun RecordGlassComponentPreview() {
    RecordsScreenPreview()
}

@Preview(device = PIXEL_7_PRO)
@Composable
private fun RecordOnGlassComponentPreview() {
    RecentRecordsWidgetPreview()
}
