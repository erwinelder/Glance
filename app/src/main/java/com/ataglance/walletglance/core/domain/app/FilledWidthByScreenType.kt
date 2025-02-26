package com.ataglance.walletglance.core.domain.app

data class FilledWidthByScreenType(
    val compact: Float = .9f,
    val medium: Float = .67f,
    val expanded: Float = .44f
) {

    fun getByType(windowType: WindowType): Float {
        return when (windowType) {
            WindowType.Compact -> compact
            WindowType.Medium -> medium
            WindowType.Expanded -> expanded
        }
    }

}
