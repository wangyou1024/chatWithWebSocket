package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class GroupRelation : Serializable{
    var grid: Long? = null
    var gid: Long? = null
    var uid: Long? = null
    var updateTime: Int? = null
    var readTime: Int? = null
    var enable: Int? = null

    constructor(
        grid: Long?,
        gid: Long?,
        uid: Long?,
        updateTime: Int?,
        readTime: Int?,
        enable: Int?
    ) {
        this.grid = grid
        this.gid = gid
        this.uid = uid
        this.updateTime = updateTime
        this.readTime = readTime
        this.enable = enable
    }
}