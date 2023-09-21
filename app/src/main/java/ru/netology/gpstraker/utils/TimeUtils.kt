package ru.netology.gpstraker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
@SuppressLint("SimpleDateFormat")
object TimeUtils {
    private val dateFormater = SimpleDateFormat("dd/MM/yyyy HH:mm")
    private val timeFormater = SimpleDateFormat("HH:mm:ss:SSS")

    fun getTime(timeInMillis: Long): String {
        val cv = Calendar.getInstance()
        timeFormater.timeZone = TimeZone.getTimeZone("UTC")
        cv.timeInMillis = timeInMillis
        return timeFormater.format(cv.time)
    }

    fun getDate(): String {
        val cv = Calendar.getInstance()
        return dateFormater.format(cv.time)
    }
}