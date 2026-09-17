package com.liyaan.utils

import kotlin.time.Clock


object TimeUtils {
    /**
     * 获取当前时间戳（毫秒）
     */
    fun getCurrentTimestampMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    /**
     * 获取当前时间戳（秒）
     */
    fun getCurrentTimestampSeconds(): Long {
        return Clock.System.now().epochSeconds
    }



    private fun Int.padZero(): String {
        return this.toString().padStart(2, '0')
    }
}
