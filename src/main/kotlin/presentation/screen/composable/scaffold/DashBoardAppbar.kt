package presentation.screen.composable.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_drop_down_arrow
import com.abapps.e_invoice.generated.resources.ic_logout
import data.util.AppConstants
import design_system.theme.Theme
import org.jetbrains.compose.resources.vectorResource
import presentation.screen.composable.EIDropdownMenu
import presentation.screen.composable.modifier.circleLayout
import presentation.screen.composable.modifier.cursorHoverIconHand
import presentation.util.kms
import resource.Resources

@Composable
fun DashboardAppbar(
    onClickDropDownMenu: () -> Unit,
    onDismissDropDownMenu: () -> Unit,
    title: String,
    username: String,
    firstUsernameLetter: String,
    isDropMenuExpanded: Boolean,
    onLogOut: () -> Unit,
    onActivate: () -> Unit
) {
    val dropMenuArrowRotateDirection =
        animateFloatAsState(targetValue = if (isDropMenuExpanded) 180f else 0f)

    Column(Modifier.background(Theme.colors.surface)) {
        Row(
            Modifier.height(96.kms).fillMaxWidth()
                .padding(horizontal = 40.kms),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = Theme.typography.headlineLarge,
                color = Theme.colors.contentPrimary
            )
            AnimatedVisibility(visible = AppConstants.storeName.isNotEmpty()) {
                Text(
                    text = "Store: ${AppConstants.storeName}",
                    style = Theme.typography.headline,
                    color = Theme.colors.contentPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Box(contentAlignment = Alignment.CenterEnd) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.kms),
                    modifier = Modifier.clickable(onClick = onClickDropDownMenu)
                        .cursorHoverIconHand()
                ) {
                    Text(
                        modifier = Modifier.circleLayout().padding(8.kms),
                        style = Theme.typography.titleMedium,
                        text = firstUsernameLetter,
                        color = Color.White
                    )
                    Text(
                        text = username,
                        style = Theme.typography.titleMedium,
                        color = Theme.colors.contentPrimary
                    )
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_drop_down_arrow),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Theme.colors.contentPrimary),
                        modifier = Modifier.graphicsLayer {
                            rotationZ = dropMenuArrowRotateDirection.value
                        }
                    )
                }
                Box(contentAlignment = Alignment.BottomEnd) {
                    EIDropdownMenu(
                        expanded = isDropMenuExpanded,
                        onDismissRequest = onDismissDropDownMenu,
                        offset = DpOffset.Zero.copy(y = 32.kms),
                        shape = RoundedCornerShape(Theme.radius.medium)
                            .copy(topEnd = CornerSize(Theme.radius.small)),
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onDismissDropDownMenu()
                                onLogOut()
                            },
                            modifier = Modifier.cursorHoverIconHand(),
                            enabled = true,
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.spacedBy(8.kms),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        vectorResource(Res.drawable.ic_logout),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = Resources.strings.logout,
                                        textAlign = TextAlign.Center,
                                        style = Theme.typography.titleMedium,
                                        color = Theme.colors.primary
                                    )
                                }
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                onDismissDropDownMenu()
                                onActivate()
                            },
                            modifier = Modifier.cursorHoverIconHand(),
                            enabled = true,
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.spacedBy(8.kms),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.PlayCircleOutline,
                                        contentDescription = null,
                                        tint = Theme.colors.primary
                                    )
                                    Text(
                                        text = Resources.strings.activate,
                                        textAlign = TextAlign.Center,
                                        style = Theme.typography.titleMedium,
                                        color = Theme.colors.primary
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
        HorizontalDivider(thickness = 1.kms, color = Theme.colors.divider)
    }
}