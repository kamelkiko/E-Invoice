package design_system.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import design_system.theme.Theme

@Composable
fun EIChip(
    label: String,
    isSelected: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter? = null
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.primary else Color.Transparent
    )
    val labelColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.onPrimary
        else Theme.colors.contentSecondary
    )
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.onPrimary
        else Theme.colors.contentSecondary
    )
    AssistChip(
        modifier = modifier.height(40.dp),
        onClick = { onClick(!isSelected) },
        label = { Text(text = label, style = Theme.typography.titleMedium) },
        leadingIcon = {
            painter?.let {
                Icon(
                    painter = painter,
                    contentDescription = "$label icon",
                    Modifier.size(AssistChipDefaults.IconSize),
                    tint = iconColor
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor,
            labelColor = labelColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Theme.colors.divider,
        ),
        shape = RoundedCornerShape(Theme.radius.small)
    )
}