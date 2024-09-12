package presentation.screen.history

import presentation.screen.receipts.PageListener

interface HistoryInteractionListener : PageListener {

//    fun onEditTaxiClicked(itemId: Long)

    fun onClickExportAll()

    fun onRetry()

    fun onSnackBarDismiss()

    fun onSnackBarSuccessDismiss()
}