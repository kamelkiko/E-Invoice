package resource.strings

interface IStringResources {
    //region Login
    val login: String
    val loginTitle: String
    val loginUsername: String
    val loginPassword: String
    val loginButton: String
    val loginKeepMeLoggedIn: String
    //endregion Login

    //region user
    val receipts: String
    val search: String
    val settings: String
    val setup: String
    val history: String
    //endregion user

    //region receipt
    val selectAll: String
    val send: String
    val outOf: String
    val pluralLetter: String
    val number: String
    val invoiceId: String
    val invoiceNumber: String
    val totalTaxes: String
    val totalValues: String
    val subTotal: String
    val perc: String
    val uuid: String
    val currency: String
    val date: String
    val storeName: String
    //endregion receipt

    //region dashboard
    val logout: String
    val darkTheme: String
    val dropDownMenu: String
    val activate: String
    //endregion dashboard

    //region settings
    val manageAccount: String
    val editYourAccount: String
    val update: String
    val english: String
    val arabic: String
    //endregion

    val clearAll: String
    val noMatchesFound: String
    val noInternet: String
    val unKnownError: String
    val empty: String
    val accountUpdated: String
}