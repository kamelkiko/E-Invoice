package data.util

import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.sqlQuery
import com.vladsch.kotlin.jdbc.usingDefault

fun getData(sql: String): Result<List<Map<String, Any?>>> {
    return try {
        usingDefault { session ->
            val allIdsQuery = sqlQuery(sql)
            val result = session.list(allIdsQuery) { it.toMap() }
            Result.success(result)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

private fun Row.toMap(): Map<String, Any?> {
    val columnCount = this.metaDataOrNull().columnCount
    val map = mutableMapOf<String, Any?>()

    for (i in 1..columnCount) {
        val columnName = this.metaDataOrNull().getColumnName(i)
        map[columnName] = this.anyOrNull(columnName)
    }

    return map
}