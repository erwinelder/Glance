package com.ataglance.walletglance.errorHandling.presentation.components.containers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheetContentDialog
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(
    resultState: ResultUiState?,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val titleColor = if (resultState?.isSuccessful == true) {
        GlanceTheme.success
    } else {
        GlanceTheme.error
    }
    val iconGradientColor = if (resultState?.isSuccessful == true) {
        GlanceTheme.successGradient
    } else {
        GlanceTheme.errorGradient
    }

    GlanceBottomSheet(
        visible = resultState != null,
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch { sheetState.hide() }
            onDismissRequest()
        },
        dragHandle = {}
    ) {
        resultState?.let {
            GlanceBottomSheetContentDialog(
                title = stringResource(resultState.titleRes),
                titleColor = titleColor,
                message = stringResource(resultState.messageRes),
                iconRes = if (resultState.isSuccessful) R.drawable.success_icon else
                    R.drawable.error_icon,
                iconDescription = if (resultState.isSuccessful) "Success" else "Error",
                iconGradientColor = iconGradientColor
            ) {
                SecondaryButton(
                    text = stringResource(R.string.close),
                    onClick = {
                        coroutineScope.launch { sheetState.hide() }
                        onDismissRequest()
                    }
                )
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun ResultBottomSheetPreview() {
    val resultState = ResultUiState(
        isSuccessful = true,
        titleRes = R.string.email_sent,
        messageRes = R.string.reset_password_email_sent
//        isSuccessful = false,
//        titleRes = R.string.oops,
//        messageRes = R.string.email_for_password_reset_error
    )

    var state by remember { mutableStateOf<ResultUiState?>(resultState) }

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SmallPrimaryButton(
            text = "Show error",
            onClick = { state = resultState }
        )
        ResultBottomSheet(
            resultState = state,
            onDismissRequest = { state = null }
        )
    }
}