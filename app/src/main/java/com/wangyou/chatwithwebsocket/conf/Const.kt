package com.wangyou.chatwithwebsocket.conf

object Const {
    const val TAG = "wangyou"

//    const val IP = "192.168.32.250"
    const val IP = "baiyou1024.top"
    const val PORT = "8082"
    // address作为retrofit的baseUrl时，建议采用baseUrl以/结尾，
    // API不以/开头，具体原因可以查看baseUrl()方法的注释
    const val address = "http://$IP:$PORT/"
    const val webSocket = "ws://$IP:$PORT/chat/websocket"
    const val imageUrl = "http://$IP:$PORT/file/imageHeader"

    // 聊天
    const val chat = "/app/chat"
    const val chatResponse = "/user/queue/chat"

    // 好友申请
    const val friendApplication = "/app/friendApplication"
    const val friendApplicationResponse = "/user/queue/friendApplication"

    // 群申请
    const val groupApplication = "/app/groupApplication"
    const val groupApplicationResponse = "/user/queue/groupApplication"
}