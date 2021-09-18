package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class Group constructor(): Serializable {
    var gid: Long? = null
    var groupNum: String? = null
    var groupName: String? = null
    var groupImage: String? = null
    var introduce: String? = null
    var updateTime: Int? = null
    var enable: Int? = null

    constructor(
        gid: Long?,
        groupNum: String?,
        groupName: String?,
        groupImage: String?,
        introduce: String?,
        updateTime: Int?,
        enable: Int?
    ) : this() {
        this.gid = gid
        this.groupNum = groupNum
        this.groupName = groupName
        this.groupImage = groupImage
        this.introduce = introduce
        this.updateTime = updateTime
        this.enable = enable
    }
}