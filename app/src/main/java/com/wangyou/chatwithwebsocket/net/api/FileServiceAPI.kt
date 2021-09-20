package com.wangyou.chatwithwebsocket.net.api

import com.wangyou.chatwithwebsocket.net.response.ResponseData
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FileServiceAPI {

    @Multipart
    @POST("/file/uploadImage")
    fun uploadImage(@Part part: MultipartBody.Part, @Query("type") type: Int, @Query("id") id: Long): Observable<ResponseData<String>>
}