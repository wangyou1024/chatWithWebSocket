package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation
import com.wangyou.chatwithwebsocket.net.api.UserRelationServiceAPI
import com.wangyou.chatwithwebsocket.net.api.UserServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer2
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.net.response.WebSocketTransformer
import com.wangyou.chatwithwebsocket.util.DateTimeUtil
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
    var stompClientLifecycle: StompClientLifecycle,
    var userRelationServiceAPI: UserRelationServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    private var personal: MutableLiveData<User> = MutableLiveData(User())
    private var self: MutableLiveData<User> = MutableLiveData(User())

    // 当前登录者与展示对象的关系：0：自己；1：陌生人；2：好友
    private var relation: MutableLiveData<Int> = MutableLiveData(0)
    // 当前页面是否从群聊内进入
    private var gid: MutableLiveData<Long> = MutableLiveData(0)

    var username: MutableLiveData<String> = MutableLiveData("")
    var realName: MutableLiveData<String> = MutableLiveData("")
    var imageUrl: MutableLiveData<String> = MutableLiveData("")
    var phone: MutableLiveData<String> = MutableLiveData("")
    var age: MutableLiveData<String> = MutableLiveData("")
    var address: MutableLiveData<String> = MutableLiveData("")
    var email: MutableLiveData<String> = MutableLiveData("")
    private var gender: MutableLiveData<Int> = MutableLiveData(0)
    var introduce: MutableLiveData<String> = MutableLiveData("")
    var loadSelfError: MutableLiveData<Boolean> = MutableLiveData(false)

    fun loadUser(username: String) {
        userServiceAPI.findUserByUsername(username)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                this.personal.value = it
                synchronizedInfo()
                Log.i(Const.TAG, it.username!!)
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
    }

    fun loadUserById(uid: Long) {
        userServiceAPI.findUserById(uid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                this.personal.value =  it
                synchronizedInfo()
            }, object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
        loadUserRelation(uid)
    }

    private fun loadUserRelation(uid: Long){
        // 当前进入的是登录者
        if (uid == self.value?.uid){
            relation.value = 0
            return
        }
        userRelationServiceAPI.findUserRelation(uid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                if (it.enable == null || it.enable == UserRelation.NO_DEAL || it.enable == UserRelation.REFUSE || it.enable == UserRelation.DELETE){
                    relation.value = 1
                } else {
                    relation.value = 2
                }
            }, object : ErrorConsumer(){
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
                this.personal.value = it
                this.self.value = it
                synchronizedInfo()
                // 将登录者信息加载到stompClient中，订阅信息才能正确响应
                stompClientLifecycle.setSelf(self)
                loadSelfError.value = false
                // 标记当前展示角色为自己
                relation.value = 0
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    loadSelfError.value = true
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
    }


    fun sendFriendApplication() {
        if (!stompClient.isConnected){
            stompClientLifecycle.connect()
        }
        val userRelation = UserRelation(
            0,
            self.value?.uid,
            personal.value?.uid,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            UserRelation.NO_DEAL
        )
        stompClient.send(Const.friendApplication, Gson().toJson(userRelation)).subscribe()
    }

    fun deleteFriend() {
        if (!stompClient.isConnected){
            stompClientLifecycle.connect()
        }
        val userRelation = UserRelation(
            0,
            self.value?.uid,
            personal.value?.uid,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            UserRelation.DELETE
        )
        stompClient.send(Const.friendApplication, Gson().toJson(userRelation)).subscribe()
    }

    fun deleteGroupMember(){
        if (!stompClient.isConnected){
            stompClientLifecycle.connect()
        }
        val groupRelation = GroupRelation(
            0,
            gid.value,
            personal.value?.uid,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            GroupRelation.DELETE
        )
        stompClient.send(Const.groupApplication, Gson().toJson(groupRelation)).subscribe()
    }


    private fun synchronizedInfo() {
        username.value = personal.value?.username?:""
        realName.value = personal.value?.realName?:""
        imageUrl.value = personal.value?.imageUrl?:""
        phone.value = personal.value?.phone?:""
        age.value = personal.value?.age?.toString()?:"0"
        address.value = personal.value?.address?:""
        email.value = personal.value?.email?:""
        gender.value = personal.value?.gender?:0
        introduce.value = personal.value?.introduce?:""
    }

    fun getPersonal(): MutableLiveData<User> {
        return personal
    }

    fun setPersonal(user: MutableLiveData<User>) {
        personal = user
    }

    fun getGender(): MutableLiveData<Int> {
        return gender
    }

    fun setGender(gender: Int) {
        this.gender.value = gender
    }

    fun getRelation(): MutableLiveData<Int> {
        return relation
    }

    fun setGid(gid: Long){
        this.gid.value = gid
    }

    fun getGid(): MutableLiveData<Long>{
        return this.gid
    }

    fun getSelf(): MutableLiveData<User>{
        return self
    }

}