package com.wangyou.chatwithwebsocket.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

object DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getStrByTime(now: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val time =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZoneId.systemDefault())
            val timeNow = LocalDateTime.now()
            var str = ""
            if (time.year == timeNow.year){
                str = if (time.month == timeNow.month){
                    when {
                        time.dayOfMonth == timeNow.dayOfMonth -> {
                            ""
                        }
                        timeNow.dayOfMonth - time.dayOfMonth == 1 -> {
                            "昨天"
                        }
                        timeNow.dayOfMonth - time.dayOfMonth <= 7 -> {
                            time.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)
                        }
                        else -> {
                            "${time.month.value}-${time.dayOfMonth}"
                        }
                    }
                }else {
                    "${time.month}-${time.dayOfMonth}"
                }
            } else {
                str = "${time.year}-${time.month}-${time.dayOfMonth}"
            }
            if (time.hour > 12){
                str = "$str 下午${time.hour-12}:"
            }else {
                str = "$str 上午${time.hour}:"
            }
            if (time.minute < 10){
                str = "${str}0"
            }
            "$str${time.minute}"
        } else {
            val ft = SimpleDateFormat("yyyy-MM-dd hh:mm")
            val time = Date(now * 1000)
            ft.format(time)
        }
    }

    @JvmStatic
    fun getTimeNow(): Long{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Instant.now().epochSecond
        } else {
            Date().time/1000
        }
    }

    @JvmStatic
    fun getStrNow(): String{
        return getStrByTime(getTimeNow())
    }
}