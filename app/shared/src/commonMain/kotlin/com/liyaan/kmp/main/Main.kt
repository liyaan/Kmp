package com.liyaan.kmp.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.liyaan.kmp.main.car.BuyCarView
import com.liyaan.kmp.main.`class`.ClassView
import com.liyaan.kmp.main.home.HomeView
import com.liyaan.kmp.main.one.OneSelfView
import com.liyaan.kmp.weight.BottomNavItem
import com.liyaan.kmp.weight.BottomNavigationView
import kmp.app.shared.generated.resources.Res
import kmp.app.shared.generated.resources.home
import kmp.app.shared.generated.resources.select_home
import org.jetbrains.compose.resources.imageResource

@Composable
fun MainView(){
    val navController = rememberNavController()
    val list = listOf(
        BottomNavItem("HOME", imageResource(Res.drawable.home),"首页",imageResource(Res.drawable.select_home),
            Color.Blue,Color.Black),
        BottomNavItem("MYCLASS", imageResource(Res.drawable.home),"分类",imageResource(Res.drawable.select_home),
            Color.Blue,Color.Black),
        BottomNavItem("BUYCAR", imageResource(Res.drawable.home),"购物车",imageResource(Res.drawable.select_home),
            Color.Blue,Color.Black),
        BottomNavItem("MY", imageResource(Res.drawable.home),"我的",imageResource(Res.drawable.select_home),
            Color.Blue,Color.Black)

    )
    Scaffold (
        bottomBar = {
            BottomNavigationView(
                navController = navController,
                modifierIcon = Modifier.size(35.dp,40.dp),
                fontSize = 16.sp,
                items = list
            )
        }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = list[0].route,
            modifier = Modifier.padding(innerPadding)
        ) {
            list.forEach { data->
                composable(data.route) {
                    when (data.route) {
                        "HOME" -> {
                            HomeView()
                        }
                        "MYCLASS" -> {
                            ClassView()
                        }
                        "BUYCAR" -> {
                            BuyCarView()
                        }
                        else -> {
                            OneSelfView()
                        }
                    }
                }
            }
        }

    }
}




