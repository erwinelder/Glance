package com.ataglance.walletglance.errorHandling.presentation.components.containers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.errorHandling.domain.model.TaskResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorMessageBottomSheet(
    taskResult: TaskResult?,
    onTaskResultReset: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    GlanceBottomSheet(
        visible = taskResult != null,
        sheetState = sheetState,
        onDismissRequest = onTaskResultReset
    ) {
        taskResult?.let {
            Icon(
                painter = painterResource(
                    if (taskResult.isSuccessful) R.drawable.setup_finished_icon else
                        R.drawable.setup_finished_icon
                ),
                contentDescription = if (taskResult.isSuccessful) "Success" else "Error",
                tint = if (taskResult.isSuccessful) GlanceTheme.success else GlanceTheme.error
            )
            Text(
                text = stringResource(taskResult.messageRes),
                fontSize = 20.sp,
                color = GlanceTheme.onBackground
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun ErrorMessageBottomSheetPreview() {
    val taskResult = TaskResult(
        isSuccessful = false,
        messageRes = R.string.email_for_password_reset_error
    )

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        ErrorMessageBottomSheet(
            taskResult = taskResult,
            onTaskResultReset = {}
        )
    }
}