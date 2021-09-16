package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UserServiceAPI {

    @GET("/user/findUserById")
    fun findUserById(@Query("uid") uid: Long): Observable<ResponseData<User>>

    @GET("/user/findUserByUsername")
    fun findUserByUsername(@Query("username") username: String): Observable<ResponseData<User>>

    @GET("/user/findUserByPrincipal")
    fun findUserByPrincipal(): Observable<ResponseData<User>>
}