package com.wangyou.chatwithwebsocket.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getStrByTime(now: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val time =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZoneId.systemDefault())
            "${time.hour}:${time.minute}"
        } else {
            val ft = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
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