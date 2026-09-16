package com.liyaan.kmp.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.liyaan.kmp.weight.RefreshListView
import com.liyaan.kmp.weight.SwiperImageView
import com.liyaan.kmp.weight.SwiperImageViewType
import com.liyaan.test.DemoItem
import com.liyaan.test.fetchPage
import kmp.app.shared.generated.resources.Res
import kmp.app.shared.generated.resources.one
import kmp.app.shared.generated.resources.third
import kmp.app.shared.generated.resources.two
import kotlinx.coroutines.launch

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


    val scope = rememberCoroutineScope()

    // 状态管理
    val items = remember { mutableStateListOf<DemoItem>() }
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var hasMore by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(1) }

    // 初始加载
    LaunchedEffect(Unit) {
        isRefreshing = true
        val data = fetchPage(1)
        items.clear()
        items.addAll(data)
        currentPage = 1
        hasMore = data.isNotEmpty()
        isRefreshing = false
    }

    // 刷新逻辑
    fun onRefresh() {
        scope.launch {
            isRefreshing = true
            val data = fetchPage(1)
            items.clear()
            items.addAll(data)
            currentPage = 1
            hasMore = data.isNotEmpty()
            isRefreshing = false
        }
    }

    // 加载更多逻辑
    fun onLoadMore() {
        scope.launch {
            isLoadingMore = true
            val nextPage = currentPage + 1
            val data = fetchPage(nextPage)

            if (data.isEmpty()) {
                hasMore = false
            } else {
                items.addAll(data)
                currentPage = nextPage
            }
            isLoadingMore = false
        }
    }

    Column {
        SwiperImageView(
            imageUrls = bannerList,
            autoPlayInterval = 3000L,
            onItemClick={ index ->
                println("点击了第${index+1}张轮播图")
            },
            aspectRatio = 16f/6f,
            modifier = Modifier.padding(10.dp)
        )


        RefreshListView(
            items = items,
            isRefreshing = isRefreshing,
            isLoadingMore = isLoadingMore,
            hasMore = hasMore,
            onRefresh = ::onRefresh,
            onLoadMore = ::onLoadMore,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            headerView = {
                SwiperImageView(
                    type = SwiperImageViewType.RES_IMAGE,
                    imageUrls = bannerRes,
                    autoPlayInterval = 3000L,
                    onItemClick={ index ->
                        println("点击了第${index+1}张轮播图")
                    },
                    aspectRatio = 16f/6f
                )
            }
        ) { _, item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

    }
}