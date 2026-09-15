package com.liyaan.base

import com.liyaan.common.WanandroidService
import com.liyaan.net.HttpClientFactory

object AppConstants {
    // 1. 内部持有通过工厂创建的 HttpClient 实例
    private val httpClient = HttpClientFactory.create()
    const val BASE_URL = ""

    // 3. 对外暴露各个功能模块的 Service 实例
    val wDataBase: WanandroidService by lazy {
        WanandroidService(httpClient, BASE_URL)
    }
}