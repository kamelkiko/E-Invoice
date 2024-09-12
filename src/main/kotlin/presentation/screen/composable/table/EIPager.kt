package presentation.screen.composable.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_arrow_left_dark
import com.abapps.e_invoice.generated.resources.ic_arrow_right_dark
import org.jetbrains.compose.resources.painterResource
import presentation.util.kms

@Composable
fun EIPager(
    modifier: Modifier = Modifier,
    maxPages: Int,
    currentPage: Int,
    onPageClicked: (Int) -> Unit,
    maxDisplayPages: Int = 6
) {
    val start = calculateStartPage(currentPage, maxPages, maxDisplayPages)
    val end = calculateEndPage(start, currentPage, maxPages, maxDisplayPages)
    val showPages = (start..end).toList()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.kms),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowIcon(
            painter = painterResource(Res.drawable.ic_arrow_left_dark),
            onClick = { onPageClicked(currentPage - 1) },
            enable = currentPage != 1
        )

        showPages.forEachIndexed { index, position ->
            EIToggleableTextButton(
                "$position",
                onSelectChange = { onPageClicked(position) },
                selected = currentPage == position
            )
            if (index == showPages.lastIndex) {
                if (position < maxPages - 1) {
                    EIToggleableTextButton(
                        "...",
                        onSelectChange = {},
                        selected = false
                    )
                }
                if (position != maxPages) {
                    EIToggleableTextButton(
                        "$maxPages",
                        onSelectChange = { onPageClicked(maxPages) },
                        selected = currentPage == maxPages
                    )
                }
            }
        }

        ArrowIcon(
            painter = painterResource(Res.drawable.ic_arrow_right_dark),
            onClick = { onPageClicked(currentPage + 1) },
            enable = currentPage != maxPages
        )
    }
}

private fun calculateStartPage(currentPage: Int, maxPages: Int, maxDisplayPages: Int): Int {
    return when {
        maxPages < maxDisplayPages -> 1
        currentPage >= maxPages - 2 -> maxPages - 4
        currentPage >= maxDisplayPages - 2 -> currentPage - 2
        currentPage > maxDisplayPages - 1 -> currentPage - 3
        else -> 1
    }
}

private fun calculateEndPage(
    start: Int, currentPage: Int, maxPages: Int, maxDisplayPages: Int
): Int {
    return if (maxDisplayPages > maxPages) {
        maxPages
    } else if (currentPage <= maxPages - 1) {
        start + 4
    } else {
        maxPages
    }
}