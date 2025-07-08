package com.ataglance.walletglance.repeatedRecords.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer

@Composable
fun RepeatedRecordComponent() {

}


@Preview(device = PIXEL_7_PRO)
@Composable
private fun RepeatedRecordComponentPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        RepeatedRecordComponent()
    }
}