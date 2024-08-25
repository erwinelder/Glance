package com.ataglance.walletglance.core.presentation.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.makingRecord.presentation.components.MakeRecordFieldContainer

@Composable
fun DateField(
    dateFormatted: String,
    cornerSize: Dp = 15.dp,
    onClick: () -> Unit = {}
) {
    MakeRecordFieldContainer(R.string.date) {
        Text(
            text = dateFormatted,
            color = GlanceTheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .bounceClickEffect(.97f, onClick = onClick)
                .clip(RoundedCornerShape(cornerSize))
                .background(GlanceTheme.surface)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}