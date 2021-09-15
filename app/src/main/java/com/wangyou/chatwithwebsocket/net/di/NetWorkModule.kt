package com.wangyou.chatwithwebsocket.net.di

import android.util.Log
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.net.api.LoginAPI
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.i("wangyou", it)
        }).setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Const.address).client(okHttpClient)
            //添加对象转换
            .addConverterFactory(GsonConverterFactory.create())
            // 添加rxjava支持 Call -> Observable
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideLoginAPI(retrofit: Retrofit):LoginAPI{
        return retrofit.create(LoginAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCompositeDisposableLifecycle(): CompositeDisposableLifecycle{
        return CompositeDisposableLifecycle()
    }
}