package com.liyaan.common

import com.liyaan.common.entity.BaseResponse
import com.liyaan.common.entity.PopularRouteData
import com.liyaan.common.entity.RegisterData
import com.liyaan.common.entity.request.RegisterEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType

class WanandroidService(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {

    suspend fun getPopularRoute(): BaseResponse<List<PopularRouteData>> {
        return httpClient.get("$baseUrl/popular/route/json").body()
    }

    suspend fun postUserRegister(register: RegisterEntity): BaseResponse<RegisterData> {
        return httpClient.submitForm ("$baseUrl/user/register",
            formParameters =Parameters.build {
                append("username", register.username)
                append("password", register.password)
                append("repassword", register.repassword)
            }).body()
    }
}