package com.ataglance.walletglance.billing.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer

@Composable
fun SubscriptionScreen(

) {

}



@Preview
@Composable
fun PreviewSubscriptionScreen(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SubscriptionScreen()
    }
}
