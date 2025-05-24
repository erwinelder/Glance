package com.ataglance.walletglance.notification.presentation.screen

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.bottomSheet.GlanceBottomSheetDialog
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.switchButton.SwitchWithLabel
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.notification.presentation.viewmodel.NotificationsViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreenWrapper(
    navController: NavController
) {
    val viewModel = koinViewModel<NotificationsViewModel>()

    val dailyRecordsReminderTurnedOn by viewModel.dailyRecordsReminderTurnedOn.collectAsStateWithLifecycle()
    val showNotificationsDeniedDialog by viewModel.showNotificationsDeniedDialog.collectAsStateWithLifecycle()

    NotificationsScreen(
        onNavigateBack = navController::popBackStack,
        dailyRecordsReminderTurnedOn = dailyRecordsReminderTurnedOn,
        onSetDailyRecordReminder = viewModel::setDailyRecordsReminder,
        checkNotificationPermission = viewModel::checkNotificationPermission,
        showNotificationsDeniedDialog = showNotificationsDeniedDialog,
        onSetShowNotificationsDeniedDialog = viewModel::setShowNotificationsDeniedDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit,
    dailyRecordsReminderTurnedOn: Boolean,
    onSetDailyRecordReminder: (Boolean) -> Unit,
    checkNotificationPermission: () -> Boolean,
    showNotificationsDeniedDialog: Boolean,
    onSetShowNotificationsDeniedDialog: (Boolean) -> Unit
) {
    val appTheme = CurrAppTheme
    val thisCategory = remember {
        SettingsCategory.Notifications(appTheme)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onSetDailyRecordReminder(true)
        } else {
            onSetShowNotificationsDeniedDialog(true)
        }
    }

    Box {
        SettingsCategoryScreenContainer(
            thisCategory = thisCategory,
            onNavigateBack = onNavigateBack,
            title = stringResource(R.string.adjust_app_notifications),
            mainScreenContentBlock = {
                NotificationsScreenContent(
                    dailyRecordsReminderTurnedOn = dailyRecordsReminderTurnedOn,
                    onSetDailyRecordReminder = { turnOn ->
                        if (turnOn) {
                            if (checkNotificationPermission()) {
                                onSetDailyRecordReminder(true)
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        } else {
                            onSetDailyRecordReminder(false)
                        }
                    }
                )
            },
            allowScroll = false
        )

        GlanceBottomSheetDialog(
            visible = showNotificationsDeniedDialog,
            iconRes = R.drawable.notifications_large_icon,
            iconDescription = "Notifications denied",
            title = stringResource(R.string.notifications_denied_title),
            message = stringResource(R.string.notifications_denied_message),
            onDismissRequest = { onSetShowNotificationsDeniedDialog(false) }
        ) { onSheetHide ->
            SecondaryButton(
                text = stringResource(R.string.close),
                onClick = {
                    onSheetHide()
                    onSetShowNotificationsDeniedDialog(false)
                }
            )
        }
    }
}

@Composable
private fun NotificationsScreenContent(
    dailyRecordsReminderTurnedOn: Boolean,
    onSetDailyRecordReminder: (Boolean) -> Unit
) {
    GlassSurface {
        GlassSurfaceContentColumnWrapper {
            SwitchWithLabel(
                checked = dailyRecordsReminderTurnedOn,
                onCheckedChange = onSetDailyRecordReminder,
                labelText = stringResource(R.string.daily_reminders_to_record_expenses)
            )
        }
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun NotificationsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isBottomBarVisible: Boolean = false
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible
    ) {
        NotificationsScreen(
            onNavigateBack = {},
            dailyRecordsReminderTurnedOn = false,
            onSetDailyRecordReminder = {},
            checkNotificationPermission = { true },
            showNotificationsDeniedDialog = false,
            onSetShowNotificationsDeniedDialog = {}
        )
    }
}