package com.frn.modernnote.presentation.utils

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import java.util.logging.SimpleFormatter


object DateFormatter {
    private val millisInHours = TimeUnit.HOURS.toMillis(1)
    private val millisInDays = TimeUnit.DAYS.toMillis(1)
    private val formatter = SimpleDateFormat.getDateInstance().format(DateFormat.SHORT)
    fun formatDateToString(timestamp: Long): String {

        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when{
            diff < millisInHours -> "Just now"
            diff < millisInDays -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours h ago"
            }
            else -> {
                formatter.format(timestamp)
            }
        }

    }

}


fun Long.toFormatDate(): String{
    return DateFormatter.formatDateToString(this)
}