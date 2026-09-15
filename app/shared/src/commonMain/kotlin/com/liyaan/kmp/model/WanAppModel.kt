package com.liyaan.kmp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liyaan.base.AppConstants
import com.liyaan.common.entity.PopularRouteData
import com.liyaan.common.entity.RegisterData
import com.liyaan.common.entity.request.RegisterEntity
import com.liyaan.utils.isSuccessZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed interface WanAppState<out T> {
    // Loading 和 Error 状态通常不关心数据类型，可以保持不变
    data object Loading : WanAppState<Nothing>
    data class Error(val message: String) : WanAppState<Nothing>

    // 2. Success 状态现在可以持有任何类型的数据
    data class Success<out T>(val data: T) : WanAppState<T>
}


class WanAppModel : ViewModel(){
    //测试
    private val _swiperUiState = MutableStateFlow<WanAppState<List<PopularRouteData>>>(WanAppState.Loading)
    val swiperUiState: StateFlow<WanAppState<List<PopularRouteData>>> = _swiperUiState.asStateFlow()


    private val _registerUiState = MutableStateFlow<WanAppState<RegisterData>>(WanAppState.Loading)
    val registerUiState: StateFlow<WanAppState<RegisterData>> = _registerUiState.asStateFlow()

    fun getPopularRoute(){
        viewModelScope.launch {
            _swiperUiState.value = WanAppState.Loading
           try{
               val data = AppConstants.wDataBase.getPopularRoute()
               if (data.errorCode == 0){
                   _swiperUiState.value = WanAppState.Success(data.data!!)
               }else{
                   _swiperUiState.value = WanAppState.Error(data.errorMsg)
               }
           }catch (e: Exception){
               e.printStackTrace()
               _swiperUiState.value = WanAppState.Error("网络错误: ${e.message}")
           }
        }
    }

    fun postRegister(registerData: RegisterEntity){
        viewModelScope.launch {
            _registerUiState.value = WanAppState.Loading
            try{
                val data = AppConstants.wDataBase.postUserRegister(registerData)
                if (data.errorCode == 0){
                    _registerUiState.value = WanAppState.Success(data.data!!)
                }else{
                    _registerUiState.value = WanAppState.Error(data.errorMsg)
                }
            }catch (e: Exception){
                e.printStackTrace()
                _registerUiState.value = WanAppState.Error("网络错误: ${e.message}")
            }
        }
    }
}