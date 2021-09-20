package com.wangyou.chatwithwebsocket.net.exception

import android.util.Log
import com.google.gson.JsonParseException
import com.wangyou.chatwithwebsocket.conf.Const
import org.json.JSONException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

class APIException(
    var code: String = "",
    var errorMsg: String = ""
) : Exception() {


    companion object {
        const val UNKNOWN_ERROR = 1000
        const val PARSE_ERROR = 1001
        const val NETWORK_ERROR = 1002

        @JvmStatic
        fun handleException(e: Throwable): APIException {
            var ex: APIException = APIException(UNKNOWN_ERROR.toString(), e.message?:"未知异常")
            if (e is JsonParseException || e is JSONException || e is ParseException) {
                ex = APIException(PARSE_ERROR.toString(), "数据解析异常")
            } else if (e is ConnectException || e is UnknownHostException || e is SocketTimeoutException) {
                ex = APIException(NETWORK_ERROR.toString(), "网络请求异常")
            }
            return ex
        }
    }
}