package com.ataglance.walletglance.presentation.ui.uielements.fields

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
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect

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