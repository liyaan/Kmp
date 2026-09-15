package com.liyaan.common.entity.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterEntity(
    val username: String,
    val password: String,
    val repassword: String
)
