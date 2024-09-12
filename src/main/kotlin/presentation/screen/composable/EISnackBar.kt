package presentation.screen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_close
import org.jetbrains.compose.resources.vectorResource
import presentation.util.kms
import design_system.theme.Theme

@Composable
fun SnackBar(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Theme.colors.surface,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = backgroundColor)
            .width(512.kms)
            .border(
                width = 1.kms, color = Theme.colors.divider,
                shape = RoundedCornerShape(Theme.radius.medium),
            )
    ) {
        content()
        Spacer(modifier = Modifier.weight(1f))
        Image(
            imageVector = vectorResource(Res.drawable.ic_close),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = Theme.colors.contentPrimary),
            modifier = Modifier.padding(16.dp).clickable(
                onClick = onDismiss
            )
        )
    }
}