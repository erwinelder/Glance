package com.ataglance.walletglance.record.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.RecordCategory
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.utils.formatDateLongAsDayMonthYear
import com.ataglance.walletglance.core.utils.getCurrentDateLong
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType

@Composable
fun RecordStackComponent(
    recordStack: RecordStack,
    includeYearInDate: Boolean,
    resourceManager: ResourceManager,
    onRecordClick: (Int) -> Unit
) {
    GlassSurfaceOnGlassSurface(onClick = { onRecordClick(recordStack.recordNum) }) {
        // date
        Text(
            text = recordStack.date.formatDateLongAsDayMonthYear(
                resourceManager = resourceManager,
                includeYear = includeYearInDate
            ),
            color = GlanceColors.outline,
            fontSize = 16.sp
        )
        // note with category
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            recordStack.stack.forEach { recordStackItem ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!recordStackItem.note.isNullOrEmpty()) {
                        Text(
                            text = recordStackItem.note,
                            color = GlanceColors.onSurface.copy(.8f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    RecordCategory(
                        category = recordStackItem.categoryWithSub
                            ?.getSubcategoryOrCategory()
                    )
                }
            }
        }
        // amount
        Text(
            text = recordStack.getFormattedAmountWithSpaces(),
            color = GlanceColors.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light
        )
    }
}



@Preview
@Composable
private fun RecordStackComponentPreview() {
    val defaultCategories = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
    val recordStack = RecordStack(
        recordNum = 1,
        date = getCurrentDateLong(),
        type = RecordType.Expense,
        account = Account().toRecordAccount(),
        totalAmount = 516.41,
        stack = listOf(
            RecordStackItem(
                amount = 0.0,
                quantity = null,
                categoryWithSub = defaultCategories.expense[0].getWithSubcategoryWithId(13),
                note = "some note note note",
                includeInBudgets = true
            ),
            RecordStackItem(
                amount = 0.0,
                quantity = null,
                categoryWithSub = defaultCategories.expense[1].getWithSubcategoryWithId(16),
                note = "some note note note",
                includeInBudgets = true
            ),
        )
    )

    PreviewContainer {
        RecordStackComponent(
            recordStack = recordStack,
            includeYearInDate = false,
            resourceManager = ResourceManagerImpl(LocalContext.current),
            onRecordClick = {}
        )
    }
}
