package com.wangyou.chatwithwebsocket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.ActivityMainBinding
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.StompClient
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var compositeDisposableLifecycle: CompositeDisposableLifecycle

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var toast: Toast

    @Inject
    lateinit var stompClient: StompClient

    @Inject
    lateinit var stompClientLifecycle: StompClientLifecycle

    private val personalViewModel by viewModels<PersonalViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    private var navController: NavController? = null

    private var binding: ActivityMainBinding? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.fragmentAll)
        // 查看本地cookie状态
        val cookieList = okHttpClient.cookieJar().loadForRequest(HttpUrl.get(Const.address))
        personalViewModel.loadSelfError.observe(this, {
            if (it) {
                loginPage("登录状态异常")
            } else {
                personalViewModel.getPersonal().value?.username?.let { username ->
                    loginViewModel.setUsername(username)
                }
            }
        })
        loginViewModel.getLogined().observe(this, {
            Log.i(Const.TAG, "登录状态更新$it")
            if (it){
                personalViewModel.loadSelf()
                if(!stompClient.isConnected){
                    Log.i(Const.TAG, "重新连接stompClient")
                    stompClientLifecycle.connect()
                }
            }
        })
        when {
            cookieList.isEmpty() -> {
                loginPage("未登录")
            }
            cookieList[0].expiresAt() < System.currentTimeMillis() -> {
                loginPage("登录过期")
            }
            else -> {
                personalViewModel.loadSelf()
            }
        }
        lifecycle.addObserver(compositeDisposableLifecycle)
        lifecycle.addObserver(stompClientLifecycle)
    }

    /**
     * 个人中心的退出按钮
     */
    fun exit(view: View) {
        loginViewModel.logout()
        SharedPrefsCookiePersistor(this).clear()
        loginPage("已退出")
    }

    /**
     * 托管fragment的返回
     */
    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.fragmentAll).navigateUp()

    private fun loginPage(error: String) {
        navController!!.popBackStack(R.id.mainFragment, true)
        navController!!.navigate(R.id.loginFragment)
        toast.duration = Toast.LENGTH_SHORT
        toast.setText(error)
        toast.show()
    }
}