package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.api.UserServiceAPI
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer2
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.net.response.WebSocketTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import org.reactivestreams.Publisher
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    var toast: Toast,
    var stompClient: StompClient,
    var userServiceAPI: UserServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    private var personal: MutableLiveData<User>? = MutableLiveData(User())

    // 当前登录者与展示对象的关系：0：自己；1：陌生人；2：好友
    private var relation: MutableLiveData<Int>? = MutableLiveData(0)

    var username: MutableLiveData<String>? = MutableLiveData("")
    var realName: MutableLiveData<String>? = MutableLiveData("")
    var imageUrl: MutableLiveData<String>? = MutableLiveData("")
    var phone: MutableLiveData<String>? = MutableLiveData("")
    var age: MutableLiveData<String>? = MutableLiveData("")
    var address: MutableLiveData<String>? = MutableLiveData("")
    var email: MutableLiveData<String>? = MutableLiveData("")
    private var gender: MutableLiveData<Int>? = MutableLiveData(0)
    var introduce: MutableLiveData<String>? = MutableLiveData("")
    var loadSelfError: MutableLiveData<Boolean> = MutableLiveData(false)

    fun loadUser(username: String) {
        userServiceAPI.findUserByUsername(username)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                this.personal!!.value = it
                synchronizedInfo()
                Log.i(Const.TAG, it.username!!)
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
    }

    fun loadSelf() {
        userServiceAPI.findUserByPrincipal()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                this.personal!!.value = it
                synchronizedInfo()
                loadSelfError.value = false
                // 标记当前展示角色为自己
                relation?.value = 0
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    loadSelfError.value = true
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
              topic()
    }


    fun topic() {
        val subscribe = stompClient.topic(Const.chatResponse)
            .compose(WebSocketTransformer.option())
            .subscribe({
                Log.i(Const.TAG, "Stomp re==$it");
            }, object : ErrorConsumer2() {
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            });
        compositeDisposableLifecycle.addDisposable(subscribe)
        stompClient.send(Const.chat, "nothing").subscribe()
    }

    private fun synchronizedInfo() {
        username?.value = personal?.value?.username?:""
        realName?.value = personal?.value?.realName?:""
        imageUrl?.value = personal?.value?.imageUrl?:""
        phone?.value = personal?.value?.phone?:""
        age?.value = personal?.value?.age?.toString()?:"0"
        address?.value = personal?.value?.address?:""
        email?.value = personal?.value?.email?:""
        gender?.value = personal?.value?.gender?:0
        introduce?.value = personal?.value?.introduce?:""
    }

    fun getPersonal(): MutableLiveData<User> {
        return personal!!
    }

    fun setPersonal(user: MutableLiveData<User>) {
        personal = user
    }

    fun getGender(): MutableLiveData<Int> {
        return gender!!
    }

    fun setGender(gender: Int) {
        this.gender!!.value = gender
    }

    fun getRelation(): MutableLiveData<Int> {
        return relation!!
    }

}