package com.wangyou.chatwithwebsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.databinding.ActivityMainBinding
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.util.SetStatusBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    @Inject
    lateinit var compositeDisposableLifecycle: CompositeDisposableLifecycle

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = Navigation.findNavController(this, R.id.fragmentAll)
        navController.popBackStack(R.id.mainFragment, true)
        navController.navigate(R.id.loginFragment)
        lifecycle.addObserver(compositeDisposableLifecycle)
    }

    override fun onSupportNavigateUp() =
            Navigation.findNavController(this, R.id.fragmentAll).navigateUp()
}