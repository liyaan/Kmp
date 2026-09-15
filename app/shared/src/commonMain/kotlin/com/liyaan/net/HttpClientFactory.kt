package com.liyaan.net

import io.ktor.client.HttpClient

expect object HttpClientFactory{
    fun create(): HttpClient
}