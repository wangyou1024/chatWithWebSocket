package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.net.api.LoginServiceAPI
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var toast: Toast,
    var loginServiceAPI: LoginServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
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

    fun loginIn(){
        if (logining.value!! || logined.value!!){
            return
        }else{
            logining.value = true
            logining.value = logining.value
        }
        loginServiceAPI.login(userName= username!!.get()!!, password = password!!.get()!!)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                navController!!.popBackStack(R.id.loginFragment, true)
                navController!!.navigate(R.id.mainFragment)
                logining.value = false
                logined.value = true
                toast.setText(R.string.login_success)
                toast.show()
            },object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    logining.value = false
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
    }

    fun signUp(){
        if (logining.value!! || logined.value!!){
            return
        }else if (!Regex("""$[0-9]{11}^""").matches(username!!.get()!!)){
            toast.setText(R.string.invalid_phone)
            toast.show()
            return
        } else {
            logining.value = true
            logining.value = logining.value
        }
        loginServiceAPI.signUp(userName= username!!.get()!!, password = password!!.get()!!)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                logining.value = false
                toast.setText(R.string.sign_success)
                toast.show()
            },object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "${DateTimeUtil.getStrNow()}:${ex.errorMsg}")
                    logining.value = false
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
    }

    fun logout(){
        loginServiceAPI.logout()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                logined.value = false
            }, object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
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

    fun setLogined(logined: Boolean){
        this.logined.value = logined
    }
}