package design_system.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import design_system.composable.modifier.noRippleEffect
import design_system.composable.utils.debouncedClick
import design_system.theme.Theme
import design_system.theme.Theme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EIAppBar(
    modifier: Modifier = Modifier,
    onNavigateUp: (() -> Unit)? = null,
    title: String = "",
    isBackIconVisible: Boolean = true,
    painterResource: Painter? = null,
    leading: (@Composable (() -> Unit))? = null,
    actions: (@Composable (() -> Unit))? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = Theme.typography.titleLarge,
                color = colors.contentPrimary
            )
        },
        navigationIcon = {
            if (isBackIconVisible) {
                if (painterResource != null) {
                    Icon(
                        painter = painterResource,
                        contentDescription = "",
                        modifier = Modifier.noRippleEffect(onClick = debouncedClick { onNavigateUp?.invoke() })
                            .padding(start = 16.dp, end = 16.dp),
                        tint = colors.contentSecondary,
                    )
                }
            } else {
                leading?.invoke()
            }

        },
        actions = {
            actions?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface),
        modifier = modifier,
    )
}