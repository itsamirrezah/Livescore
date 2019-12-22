package com.itsamirrezah.livescore.util

import android.text.format.DateUtils
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class Utils {

    companion object {

        fun getDates(date: Date, fromTop: Boolean): Pair<String, String> {
            val count = 9
            val from = if (!fromTop) 1 else -(count)
            val to = if (!fromTop) count else count - 1

            val dateFrom = Instant.ofEpochMilli(date.time).plus(from.toLong(), ChronoUnit.DAYS)
            val dateTo = dateFrom.plus(to.toLong(), ChronoUnit.DAYS)

            return Pair(
                shortDateFormat(DateTimeUtils.toDate(dateFrom)),
                shortDateFormat(DateTimeUtils.toDate(dateTo))
            )
        }

        fun getDates(): Pair<String, String> {
            val yesterday = Instant.now().minus(1, ChronoUnit.DAYS)
            return getDates(DateTimeUtils.toDate(yesterday), false)
        }

        //e.g: 2020-01-01
        fun shortDateFormat(date: Date): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date)
        }

        //e.g: December 18
        fun shortRelativeDate(time: Long): String {
            return SimpleDateFormat("MMMM dd", Locale.ENGLISH).format(time)
        }

        //e.g: 18:30
        fun clockFormat(date: Date): String {
            return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date)
        }

        //e.g: Yesterday, Today, Tomorrow
        fun relativeDay(time: Long): String {
            return DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS
            ).toString()
        }

        //e.g: Sunday, Monday, etc
        fun dayOfWeekFormat(time: Long): String {
            return SimpleDateFormat("EEEE", Locale.ENGLISH).format(time)
        }

        fun daysDifference(localDate: LocalDate): Long {
            return abs(ChronoUnit.DAYS.between(LocalDate.now(), localDate))
        }

        fun toLocalDate(utcDate: String): LocalDate {
            return LocalDate.parse(utcDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
        }

        fun toDate(utcDate: String): Date? {
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(utcDate)
        }

        fun toDate(localDate: LocalDate): Date {
            return DateTimeUtils.toDate(localDate.atStartOfDay().toInstant(ZoneOffset.UTC))
        }
    }
}