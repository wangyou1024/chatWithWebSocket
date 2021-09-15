package com.wangyou.chatwithwebsocket.conf

object Const {
    const val TAG = "wangyou"
    const val placeholder = "placeholder"

    const val IP = "192.168.133.250"
    const val PORT = "8080"
    const val address = "http://$IP:$PORT"
    const val webSocket = "ws://$IP:$PORT/chat/websocket"
    const val broadcast = "/app/hello"
    const val broadcastResponse = "/topic/greetings"
    const val chat = "/app/chat"
    const val chatResponse = "/user/queue/chat"
}