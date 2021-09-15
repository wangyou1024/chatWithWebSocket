package com.wangyou.chatwithwebsocket.net.response

class ResponseData<T>(
    var data: T? = null,
    var code: String? = null,
    var msg: String? = null,
) {
}