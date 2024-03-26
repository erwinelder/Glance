package com.ataglance.walletglance.ui.theme.uielements.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.model.DateRangeController
import com.ataglance.walletglance.model.RecordController
import com.ataglance.walletglance.model.RecordStack
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.ui.theme.GlanceTheme

@Composable
fun RecordStackComponent(
    recordStack: RecordStack,
    includeYearToDate: Boolean,
    getCategoryAndIcon: (Int, Int?, RecordType?) -> Pair<Category?, Int?>?,
    getAccount: (Int) -> Account?,
    onRecordClick: (Int) -> Unit
) {
    val account = getAccount(recordStack.accountId)

    RecordContainer({ onRecordClick(recordStack.recordNum) }) {
        // date
        Text(
            text = DateRangeController().convertDateLongToDayMonthYear(recordStack.date, includeYearToDate),
            color = GlanceTheme.outline,
            fontSize = 16.sp
        )
        // note with category
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            recordStack.stack.take(3).forEach { recordStackUnit ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!recordStackUnit.note.isNullOrEmpty()) {
                        Text(
                            text = recordStackUnit.note,
                            color = GlanceTheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    RecordCategory(
                        categoryAndIconRes = getCategoryAndIcon(
                            recordStackUnit.categoryId,
                            recordStackUnit.subcategoryId,
                            RecordController().recordTypeCharToRecordType(recordStack.type)
                        )
                    )
                }
            }
        }
        // amount
        Text(
            text = recordStack.getFormattedAmountWithSpaces(account?.currency),
            color = GlanceTheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun RecordCategory(categoryAndIconRes: Pair<Category?, Int?>?) {
    categoryAndIconRes?.first?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoryAndIconRes.second?.let {
                Icon(
                    painter = painterResource(categoryAndIconRes.second!!),
                    contentDescription = categoryAndIconRes.first!!.name + " icon",
                    tint = GlanceTheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = categoryAndIconRes.first!!.name,
                color = GlanceTheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}