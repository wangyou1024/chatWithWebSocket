package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class Chat constructor() :Serializable {
    var cid: Long? = null
    var sender: Long? = null
    var recipient: Long? = null
    var gid: Long? = null
    var name: String? = null
    var content: String? = null
    var imageUrl: String? = null
    var updateTime: Long? = 0
    var enable: Int? = 0


    constructor(name: String?, content: String?, imageUrl: String?, updateTime: Long?) : this() {
        this.name = name
        this.content = content
        this.imageUrl = imageUrl
        this.updateTime = updateTime
    }

    constructor(cid: Long?, sender: Long?, recipient: Long?, gid: Long?, content: String?, updateTime: Long?, enable: Int) : this() {
        this.cid = cid
        this.sender = sender
        this.recipient = recipient
        this.gid = gid
        this.content = content
        this.updateTime = updateTime
        this.enable = enable
    }

}