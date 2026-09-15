package com.liyaan.utils

fun Int.isSuccessZero(success:()-> Unit,fail:()-> Unit){
    if (this == 0) success() else fail()
}