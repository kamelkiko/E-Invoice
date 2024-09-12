package presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import presentation.screen.receipts.ReceiptsListScreen
import presentation.screen.search.SearchScreen
import presentation.screen.settings.SettingsScreen
import presentation.screen.history.HistoryScreen
import resource.Resources

object ListReceiptsTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Resources.strings.receipts
            return remember { TabOptions(index = 0u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = ReceiptsListScreen())
    }
}

object SearchTab : Tab {

    override val options: TabOptions
        @Composable get() {
            val title = Resources.strings.search
            return remember { TabOptions(index = 1u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = SearchScreen())
    }
}

object StatusTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Resources.strings.history
            return remember { TabOptions(index = 2u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = HistoryScreen())
    }
}

object SettingsTab : Tab {

    override val options: TabOptions
        @Composable get() {
            val title = Resources.strings.settings
            return remember { TabOptions(index = 3u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = SettingsScreen())
    }
}