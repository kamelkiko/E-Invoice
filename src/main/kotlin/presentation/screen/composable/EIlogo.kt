package presentation.screen.composable

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun EILogo(expanded: Boolean, modifier: Modifier = Modifier) {
    Box(modifier) {
        Crossfade(expanded) { targetState ->
            Image(
                painterResource(
                    if (targetState) Res.drawable.logo else Res.drawable.logo
                ),
                contentDescription = null,
                alignment = Alignment.CenterStart,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}