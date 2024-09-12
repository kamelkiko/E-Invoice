package design_system.composable.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.bounceClick(
    enabled: Boolean = true,
    scaleDown: Float = 0.9f,
    onClick: () -> Unit
): Modifier = composed {
    if (enabled) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val animatable = remember {
            Animatable(1f)
        }

        LaunchedEffect(key1 = isPressed) {
            if (isPressed) animatable.animateTo(scaleDown)
            else animatable.animateTo(1f)
        }

        Modifier.graphicsLayer {
            val scale = animatable.value
            scaleX = scale
            scaleY = scale
        }.noRippleEffect(onClick = onClick, interactionSource = interactionSource)
    } else this
}