package com.liyaan.common.entity

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T?
)


@Serializable
data class PopularRouteData(
    val desc: String,
    val cover: String,
    val name: String,
    val id: Int
)

@Serializable
data class RegisterData(
    val nickname :String,
    val id:Int
)