package com.liyaan.kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.liyaan.common.entity.PopularRouteData
import com.liyaan.common.entity.RegisterData
import com.liyaan.common.entity.request.RegisterEntity
import com.liyaan.kmp.main.MainView
import com.liyaan.kmp.model.WanAppModel
import com.liyaan.kmp.model.WanAppState
import org.jetbrains.compose.resources.painterResource

import kmp.app.shared.generated.resources.Res
import kmp.app.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(
    viewModel: WanAppModel = viewModel { WanAppModel() }
) {
    val swiperUiState by viewModel.swiperUiState.collectAsState()

    val registerUiState by viewModel.registerUiState.collectAsState()
//    LaunchedEffect(Unit){
//        viewModel.getPopularRoute()
//    }
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
//                val registerData = RegisterEntity(
//                    "test_John_2026091400",
//                    "2222222",
//                    "2222222"
//                )
//                viewModel.postRegister(registerData)

                showContent = !showContent

            }) {
                Text("Click me!")


            }
            AnimatedVisibility(showContent) {
                MainView()
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                    ListViewApp(swiperUiState = swiperUiState)
//                    RegisterView(registerUiState = registerUiState)
//
//                }
            }
        }
    }
}

@Composable
fun ListViewApp(swiperUiState: WanAppState<List<PopularRouteData>>){
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        when(swiperUiState){
            is WanAppState.Error ->{
                Text(text = swiperUiState.message,
                    color = Color.Red,
                    fontSize = 18.sp,
                    )
            }
            is WanAppState.Loading -> {
                CircularProgressIndicator()
            }
            is WanAppState.Success ->{
                swiperUiState.data.forEach {
                    Text(text = it.name,
                        color = Color.Black,
                        fontSize = 18.sp,)
                }
            }
        }
    }
}

@Composable
fun RegisterView(registerUiState: WanAppState<RegisterData>){
    Column {

        when(registerUiState){
            is WanAppState.Loading ->{
                CircularProgressIndicator()
            }
            is WanAppState.Error->{
                Text(text = registerUiState.message,
                    color = Color.Red,
                    fontSize = 18.sp,
                )
            }
            is WanAppState.Success ->{
                Text(text = registerUiState.data.nickname,
                    color = Color.Gray,
                    fontSize = 18.sp,
                )
            }
        }
    }
}