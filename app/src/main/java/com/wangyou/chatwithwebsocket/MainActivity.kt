package com.wangyou.chatwithwebsocket

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.ActivityMainBinding
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.util.SetStatusBar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
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
            } else if (personalViewModel.getPersonal().value?.username != null){
                loginViewModel.setUsername(personalViewModel.getPersonal().value!!.username!!)
                val ft = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val time = Date(cookieList[0].expiresAt())
                Log.i(Const.TAG, "过期时间：${ft.format(time)}")
                toast.setText(ft.format(time))
                toast.show()
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
    }

    /**
     * 个人中心的退出按钮
     */
    fun exit(view: View){
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
        toast.setText(error)
        toast.show()
    }
}