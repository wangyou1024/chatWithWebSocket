package com.wangyou.chatwithwebsocket.net.di

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.wangyou.chatwithwebsocket.conf.Const
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
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.net.api.*
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


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
    fun provideStompClient(okHttpClient: OkHttpClient): StompClient {
        // okHttpClient中含有登录信息，无需再另外添加header
        val stompClient =
            Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.webSocket, null, okHttpClient)
        // stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000)
        return stompClient
    }

    /**
     * 控制stompClient生命周期
     */
    @Provides
    @Singleton
    fun provideStompClientLifeCycle(
        stompClient: StompClient,
        compositeDisposableLifecycle: CompositeDisposableLifecycle,
        toast: Toast
    ): StompClientLifecycle {
        val stompClientLifecycle =
            StompClientLifecycle(stompClient, compositeDisposableLifecycle, toast)
        val subscribe = stompClient.lifecycle()?.subscribe { lifecycleEvent: LifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> Log.d(Const.TAG, "Stomp 连接开启")
                LifecycleEvent.Type.ERROR -> {
                    Log.e(Const.TAG, "Stomp 连接异常", lifecycleEvent.exception)
                }
                LifecycleEvent.Type.CLOSED -> Log.d(Const.TAG, "Stomp 连接断开")
                else -> Log.d(Const.TAG, "Stomp 未知异常")
            }
        }
        if (subscribe != null) {
            compositeDisposableLifecycle.addDisposable(subscribe)
        }
        return stompClientLifecycle
    }

    @Provides
    @Singleton
    fun provideCompositeDisposableLifecycle(): CompositeDisposableLifecycle {
        return CompositeDisposableLifecycle()
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
    fun provideUserRelationServiceAPI(retrofit: Retrofit): UserRelationServiceAPI {
        return retrofit.create(UserRelationServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupServiceAPI(retrofit: Retrofit): GroupServiceAPI {
        return retrofit.create(GroupServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupRelationServiceAPI(retrofit: Retrofit): GroupRelationServiceAPI {
        return retrofit.create(GroupRelationServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideChatServiceAPI(retrofit: Retrofit): ChatServiceAPI {
        return retrofit.create(ChatServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideFileServiceAPI(retrofit: Retrofit): FileServiceAPI {
        return retrofit.create(FileServiceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCompositeToast(application: Application): Toast {
        return Toast.makeText(application, "", Toast.LENGTH_LONG)
    }
}