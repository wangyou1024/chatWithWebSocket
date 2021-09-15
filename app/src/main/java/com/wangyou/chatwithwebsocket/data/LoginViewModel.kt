package com.wangyou.chatwithwebsocket.data

import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.net.api.LoginAPI
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var loginAPI: LoginAPI,
): ViewModel() {
    private var username: ObservableField<String>? = null
    private var password: ObservableField<String>? = null
    private var navController: NavController? = null
    private var logining: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var logined: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    init {
        username = ObservableField("admin1")
        password = ObservableField("123")
    }

    fun setNavController(context: NavController){
        this.navController = context
    }

    fun getUsername(): String{
        return username!!.get()!!
    }

    fun setUsername(username: String){
        this.username!!.set(username)
    }

    fun getPassword(): String{
        return password!!.get()!!
    }

    fun setPassword(password: String){
        this.password!!.set(password)
    }

    fun isLogining(): MutableLiveData<Boolean>{
        return logining
    }

    fun loginIn(){
        if (logining.value!! || logined.value!!){
            return
        }else{
            logining.value = true
            logining.value = logining.value
        }
        loginAPI.login(userName= username!!.get()!!, password = password!!.get()!!)
            .compose(ResponseTransformer.obtion())
            .subscribe({
                Log.i(Const.TAG, "${DateTimeUtil.getStrNow()}:登录成功")
                navController!!.popBackStack(R.id.loginFragment, true)
                navController!!.navigate(R.id.mainFragment)
                logining.value = false
                logined.value = true
            },object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "${DateTimeUtil.getStrNow()}:${ex.errorMsg}")
                    logining.value = false
                }
            })
    }

    fun signUp(){
        Log.i("signUp", "${username!!.get()}, ${password!!.get()}")
    }
}