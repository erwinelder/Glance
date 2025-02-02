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
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.utils.convertDateLongToDayMonthYear
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.RecordType

@Composable
fun RecordStackComponent(
    recordStack: RecordStack,
    includeYearToDate: Boolean,
    onRecordClick: (Int) -> Unit
) {
    GlassSurfaceOnGlassSurface(onClick = { onRecordClick(recordStack.recordNum) }) {
        // date
        Text(
            text = convertDateLongToDayMonthYear(
                date = recordStack.date,
                context = LocalContext.current,
                includeYear = includeYearToDate
            ),
            color = GlanceTheme.outline,
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
                            color = GlanceTheme.onSurface.copy(.8f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    RecordCategory(
                        category = recordStackItem.categoryWithSubcategory
                            ?.getSubcategoryOrCategory()
                    )
                }
            }
        }
        // amount
        Text(
            text = recordStack.getFormattedAmountWithSpaces(),
            color = GlanceTheme.onSurface,
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
        date = getTodayDateLong(),
        type = RecordType.Expense,
        account = Account().toRecordAccount(),
        totalAmount = 516.41,
        stack = listOf(
            RecordStackItem(
                amount = 0.0,
                quantity = null,
                categoryWithSubcategory = defaultCategories.expense[0].getWithSubcategoryWithId(13),
                note = "some note note note",
                includeInBudgets = true
            ),
            RecordStackItem(
                amount = 0.0,
                quantity = null,
                categoryWithSubcategory = defaultCategories.expense[1].getWithSubcategoryWithId(16),
                note = "some note note note",
                includeInBudgets = true
            ),
        )
    )

    PreviewContainer {
        RecordStackComponent(
            recordStack = recordStack,
            includeYearToDate = false,
            onRecordClick = {}
        )
    }
}
