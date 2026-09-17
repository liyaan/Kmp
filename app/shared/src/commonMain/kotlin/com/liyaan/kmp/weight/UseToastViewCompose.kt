package com.liyaan.kmp.weight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun UseToastViewCompose(composer:@Composable (ToastState)->Unit){
    val toastState = rememberToastState()
    val showScope = rememberCoroutineScope()

    composer(toastState)

    ToastView(
        state = toastState,
        onDismiss = { toastState.hideToast() }
    )
}