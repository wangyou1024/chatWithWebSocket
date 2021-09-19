package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupServiceAPI {

    @POST("/group/createGroup")
    fun createGroup(@Body group: Group): Observable<ResponseData<Group>>

    @GET("/group/findGroupById")
    fun findGroupById(@Query("gid") gid: Long): Observable<ResponseData<Group>>

    @GET("/group/findJoinedGroups")
    fun findJoinedGroups(): Observable<ResponseData<MutableList<Group>>>

    @GET("/group/searchGroups")
    fun searchGroups(@Query("searchKey") searchKey: String): Observable<ResponseData<MutableList<Group>>>

    @POST("/group/findGroupListByIds")
    fun findGroupListByIds(@Body gids: MutableList<Long>): Observable<ResponseData<MutableList<Group>>>
}