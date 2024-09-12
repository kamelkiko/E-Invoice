package presentation.screen.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import design_system.composable.EITextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTextField(
    options: List<DropDownState>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    selectedItem: DropDownState,
    enabled: Boolean = true,
    onClick: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selectedItem) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        EITextField(
            text = selectedOptionText.name,
            enabled = enabled,
            onValueChange = {},
            readOnly = true,
            modifier = modifier.menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            label = "Choose store"
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onClick(selectionOption.id)
                    },
                    text = {
                        Text(text = selectionOption.name)
                    },
                )
            }
        }
    }
}

@Immutable
data class DropDownState(
    val id: Int = 0,
    val name: String = ""
)