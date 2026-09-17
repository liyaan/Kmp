package com.liyaan.kmp.weight

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ToastView(
    state: ToastState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = state.isVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = state.message ?: "",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }

    // 自动隐藏逻辑
    LaunchedEffect(state.isVisible) {
        if (state.isVisible) {
            delay(state.duration.milliseconds)
            onDismiss()
        }else{
            if (state.list.isNotEmpty()){
                delay((state.duration+100).milliseconds)
                state.showToastData(state.list[0].content,state.list[0].showTime)
                state.list.removeAt(0)
            }

        }
    }
}

class ToastState {
    private var _message by mutableStateOf<String?>(null)
    val message: String? get() = _message

    private var _duration by mutableStateOf(0L)
    val duration: Long get() = _duration

    private var _isVisible by mutableStateOf(false)
    val isVisible: Boolean get() = _isVisible

    private val _list = mutableListOf<ShowToastData>()
    val list: MutableList<ShowToastData> get() = _list
    fun showToast(msg: String, duration: Long = 1000L) {
        if (_isVisible){
            _list.add(ShowToastData(msg,duration))
        }else{
            showToastData(msg,duration)
        }

    }
    fun showToastData(msg: String, duration: Long = 1000L) {
        _message = msg
        _duration = duration
        _isVisible = true

    }
    fun hideToast() {
        _duration = 0L
        _isVisible = false
        _message = null
    }

    data class ShowToastData(
        val content: String,
        val showTime: Long
    )
}

@Composable
fun rememberToastState(): ToastState {
    return remember { ToastState() }
}