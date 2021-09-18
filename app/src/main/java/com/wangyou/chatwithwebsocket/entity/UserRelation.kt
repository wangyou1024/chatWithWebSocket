package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class UserRelation: Serializable {

    var urid: Long? = null
    var uidFormer: Long? = null
    var uidLatter: Long? = null
    var updateTime: Int? = null
    var readTime: Int? = null

    /**
     * 是否可用，0：未操作，1：拒绝，2：同意，3：删除，4：从关系(实际有效内容为read_time)
     */
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

    companion object{
        const val NO_DEAL = 0
        const val REFUSE = 1
        const val AGREE = 2
        const val DELETE = 3
        const val DEPENDENCE = 4
    }
    override fun toString(): String {
        return super.toString()
    }
}