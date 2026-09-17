package com.liyaan.kmp.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @param tabTitles Tab名称列表
 * @param headerHeight 可折叠头部高度（不包含Tab栏）
 * @param tabHeight Tab栏高度，默认50dp
 * @param headerContent 可折叠头部内容，参数为折叠进度（0=完全展开，1=完全收起）
 * @param listItemContent 列表项内容，参数为页索引、item索引、item数据
 *
 * @param selectedContentColor 设置Tab选中后的字体颜色
 * @param unselectedContentColor 设置Tab默认的字体颜色
 * @param text 设置Tab Text
 * @param divider 设置Tab 下划线
 */
@Composable
fun <T> StickyHeaderTabScaffold(
    tabTitles: List<String>,
    headerHeight: Dp,
    listData: List<List<T>>,
    tabHeight: Dp = 50.dp,
    headerContent: @Composable (collapseProgress: Float) -> Unit,
    listItemContent: @Composable (pageIndex: Int, itemIndex: Int, item: T) -> Unit,
    selectedContentColor : Color = Color.Blue,
    unselectedContentColor : Color =Color.Black,
    text: @Composable (() -> Unit)? = null,
    divider: @Composable () -> Unit = @Composable { HorizontalDivider() },
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val headerHeightPx = with(density) { headerHeight.toPx() }
    val totalHeaderHeight = headerHeight + tabHeight

    // 1. 状态初始化
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    // 每个Tab页对应一个独立的LazyListState，保存各自滚动位置
    val listStates = remember(tabTitles.size) {
        List(tabTitles.size) { LazyListState() }
    }

    // 2. 计算当前头部偏移量（核心逻辑：仅根据当前列表滚动位置计算，无事件拦截）
    val headerOffset = remember {
        derivedStateOf {
            val currentListState = listStates[pagerState.currentPage]
            val firstVisibleIndex = currentListState.firstVisibleItemIndex
            val scrollOffset = currentListState.firstVisibleItemScrollOffset

            when {
                // 列表已滚动超过第一个item：头部完全收起，吸顶
                firstVisibleIndex > 0 -> -headerHeightPx
                // 列表在第一个item：根据滚动偏移计算头部位置，最多收起整个头部高度
                else -> (-scrollOffset.toFloat()).coerceIn(-headerHeightPx, 0f)
            }
        }
    }

    // 折叠进度（0~1，可用于头部动画）
    val collapseProgress = remember {
        derivedStateOf { (headerOffset.value / -headerHeightPx).coerceIn(0f, 1f) }
    }

    // 3. 切换Tab时自动同步列表位置，避免头部状态跳变
    LaunchedEffect(pagerState.currentPage) {
        val targetListState = listStates[pagerState.currentPage]
        val targetScrollOffset = (-headerOffset.value).toInt()

        // 瞬间滚动到匹配位置，无动画，用户无感知
        if (targetListState.firstVisibleItemIndex != 0 ||
            abs(targetListState.firstVisibleItemScrollOffset - targetScrollOffset) > 10
        ) {
            targetListState.scrollToItem(
                index = 0,
                scrollOffset = targetScrollOffset.coerceIn(0, headerHeightPx.toInt())
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 4. 内容区：HorizontalPager + 每个页的LazyList
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val currentListState = listStates[pageIndex]
            LazyColumn(
                state = currentListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // 关键：第一个item加占位Spacer，高度等于头部总高度，避免内容被头部遮挡
                item {
                    Spacer(modifier = Modifier.height(totalHeaderHeight))
                }

                // 列表真实内容
                items(listData[pageIndex]) { item ->
                    listItemContent(pageIndex, listData[pageIndex].indexOf(item), item)
                }
            }
        }

        // 5. 悬浮头部+Tab栏（浮在列表上层）
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = headerOffset.value
                }
        ) {
            // 可折叠头部区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                headerContent(collapseProgress.value)
            }

            // Tab栏（吸顶后固定在顶部）
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(tabHeight),
                containerColor = MaterialTheme.colorScheme.surface,
                divider = divider
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            text?.let {
                                it()
                            }?: run {
                                Text(title)
                            }
                        },
                        selectedContentColor = selectedContentColor,
                        unselectedContentColor = unselectedContentColor
                    )
                }
            }
        }
    }
}
