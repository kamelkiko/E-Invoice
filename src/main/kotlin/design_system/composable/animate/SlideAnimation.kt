package design_system.composable.animate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SlideAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = slideInVertically { it } + fadeIn(tween(600)),
    exit: ExitTransition = slideOutVertically { it } + fadeOut(tween(600)),
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = enter,
        exit = exit,
    ) {
        content()
    }
}