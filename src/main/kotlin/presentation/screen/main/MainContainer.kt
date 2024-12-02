package presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.abapps.e_invoice.generated.resources.*
import data.util.AppConstants
import design_system.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import presentation.screen.composable.SnackBar
import presentation.screen.composable.scaffold.DashBoardScaffold
import presentation.screen.composable.scaffold.DashboardAppbar
import presentation.screen.composable.scaffold.DashboardSideBar
import presentation.screen.composable.scaffold.EISideBarItem
import presentation.screen.login.LoginScreen
import presentation.util.EventHandler
import presentation.util.kms
import resource.Resources

object MainContainer : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<MainViewModel>()
        val state by screenModel.state.collectAsState()
        val listener: MainInteractionListener = screenModel
//        var isDark by LocalThemeIsDark.current
//        isDark = state.isDarkMode

        EventHandler(screenModel.effect) { effect, navigator ->
            when (effect) {
                MainUiEffect.Logout -> {
                    navigator.replaceAll(LoginScreen())
                }
            }
        }

        LaunchedEffect(state.isSnackBarVisible) {
            delay(1500)
            listener.onSnackBarDismiss()
        }

        LaunchedEffect(state.isSnackBarSuccessVisible) {
            delay(1500)
            listener.onSnackBarSuccessDismiss()
        }

        TabNavigator(ListReceiptsTab) {
            val tabNavigator = LocalTabNavigator.current
            DashBoardScaffold(
                appbar = {
                    DashboardAppbar(
                        title = tabNavigator.current.options.title,
                        username = state.username,
                        firstUsernameLetter = state.firstUsernameLetter,
                        onLogOut = listener::onClickLogout,
                        isDropMenuExpanded = state.isDropMenuExpanded,
                        onClickDropDownMenu = listener::onClickDropDownMenu,
                        onDismissDropDownMenu = listener::onDismissDropDownMenu,
                        onActivate = listener::onClickActive,
                    )
                },
                sideBar = {
                    DashboardSideBar(
                        currentItem = tabNavigator.current.options.index.toInt(),
                        onSwitchTheme = listener::onSwitchTheme,
                        darkTheme = state.isDarkMode
                    ) { sideBarUnexpandedWidthInKms, mainMenuIsExpanded, itemHeight ->
                        SideBarItems(
                            mainMenuIsExpanded = mainMenuIsExpanded,
                            sideBarUnexpandedWidthInKms = sideBarUnexpandedWidthInKms,
                            itemHeight = itemHeight
                        )
                    }
                },
                content = {
                    Box(Modifier.background(Theme.colors.surface)) {
                        CurrentTab()
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            AnimatedVisibility(
                                state.isSnackBarVisible,
                                modifier = Modifier.animateContentSize(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                SnackBar(
                                    onDismiss = listener::onSnackBarDismiss,
                                    backgroundColor = Theme.colors.hover
                                ) {
                                    Image(
                                        imageVector = vectorResource(Res.drawable.ic_info_square),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(color = Theme.colors.primary),
                                        modifier = Modifier.padding(16.kms)
                                    )
                                    Text(
                                        text = state.snackBarTitle ?: Resources.strings.noInternet,
                                        style = Theme.typography.titleMedium,
                                        color = Theme.colors.primary,
                                    )
                                }
                            }
                            AnimatedVisibility(
                                state.isSnackBarSuccessVisible,
                                modifier = Modifier.animateContentSize(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                SnackBar(
                                    onDismiss = listener::onSnackBarSuccessDismiss,
                                    backgroundColor = Theme.colors.hover
                                ) {
                                    Image(
                                        imageVector = vectorResource(Res.drawable.warning),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(color = Theme.colors.green),
                                        modifier = Modifier.padding(8.kms)
                                    )
                                    Text(
                                        text = state.snackBarSuccessTitle ?: Resources.strings.noInternet,
                                        style = Theme.typography.titleMedium,
                                        color = Theme.colors.green,
                                    )
                                }
                            }
                        }
                    }
                },
            )
        }
    }

    @Composable
    private fun ColumnScope.SideBarItems(
        mainMenuIsExpanded: Boolean,
        sideBarUnexpandedWidthInKms: Dp,
        itemHeight: (itemHeight: Float) -> Unit
    ) {
        TabNavigationItem(
            tab = ListReceiptsTab,
            selectedIconResource = Res.drawable.ic_overview_fill,
            unSelectedIconResource = Res.drawable.ic_overview_outlined,
            mainMenuIsExpanded = mainMenuIsExpanded,
            sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
            modifier = Modifier.onGloballyPositioned {
                itemHeight(it.boundsInParent().height)
            }
        )
        TabNavigationItem(
            tab = SearchTab,
            selectedIconResource = Res.drawable.ic_search,
            unSelectedIconResource = Res.drawable.ic_search,
            mainMenuIsExpanded = mainMenuIsExpanded,
            sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
            modifier = Modifier.onGloballyPositioned {
                itemHeight(it.boundsInParent().height)
            }
        )
        TabNavigationItem(
            tab = StatusTab,
            selectedIconResource = Res.drawable.history,
            unSelectedIconResource = Res.drawable.history,
            mainMenuIsExpanded = mainMenuIsExpanded,
            sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
            modifier = Modifier.onGloballyPositioned {
                itemHeight(it.boundsInParent().height)
            }
        )
        if (AppConstants.isAdmin)
            TabNavigationItem(
                tab = SettingsTab,
                selectedIconResource = Res.drawable.settings,
                unSelectedIconResource = Res.drawable.settings,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
                modifier = Modifier.onGloballyPositioned {
                    itemHeight(it.boundsInParent().height)
                }
            )
      //  if (AppConstants.isAdmin)
            TabNavigationItem(
                tab = SetupTab,
                selectedIconResource = Res.drawable.ic_admin,
                unSelectedIconResource = Res.drawable.ic_admin,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
                modifier = Modifier.onGloballyPositioned {
                    itemHeight(it.boundsInParent().height)
                }
            )

    }

    @Composable
    fun ColumnScope.TabNavigationItem(
        tab: Tab,
        selectedIconResource: DrawableResource,
        unSelectedIconResource: DrawableResource,
        mainMenuIsExpanded: Boolean,
        sideBarUnexpandedWidth: Dp,
        modifier: Modifier = Modifier,
    ) {
        val tabNavigator = LocalTabNavigator.current
        EISideBarItem(
            onClick = { tabNavigator.current = tab },
            isSelected = tabNavigator.current == tab,
            label = tab.options.title,
            selectedIconResource = selectedIconResource,
            unSelectedIconResource = unSelectedIconResource,
            mainMenuIsExpanded = mainMenuIsExpanded,
            sideBarUnexpandedWidthInKms = sideBarUnexpandedWidth,
            modifier = modifier
        )
    }
}