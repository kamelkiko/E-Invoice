package presentation.screen.composable.table

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import presentation.util.kms
import resource.Resources
import design_system.theme.Theme

@Composable
fun TotalItemsIndicator(
    modifier: Modifier = Modifier,
    totalItems: Int,
    maxNumberOfItems: Int = 50,
    numberItemInPage: Int,
    onItemPerPageChange: (Int) -> Unit,
    itemType: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.kms)
    ) {
        BasicTextField(
            modifier = Modifier
                .border(
                    width = 1.kms,
                    color = Theme.colors.contentBorder,
                    shape = RoundedCornerShape(Theme.radius.medium)
                ).padding(vertical = 8.kms).width(40.kms),
            value = numberItemInPage.toString(),
            onValueChange = {
                runCatching {
                    onItemPerPageChange(
                        when {
                            it.toInt() >= maxNumberOfItems -> maxNumberOfItems
                            it.toInt() <= 0 -> 1
                            else -> it.toInt()
                        }
                    )
                }
            },
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                color = Theme.colors.contentPrimary,
                fontStyle = Theme.typography.title.fontStyle
            ),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Text(
            "$itemType ${Resources.strings.outOf} $totalItems ${itemType}${Resources.strings.pluralLetter}",
            style = Theme.typography.body,
            color = Theme.colors.contentSecondary
        )
    }
}