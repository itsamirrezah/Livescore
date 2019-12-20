package com.itsamirrezah.livescore.util

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        private val count = 9

        fun getDates(date: Date, fromTop: Boolean): Pair<String, String> {
            val from = if (!fromTop) 1 else -(count)
            val to = if (!fromTop) count else count - 2

            val dateFrom = Instant.ofEpochMilli(date.time).plus(from.toLong(), ChronoUnit.DAYS)
            val dateTo = dateFrom.plus(to.toLong(), ChronoUnit.DAYS)

            return Pair<String, String>(
                shortDateFormat(DateTimeUtils.toDate(dateFrom)),
                shortDateFormat(DateTimeUtils.toDate(dateTo))
            )
        }

        fun getDates(): Pair<String, String> {
            return getDates(Calendar.getInstance().time, false)
        }

        fun shortDateFormat(date: Date): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date)
        }
    }
}