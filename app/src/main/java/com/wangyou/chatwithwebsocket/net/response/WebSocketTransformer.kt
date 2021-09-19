package com.wangyou.chatwithwebsocket.net.response

import android.util.Log
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.net.exception.APIException
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import ua.naiksoftware.stomp.dto.StompMessage

/**
 *  webSocket默认工作在Thread.currentThread().name -> okhttp: http://IP:PORT/...
 */
class WebSocketTransformer : FlowableTransformer<StompMessage, String> {
    override fun apply(upstream: Flowable<StompMessage>): Publisher<String> {
        return upstream
            .onErrorResumeNext { throwable: Throwable ->
                Log.i(Const.TAG, "订阅异常")
                Flowable.error<StompMessage>(APIException.handleException(throwable))
            }
            .flatMap(object : Function<StompMessage, Publisher<String>> {
                override fun apply(t: StompMessage): Publisher<String> {
                    Log.i(Const.TAG, "Stomp re==${t}")
                    if (t.payload != null){
                        Log.i(Const.TAG, "Stomp payload==${t.payload}")
                        return Flowable.just(t.payload)
                    }
                    return Flowable.error(APIException(APIException.UNKNOWN_ERROR.toString(), "空值异常"))
                }
            })
             .observeOn(AndroidSchedulers.mainThread())
    }

    companion object{
        fun option(): WebSocketTransformer{
            return WebSocketTransformer()
        }
    }
}