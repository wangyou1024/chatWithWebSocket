package com.wangyou.chatwithwebsocket.data

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R

class LoginViewModel(): ViewModel() {
    private var username: ObservableField<String>? = null
    private var password: ObservableField<String>? = null
    private var navController: NavController? = null

    init {
        username = ObservableField("13635347490")
        password = ObservableField("123456")
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

    fun loginIn(){
        Log.i("login", "${username!!.get()}, ${password!!.get()}")
        navController!!.popBackStack(R.id.loginFragment, true)
        navController!!.navigate(R.id.mainFragment)
    }

    fun signUp(){
        Log.i("signUp", "${username!!.get()}, ${password!!.get()}")
    }
}