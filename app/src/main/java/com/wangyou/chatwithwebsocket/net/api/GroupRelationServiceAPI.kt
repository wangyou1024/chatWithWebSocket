package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupRelationServiceAPI {

    @GET("/groupRelation/findRelation")
    fun findRelation(@Query("gid") gid: Long): Observable<ResponseData<GroupRelation>>

    @GET("/groupRelation/findRelationList")
    fun findRelationList(): Observable<ResponseData<MutableList<GroupRelation>>>

}