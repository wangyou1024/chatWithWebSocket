package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatServiceAPI {

    @GET("/chat/findSessionList")
    fun findSessionList(): Observable<ResponseData<MutableList<Chat>>>

    @GET("/chat/findChatList")
    fun findChatList(@Query("type") type: Int, @Query("id") id: Long): Observable<ResponseData<MutableList<Chat>>>
}