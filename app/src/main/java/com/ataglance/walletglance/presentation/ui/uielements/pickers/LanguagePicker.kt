package com.ataglance.walletglance.presentation.ui.uielements.pickers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.data.app.AppLanguage
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurface

@Composable
fun LanguagePicker(
    currentLangCode: String?,
    onRadioButton: (String) -> Unit,
    languages: List<AppLanguage> = listOf(
        AppLanguage.Czech,
        AppLanguage.English,
        AppLanguage.German,
        AppLanguage.Spanish,
        AppLanguage.Russian,
        AppLanguage.Ukrainian
    )
) {
    GlassSurface {
        LazyColumn(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(24.dp, 24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = languages, key = { it.languageCode }) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .bounceClickEffect { onRadioButton(it.languageCode) }
                ) {
                    RadioButton(
                        selected = it.languageCode == currentLangCode,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = GlanceTheme.primary,
                            unselectedColor = GlanceTheme.onSurface.copy(.5f)
                        )
                    )
                    Text(
                        text = it.languageNativeName,
                        color = GlanceTheme.onSurface,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}