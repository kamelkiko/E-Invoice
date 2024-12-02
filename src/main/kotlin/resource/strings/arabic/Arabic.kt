package resource.strings.arabic

import resource.strings.IStringResources

data class Arabic(
    //region Login
    override val login: String = "تسجيل الدخول",
    override val loginTitle: String = "استخدم اكونت المدير للدخول",
    override val loginUsername: String = "اسم المستخدم",
    override val loginPassword: String = "كلمه المرور",
    override val loginButton: String = "الدخول",
    override val loginKeepMeLoggedIn: String = "احفظني للدخول مره اخري",
    //endregion Login

    //region User
    override val receipts: String = "الإيصالات",
    override val search: String = "البحث",
    override val settings: String = "الاعدادات",
    override val history: String = "السجلات",
    //endregion User

    //region Receipt
    override val selectAll: String = "اختيار الكل",
    override val send: String = "ارسال",
    override val outOf: String = "متبقي من",
    override val pluralLetter: String = "",
    override val number: String = "م",
    override val invoiceId: String = "كود الفاتورة",
    override val invoiceNumber: String = "رقم الفاتورة",
    override val totalTaxes: String = "اجمالي الضرائب",
    override val totalValues: String = "اجمالي قيمه الايصال",
    override val subTotal: String = "اجمالي السعر قبل الضريبة",
    override val perc: String = "النسبة المئوية",
    override val currency: String = "العمله",
    override val date: String = "التاريخ",
    override val storeName: String = "اسم المحل",
    //endregion Receipt

    //region dashboard
    override val logout: String = "الخروج",
    override val darkTheme: String = "الوضع الليلي",
    override val dropDownMenu: String = "قائمة مندسله",
    override val activate: String = "تفعيل",
    //endregion dashboard

    //region settings
    override val manageAccount: String = "ادارة الحساب",
    override val editYourAccount: String = "تعديل حسابك",
    override val update: String = "تعديل",
    override val english: String = "الانجليزية",
    override val arabic: String = "العربية",
    //endregion

    override val clearAll: String = "حذف الجميع",
    override val noMatchesFound: String = "خطأ! لايوجد تطابق",
    override val noInternet: String = "خطأ! لا يوجد انترنت",
    override val unKnownError: String = "خطأ غير معروف! برجاء المحاوله مره اخري",
    override val empty: String = "لا يوجد بيانات",
    override val accountUpdated: String = "تم تعديل الحساب بنجاح",
    override val uuid: String = "الرقم الفريد",
    override val setup: String = "تسطيب",
) : IStringResources
