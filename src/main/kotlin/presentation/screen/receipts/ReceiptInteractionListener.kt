package presentation.screen.receipts

interface ReceiptInteractionListener : PageListener {

    fun onChooseStartDateInputChange(date: String)

    fun onChooseEndDateInputChange(date: String)

    fun onDismissStartDate()

    fun onDismissEndDate()

    fun onShowStartDate()

    fun onShowEndDate()

    fun onEditTaxiClicked(itemId: Long)

    fun onClickSearch()

    fun onClickSelectAll()

    fun onClickSend()

    fun onRetry()

    fun onSnackBarDismiss()

    fun onSnackBarSuccessDismiss()
}

interface PageListener {

    fun onItemsIndicatorChange(itemPerPage: Int)

    fun onPageClick(pageNumber: Int)

}