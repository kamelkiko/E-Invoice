package presentation.screen.composable.table

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import presentation.util.kms
import design_system.theme.Theme

@Composable
fun EIToggleableTextButton(
    text: String,
    onSelectChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 32.kms,
    selected: Boolean = false,
    textStyle: TextStyle = Theme.typography.body,
    shape: Shape = RoundedCornerShape(Theme.radius.small),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    AnimatedContent(
        targetState = selected,
        modifier = modifier,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        Text(
            text = text,
            style = textStyle.copy(if (it) Theme.colors.onPrimary else Theme.colors.contentTertiary),
            modifier = Modifier.size(size).clip(shape)
                .background(if (it) Theme.colors.primary else Color.Transparent)
                .padding(top = 4.kms)
                .clickable(interactionSource, null) { onSelectChange(!selected) },
            textAlign = TextAlign.Center,
        )
    }
}