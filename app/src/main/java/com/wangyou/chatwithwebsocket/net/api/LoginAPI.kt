package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface LoginAPI {

    @POST("/login")
    @FormUrlEncoded
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Observable<ResponseData<User>>

}