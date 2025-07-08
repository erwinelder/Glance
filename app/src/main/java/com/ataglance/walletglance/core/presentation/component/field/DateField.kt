package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun DateField(
    formattedDate: String,
    onClick: () -> Unit = {}
) {
    FieldWithLabelWrapper(labelText = stringResource(R.string.date)) {
        GlassSurfaceOnGlassSurface(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            cornerSize = 15.dp
        ) {
            Text(
                text = formattedDate,
                color = GlanciColors.onSurface,
                fontSize = 18.sp,
                fontFamily = Manrope,
                fontWeight = FontWeight.W400
            )
        }
    }
}