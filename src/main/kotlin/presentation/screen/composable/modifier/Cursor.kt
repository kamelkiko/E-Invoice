package presentation.screen.composable.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Stable
fun Modifier.cursorHoverIconHand() =
    then(
        pointerHoverIcon(
            PointerIcon.Hand
        )
    )