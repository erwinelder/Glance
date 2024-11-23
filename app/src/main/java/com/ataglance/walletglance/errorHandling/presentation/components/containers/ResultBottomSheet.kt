package com.ataglance.walletglance.errorHandling.presentation.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.NotoSans
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.other.IconWithBackground
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(
    resultState: ResultUiState?,
    onSheetClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val solidColor = if (resultState?.isSuccessful == true) {
        GlanceTheme.success
    } else {
        GlanceTheme.error
    }
    val gradientColor = if (resultState?.isSuccessful == true) {
        GlanceTheme.successGradient
    } else {
        GlanceTheme.errorGradient
    }

    GlanceBottomSheet(
        visible = resultState != null,
        sheetState = sheetState,
        onDismissRequest = onSheetClose,
        dragHandle = {}
    ) {
        resultState?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                IconWithBackground(
                    iconRes = if (resultState.isSuccessful) R.drawable.success_icon else
                        R.drawable.error_icon,
                    backgroundGradient = gradientColor,
                    iconDescription = if (resultState.isSuccessful) "Success" else "Error"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(.9f)
                ) {
                    Text(
                        text = stringResource(resultState.titleRes),
                        fontSize = 28.sp,
                        color = solidColor,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        fontFamily = NotoSans,
                        lineHeight = 32.sp
                    )
                    Text(
                        text = stringResource(resultState.messageRes),
                        fontSize = 20.sp,
                        color = GlanceTheme.onBackground,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontFamily = Manrope,
                        lineHeight = 32.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                SecondaryButton(
                    text = stringResource(R.string.close),
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            onSheetClose()
                        }
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
            onSheetClose = { state = null }
        )
    }
}