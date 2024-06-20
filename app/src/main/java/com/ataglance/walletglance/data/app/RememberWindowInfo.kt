package com.ataglance.walletglance.data.app

/*@Composable
fun rememberWindowInfo(

) {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo = when {
            configuration.scre
        }
    )
}

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val
) {
    sealed class WindowType {
        object Compact : WindowType()
        object Medium : WindowType()
        object Expanded : WindowType()
    }
}*/

enum class WindowType {
    Compact, Medium, Expanded
}
