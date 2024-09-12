package domain.usecase

import util.getDateNow

class AdminSystemUseCase {
    fun checkPermissionWithPassword(password: String): Boolean {
        return password == adminPassword
    }

    fun checkPermissionWithPasscode(passcode: Int): Boolean {
        val passwordLogic = adminPassword.takeLast(3) + adminPassword.takeLast(3)
        return passcode == passwordLogic.toInt()
    }

    companion object {
        private val dateToday = getDateNow()
        private val passwordLogic =
            dateToday.dayOfMonth + dateToday.monthNumber + dateToday.year - (dateToday.time.hour * 3)
        private val adminPassword = "ahs${passwordLogic.toString().takeLast(3)}"
    }
}