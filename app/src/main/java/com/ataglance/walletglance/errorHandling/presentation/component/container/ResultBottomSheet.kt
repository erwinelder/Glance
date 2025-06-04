package com.ataglance.walletglance.errorHandling.presentation.component.container

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
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetComponent
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetContentDialogComponent
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.errorHandling.presentation.model.ResultTitleWithMessageState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(
    resultTitleWithMessageState: ResultTitleWithMessageState?,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val titleColor = if (resultTitleWithMessageState?.isSuccessful == true) {
        GlanciColors.success
    } else {
        GlanciColors.error
    }
    val iconGradientColor = if (resultTitleWithMessageState?.isSuccessful == true) {
        GlanciColors.successGradient
    } else {
        GlanciColors.errorGradient
    }

    BottomSheetComponent(
        visible = resultTitleWithMessageState != null,
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch { sheetState.hide() }
            onDismissRequest()
        },
        dragHandle = {}
    ) {
        resultTitleWithMessageState?.let {
            BottomSheetContentDialogComponent(
                title = stringResource(resultTitleWithMessageState.titleRes),
                titleColor = titleColor,
                message = resultTitleWithMessageState.messageRes?.let { stringResource(it) },
                iconRes = if (resultTitleWithMessageState.isSuccessful) R.drawable.success_large_icon else
                    R.drawable.error_large_icon,
                iconDescription = if (resultTitleWithMessageState.isSuccessful) "Success" else "Error",
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
    val resultTitleWithMessageState = ResultTitleWithMessageState(
        isSuccessful = true,
        titleRes = R.string.email_sent,
        messageRes = R.string.reset_password_email_sent_message
//        isSuccessful = false,
//        titleRes = R.string.oops,
//        messageRes = R.string.email_for_password_reset_error
    )

    var state by remember { mutableStateOf<ResultTitleWithMessageState?>(resultTitleWithMessageState) }

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SmallPrimaryButton(
            text = "Show error",
            onClick = { state = resultTitleWithMessageState }
        )
        ResultBottomSheet(
            resultTitleWithMessageState = state,
            onDismissRequest = { state = null }
        )
    }
}