package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface LoginServiceAPI {

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Observable<ResponseData<User>>

    @POST("user/signUp")
    @FormUrlEncoded
    fun signUp(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Observable<ResponseData<User>>

    @GET("user/findUserById")
    fun findUserById(@Field("uid") uid: Long): Observable<ResponseData<User>>

    @POST("logout")
    fun logout(): Observable<ResponseData<User>>

}