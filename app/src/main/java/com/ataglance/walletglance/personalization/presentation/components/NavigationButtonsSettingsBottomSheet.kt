package com.ataglance.walletglance.personalization.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetComponent
import com.ataglance.walletglance.core.presentation.component.container.ReorderableListStyled
import com.ataglance.walletglance.core.presentation.component.divider.SmallDivider
import com.ataglance.walletglance.core.presentation.component.other.ScreenNameWithIconComponent
import com.ataglance.walletglance.navigation.presentation.model.BottomNavBarButtonState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationButtonsSettingsBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    navigationButtonList: List<BottomNavBarButtonState>,
    onMoveButtons: (Int, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    BottomSheetComponent(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            ScreenNameWithIconComponent(navigationButton = BottomNavBarButtonState.Home())
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
            ScreenNameWithIconComponent(navigationButton = BottomNavBarButtonState.Settings())
        }
    }
}
