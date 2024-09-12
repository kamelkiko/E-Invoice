package resource

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import design_system.theme.AppTheme
import resource.strings.IStringResources
import util.LanguageCode
import util.LocalizationManager

private val localStringResources = staticCompositionLocalOf<IStringResources> {
    error("CompositionLocal IStringResources not present")
}

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
internal fun EInvoiceTheme(
    languageCode: LanguageCode,
    content: @Composable () -> Unit
) {
    val systemIsDark = true
    val isDarkState = remember { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState,
        localStringResources provides LocalizationManager.getStringResources(languageCode),
        LocalLayoutDirection provides LocalizationManager.getLayoutDirection(languageCode),
    ) {
        val isDark by isDarkState
        AppTheme(useDarkTheme = isDark, content = content)
    }
}

object Resources {
    val strings: IStringResources
        @Composable
        @ReadOnlyComposable
        get() = localStringResources.current
}