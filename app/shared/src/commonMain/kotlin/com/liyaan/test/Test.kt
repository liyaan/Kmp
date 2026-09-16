package com.liyaan.test

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

data class DemoItem(val id: Int, val title: String)

suspend fun fetchPage(page: Int): List<DemoItem> {
    delay(1500.milliseconds) // 模拟网络延迟
    if (page > 3) return emptyList() // 模拟只有3页数据

    return List(10) { index ->
        DemoItem(
            id = (page - 1) * 10 + index,
            title = "Item ${ (page - 1) * 10 + index + 1 } - Page $page"
        )
    }
}