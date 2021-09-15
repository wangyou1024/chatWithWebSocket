package com.wangyou.chatwithwebsocket.net.exception

import io.reactivex.rxjava3.functions.Consumer


abstract class ErrorConsumer : Consumer<Throwable> {
    override fun accept(t: Throwable?) {
        val ex: APIException = if (t is APIException) {
            t
        } else {
            APIException.handleException(t!!)
        }
        error(ex)
    }

    protected abstract fun error(ex: APIException)
}