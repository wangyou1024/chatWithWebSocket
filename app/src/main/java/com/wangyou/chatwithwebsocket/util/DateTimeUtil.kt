package com.wangyou.chatwithwebsocket.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object DateTimeUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    fun getStrByTime(time: Long): String{
        val time = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault())
        return "${time.hour}:${time.minute}"
    }
}