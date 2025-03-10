# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepnames class com.ataglance.walletglance.core.domain.navigation.MainScreens$*
-keepnames class com.ataglance.walletglance.settings.domain.navigation.SettingsScreens$*
-keepnames class com.ataglance.walletglance.auth.domain.navigation.AuthScreens$*
-keepnames class com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens$*
-keepnames class com.ataglance.walletglance.budget.domain.navigation.BudgetsSettingsScreens$*
-keepnames class com.ataglance.walletglance.category.domain.navigation.CategoriesSettingsScreens$*
-keepnames class com.ataglance.walletglance.categoryCollection.domain.navigation.CategoryCollectionsSettingsScreens$*
