package com.ataglance.walletglance.personalization.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.containers.ReorderableListStyled
import com.ataglance.walletglance.core.presentation.components.dividers.SmallDivider
import com.ataglance.walletglance.core.presentation.components.other.ScreenNameWithIconComponent
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationButtonsSettingsBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    navigationButtonList: List<BottomBarNavigationButton>,
    onMoveButtons: (Int, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    GlanceBottomSheet(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            ScreenNameWithIconComponent(navigationButton = BottomBarNavigationButton.Home)
            SmallDivider(modifier = Modifier.padding(top = 16.dp))
            ReorderableListStyled(
                list = navigationButtonList,
                itemKey = { _, item -> item.screenNameRes },
                onMoveItems = onMoveButtons
            ) { item ->
                ScreenNameWithIconComponent(
                    navigationButton = item,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            SmallDivider(modifier = Modifier.padding(bottom = 16.dp))
            ScreenNameWithIconComponent(navigationButton = BottomBarNavigationButton.Settings)
        }
    }
}
