package resource.strings.english

import resource.strings.IStringResources

data class English(
    //region Login
    override val login: String = "Login",
    override val loginTitle: String = "Use Admin account to login",
    override val loginUsername: String = "Username",
    override val loginPassword: String = "Password",
    override val loginButton: String = "Login",
    override val loginKeepMeLoggedIn: String = "Keep me logged in",
    //endregion Login

    //region User
    override val receipts: String = "Receipts",
    override val search: String = "Search",
    override val settings: String = "Settings",
    override val history: String = "History",
    //endregion User

    //region Receipt
    override val selectAll: String = "Select All",
    override val send: String = "Send",
    override val outOf: String = "out of",
    override val pluralLetter: String = "s",
    override val number: String = "No.",
    override val invoiceId: String = "Invoice ID",
    override val invoiceNumber: String = "Receipt NO.",
    override val totalTaxes: String = "Total Taxes",
    override val totalValues: String = "Total Values",
    override val subTotal: String = "Subtotal",
    override val perc: String = "Percentage",
    override val currency: String = "Currency",
    override val date: String = "Date",
    override val storeName: String = "Store Name",
    //endregion Receipt

    //region dashboard
    override val logout: String = "Logout",
    override val darkTheme: String = "Dark theme",
    override val dropDownMenu: String = "DropDownMenu",
    override val activate: String = "Activate",
    //endregion dashboard

    //region settings
    override val manageAccount: String = "Manage Account",
    override val editYourAccount: String = "Edit your account",
    override val update: String = "Update",
    override val english: String = "English",
    override val arabic: String = "Arabic",
    //endregion

    override val clearAll: String = "Clear all",
    override val noMatchesFound: String = "Oops, No matches found",
    override val noInternet: String = "Oops! No internet connection!",
    override val unKnownError: String = "Unknown error please retry again!",
    override val empty: String = "Empty",
    override val accountUpdated: String = "Account updated successfully",
    override val uuid: String = "UUID",
    override val setup: String = "Setup",
) : IStringResources