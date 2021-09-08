package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class Chat constructor() :Serializable {
    var name: String? = null
    var content: String? = null
    var imageUrl: String? = null
    var updateTime: Long? = 0


    constructor(name: String?, content: String?, imageUrl: String?, updateTime: Long?) : this() {
        this.name = name
        this.content = content
        this.imageUrl = imageUrl
        this.updateTime = updateTime
    }

}