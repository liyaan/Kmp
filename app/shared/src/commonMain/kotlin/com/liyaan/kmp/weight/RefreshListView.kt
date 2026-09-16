package com.liyaan.kmp.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged


/**
 * KMP 1.11.1 优化版：下拉刷新 + 上拉加载列表
 *
 * @param items 数据列表
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMore 是否有更多数据
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param itemContent 列表项内容
 * @param headerView 头部布局
 */
@Composable
fun <T>RefreshListView(
    items: List<T>,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    headerView:@Composable ()->Unit? = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (index: Int, item: T) -> Unit
){
    val pullToRefreshState = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    // 优化：使用 derivedStateOf 计算是否到达底部，避免每次重组都计算
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            if (totalItemsCount == 0) return@derivedStateOf false

            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            // 提前2个item触发加载，提升体验
            lastVisibleItemIndex >= totalItemsCount - 2
        }
    }

    // 监听滚动状态，触发上拉加载
    LaunchedEffect(lazyListState, isLoadingMore, hasMore, isRefreshing) {
        snapshotFlow { isAtBottom }
            .distinctUntilChanged() // 只有状态变化时才触发
            .collect { atBottom ->
                if (atBottom && !isLoadingMore && hasMore && !isRefreshing) {
                    onLoadMore()
                }
            }
    }

    // 使用最新的 PullToRefreshBox
    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                headerView()
            }
            itemsIndexed(items) { index, item ->
                itemContent(index, item)
            }

            // 底部加载状态指示器
            item {
                LoadMoreFooter(
                    isLoadingMore = isLoadingMore,
                    hasMore = hasMore
                )
            }
        }
    }
}

@Composable
private fun LoadMoreFooter(
    isLoadingMore: Boolean,
    hasMore: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoadingMore -> {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.Gray
                    )
                    Text("加载中...", fontSize = 14.sp, color = Color.Gray)
                }
            }
            !hasMore -> {
                Text("—— 没有更多数据了 ——", fontSize = 14.sp, color = Color.Gray)
            }
            else -> {
                // 还有数据但未触发加载，不显示任何内容，或者显示"上拉加载更多"
            }
        }
    }
}