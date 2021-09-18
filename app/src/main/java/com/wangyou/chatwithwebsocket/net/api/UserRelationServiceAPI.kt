package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.UserRelation
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UserRelationServiceAPI {

    @GET("/userRelation/findUserRelation")
    fun findUserRelation(@Query("uid") uid: Long): Observable<ResponseData<UserRelation>>

    @GET("/userRelation/findUserRelationList")
    fun findUserRelationList(): Observable<ResponseData<List<UserRelation>>>
}