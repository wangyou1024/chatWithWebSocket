package com.wangyou.chatwithwebsocket.conf

object Const {
    const val TAG = "wangyou"
    const val placeholder = "placeholder"

//    const val IP = "192.168.254.250"
    const val IP = "192.168.2.212"
    const val PORT = "8080"
    const val address = "http://$IP:$PORT"
    const val webSocket = "ws://$IP:$PORT/chat/websocket"
    const val broadcast = "/app/hello"
    const val broadcastResponse = "/topic/greetings"
    const val chat = "/app/chat"
    const val chatResponse = "/user/queue/chat"

    // 好友申请
    const val friendApplication = "/app/friendApplication"
    const val friendApplicationResponse = "/user/queue/friendApplication"

    const val groupApplication = "/app/groupApplication"
    const val groupApplicationResponse = "/user/queue/groupApplication"
}