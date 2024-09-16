package presentation.screen.search

import presentation.screen.receipts.PageListener

interface SearchInteractionListener : PageListener {

    fun onClickImport()

    fun onClickExportAll()

    fun onClickSearch()

    fun onSearchInputChange(q: String)

    fun onRetry()

    fun onSnackBarDismiss()

    fun onSnackBarSuccessDismiss()
}