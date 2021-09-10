package com.wangyou.chatwithwebsocket.entity

import java.io.Serializable

class User constructor(): Serializable {
    var uid: Long? = null
    var username: String? = null
    var password: String? = null
    var realName: String? = null
    var imageUrl: String? = null
    var phone: String? = null
    var age: Int? = 0
    var address: String? = null
    var email: String? = null
    var gender: Int? = null
    var introduce: String? = null
    var updateTime: Int? = null
    var enable: Int? = null
    var locked: Int? = null

    constructor(
        uid: Long?,
        username: String?,
        password: String?,
        realName: String?,
        imageUrl: String?,
        phone: String?,
        age: Int?,
        address: String?,
        email: String?,
        gender: Int?,
        introduce: String?,
        updateTime: Int?,
        enable: Int?,
        locked: Int?
    ):this() {
        this.uid = uid
        this.username = username
        this.password = password
        this.realName = realName
        this.imageUrl = imageUrl
        this.phone = phone
        this.age = age
        this.address = address
        this.email = email
        this.gender = gender
        this.introduce = introduce
        this.updateTime = updateTime
        this.enable = enable
        this.locked = locked
    }
}