package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun ShowHidePasswordIcon(
    isPasswordVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            painter = painterResource(
                if (isPasswordVisible) R.drawable.hide_icon else R.drawable.show_icon
            ),
            tint = GlanciColors.onSurface,
            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
            modifier = Modifier.Companion.size(24.dp)
        )
    }
}