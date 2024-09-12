package presentation.screen.composable.table

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import presentation.util.kms
import design_system.theme.Theme

@Composable
fun ArrowIcon(
    painter: Painter,
    contentDescription: String? = null,
    onClick: () -> Unit,
    enable: Boolean = true,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(24.kms)
            .graphicsLayer(
                scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f
            )
            .clickable(enabled = enable) { onClick() },
        tint = if (enable) Theme.colors.contentPrimary else Theme.colors.contentTertiary.copy(alpha = 0.35f)
    )
}