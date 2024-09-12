package presentation.screen.composable.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import presentation.screen.composable.EILogo
import presentation.screen.composable.extensions.pxToDp
import presentation.screen.composable.modifier.centerItem
import presentation.screen.composable.modifier.cursorHoverIconHand
import presentation.util.kms
import resource.Resources
import design_system.composable.EIToggleButton
import design_system.theme.Theme

@Composable
fun DashboardSideBar(
    onSwitchTheme: () -> Unit,
    darkTheme: Boolean,
    currentItem: Int,
    content: @Composable ColumnScope.(
        sideBarUnexpandedWidthInKms: Dp,
        mainMenuIsExpanded: Boolean,
        itemHeight: (itemHeight: Float) -> Unit
    ) -> Unit
) {
    val mainMenuItemHeight = remember { mutableStateOf(0f) }
    val mainMenuIsExpanded = remember { mutableStateOf(false) }
    val sideBarUnexpandedWidthInKms: Dp by remember { mutableStateOf(120.kms) }
    val sideBarExpandedWidthInKms: Dp by remember { mutableStateOf(300.kms) }

    Row(Modifier.fillMaxHeight().background(Theme.colors.background)) {
        //region sidebar
        Column(
            Modifier
                .width(
                    animateDpAsState(
                        targetValue = if (mainMenuIsExpanded.value) sideBarExpandedWidthInKms
                        else sideBarUnexpandedWidthInKms
                    ).value
                )
                .padding(vertical = 40.kms)
                .pointerInput(PointerEventType.Enter) { mainMenuIsExpanded.value = true }
                .pointerInput(PointerEventType.Exit) { mainMenuIsExpanded.value = false },
        ) {
            //region logo
            EILogo(
                expanded = mainMenuIsExpanded.value,
                modifier = Modifier.centerItem(
                    sideBarUnexpandedWidthInDp = sideBarUnexpandedWidthInKms,
                    itemWidth = 36f
                ).clickable { }.cursorHoverIconHand()
            )
            //endregion
            Spacer(Modifier.height(40.kms))
            //region main menu
            Box(Modifier.height(sideBarExpandedWidthInKms)) {
                Column(Modifier.fillMaxSize()) {
                    Spacer(
                        Modifier.height(
                            animateDpAsState(
                                (mainMenuItemHeight.value.pxToDp() * currentItem)
                            ).value
                        )
                    )
                    Spacer(
                        Modifier.height(mainMenuItemHeight.value.pxToDp())
                            .padding(vertical = 8.kms)
                            .width(4.kms)
                            .clip(RoundedCornerShape(16.kms))
                            .background(Color(0xffF53D47))
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    content(sideBarUnexpandedWidthInKms, mainMenuIsExpanded.value) { itemHeight ->
                        mainMenuItemHeight.value = itemHeight
                    }
                }
            }
            //endregion
            Spacer(Modifier.weight(1f))
            //region toggle theme button
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.kms),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                EIToggleButton(
                    isDark = darkTheme,
                    onToggle = onSwitchTheme,
                    modifier = Modifier
                        .centerItem(sideBarUnexpandedWidthInDp = sideBarUnexpandedWidthInKms)
                )
                AnimatedVisibility(
                    visible = mainMenuIsExpanded.value,
                    enter = fadeIn(tween(500)),
                    exit = fadeOut()
                ) {
                    Text(
                        Resources.strings.darkTheme,
                        maxLines = 1,
                        style = Theme.typography.titleMedium,
                        color = Theme.colors.contentPrimary
                    )
                }

            }
            //endregion
        }
        //endregion

        //region end border
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(DividerDefaults.Thickness),
            color = Theme.colors.divider
        )
        //endregion
    }
}