package com.liyaan.kmp.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.liyaan.kmp.weight.SwiperImageView
import com.liyaan.kmp.weight.SwiperImageViewType
import kmp.app.shared.generated.resources.Res
import kmp.app.shared.generated.resources.one
import kmp.app.shared.generated.resources.third
import kmp.app.shared.generated.resources.two

@Composable
fun HomeView(){
    val bannerList = remember {
        listOf(
            "https://www.4kbizhi.com/d/file/2025/03/31/small131125TcyXK1743397885.jpg",
            "https://picsum.photos/id/1025/800/450",
             "https://picsum.photos/id/1074/800/450"
        )
    }
    val bannerRes = remember {
        listOf(
            Res.drawable.one,
            Res.drawable.two,
            Res.drawable.third
        )
    }
    Column {
        SwiperImageView(
            imageUrls = bannerList,
            autoPlayInterval = 3000L,
            onItemClick={ index ->
                println("点击了第${index+1}张轮播图")
            },
            modifier = Modifier.padding(10.dp)
        )
        SwiperImageView(
            type = SwiperImageViewType.RES_IMAGE,
            imageUrls = bannerRes,
            autoPlayInterval = 3000L,
            onItemClick={ index ->
                println("点击了第${index+1}张轮播图")
            },
            modifier = Modifier.padding(10.dp)
        )
    }
}