package com.wangyou.chatwithwebsocket.net.di

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.net.api.LoginServiceAPI
import com.wangyou.chatwithwebsocket.net.api.UserServiceAPI
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
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

import com.franmontiel.persistentcookiejar.cache.SetCookieCache

import com.franmontiel.persistentcookiejar.PersistentCookieJar

import com.franmontiel.persistentcookiejar.ClearableCookieJar


@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application): OkHttpClient {
        // 请求日志
        val interceptor = HttpLoggingInterceptor {
            Log.i(Const.TAG, it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
        // 持久化cookie
        val cookieJar: ClearableCookieJar =
            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(application))
        return OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(interceptor).build()
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
    fun provideLoginServiceAPI(retrofit: Retrofit): LoginServiceAPI {
        return retrofit.create(LoginServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserServiceAPI(retrofit: Retrofit): UserServiceAPI {
        return retrofit.create(UserServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCompositeDisposableLifecycle(): CompositeDisposableLifecycle {
        return CompositeDisposableLifecycle()
    }

    @Provides
    @Singleton
    fun provideCompositeToast(application: Application): Toast {
        return Toast(application)
    }
}