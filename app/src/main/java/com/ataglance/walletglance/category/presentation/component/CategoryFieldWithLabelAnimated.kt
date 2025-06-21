package com.ataglance.walletglance.category.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabelWrapper

@Composable
fun CategoryFieldWithLabelAnimated(
    category: Category?,
    onClick: () -> Unit
) {
    FieldWithLabelWrapper(stringResource(R.string.category)) {
        AnimatedContent(targetState = category) { targetCategory ->
            CategoryField(category = targetCategory, onClick = onClick)
        }
    }
}