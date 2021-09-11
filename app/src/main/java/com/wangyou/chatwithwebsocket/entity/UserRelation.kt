package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class UserRelation: Serializable {

    var urid: Long? = null
    var uidFormer: Long? = null
    var uidLatter: Long? = null
    var updateTime: Int? = null
    var readTime: Int? = null
    var enable: Int? = null

    constructor(
        urid: Long?,
        uidFormer: Long?,
        uidLatter: Long?,
        updateTime: Int?,
        readTime: Int?,
        enable: Int?
    ) {
        this.urid = urid
        this.uidFormer = uidFormer
        this.uidLatter = uidLatter
        this.updateTime = updateTime
        this.readTime = readTime
        this.enable = enable
    }
}