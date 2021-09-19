package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserServiceAPI {

    @GET("/user/findUserById")
    fun findUserById(@Query("uid") uid: Long): Observable<ResponseData<User>>

    @GET("/user/findUserByUsername")
    fun findUserByUsername(@Query("username") username: String): Observable<ResponseData<User>>

    @GET("/user/findUserByPrincipal")
    fun findUserByPrincipal(): Observable<ResponseData<User>>

    @GET("/user/searchUser")
    fun findUserListBySearchKey(@Query("searchKey") searchKey: String): Observable<ResponseData<MutableList<User>>>

    @POST("/user/findUserListByIds")
    fun findUserListByIds(@Body ids: MutableList<Long>): Observable<ResponseData<MutableList<User>>>

    @GET("/user/findFriends")
    fun findFriends(): Observable<ResponseData<MutableList<User>>>

    @GET("/user/findLeader")
    fun findLeader(@Query("gid") gid: Long): Observable<ResponseData<User>>

    @GET("/user/findMembers")
    fun findMembers(@Query("gid") gid: Long): Observable<ResponseData<MutableList<User>>>
}