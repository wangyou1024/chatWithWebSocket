package com.wangyou.chatwithwebsocket

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import ua.naiksoftware.stomp.StompClient
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlin.Throws
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.wangyou.chatwithwebsocket.conf.Const
import ua.naiksoftware.stomp.dto.StompHeader
import io.reactivex.subscribers.DisposableSubscriber
import okhttp3.*
import ua.naiksoftware.stomp.dto.StompMessage
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp() =
            Navigation.findNavController(this, R.id.fragment).navigateUp()
}