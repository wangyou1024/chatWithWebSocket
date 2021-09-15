package com.wangyou.chatwithwebsocket.net.response

import com.wangyou.chatwithwebsocket.net.exception.APIException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * 统一异常处理
 */
class ResponseTransformer<T>(private var compositeDisposable: CompositeDisposable? = null) :
    ObservableTransformer<ResponseData<T>, T> {

    override fun apply(upstream: Observable<ResponseData<T>>): ObservableSource<T> {
        return upstream
            .doOnSubscribe(Consumer<Disposable> {
                if (compositeDisposable != null) {
                    compositeDisposable!!.add(it)
                }
            })
            .onErrorResumeNext { t ->
                Observable.error<ResponseData<T>>(
                    APIException.handleException(t!!)
                )
            }
            .flatMap(object : Function<ResponseData<T>, ObservableSource<T>> {
                /**
                 * 这段逻辑是业务请求成功后，根据接口返回的数据做一些成功失败后的规范处理
                 */
                override fun apply(responseData: ResponseData<T>): ObservableSource<T>? {
                    if (responseData.code == "200") {
                        if (responseData.data != null) {
                            return Observable.just(responseData.data)
                        }
                    }
                    return Observable.error(APIException(responseData.code!!, responseData.msg!!))
                }
            })
                // 切换线程
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object{
        fun <U> obtion(compositeDisposable: CompositeDisposable?): ResponseTransformer<U>{
            return ResponseTransformer(compositeDisposable);
        }

        fun <U> obtion(): ResponseTransformer<U>{
            return ResponseTransformer()
        }
    }
}