package com.liyaan.kmp.weight

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kmp.app.shared.generated.resources.Res
import kmp.app.shared.generated.resources.banner
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource

import org.jetbrains.compose.resources.painterResource
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds

enum class SwiperImageViewType{
    NET_IMAGE,
    RES_IMAGE
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> SwiperImageView(
    type:SwiperImageViewType = SwiperImageViewType.NET_IMAGE,
    imageUrls: List<T>,
    autoPlayInterval: Long = 3000L,
    autoPlay: Boolean = true,
    isHot: Boolean = true,
    cornerRadius: Int = 12,
    aspectRatio: Float = 16f / 9f,
    onItemClick: (index: Int) -> Unit = { _ -> },
    modifier: Modifier = Modifier
){
    if (imageUrls.isEmpty()){
        Card(
            shape = RoundedCornerShape(cornerRadius.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        ) {
            Image(
                painter = painterResource(Res.drawable.banner),
                contentDescription = "轮播图占位",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // 无限轮播配置
    val infinitePageCount = if (imageUrls.size > 1) Int.MAX_VALUE else 1
    val initialPage = if (imageUrls.size > 1) Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % imageUrls.size else 0
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { infinitePageCount })

    // 自动轮播逻辑：解决轮播不动问题
    LaunchedEffect(pagerState, autoPlay, autoPlayInterval) {
        if (!autoPlay || imageUrls.size <= 1) return@LaunchedEffect
        while (true) {
            delay(autoPlayInterval.milliseconds)
            // 用户滑动时暂停自动轮播，避免冲突
            if (!pagerState.isScrollInProgress) {
                val nextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }
    Box(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 0.dp),
            pageSpacing = 0.dp,
        ){page ->

            // 映射无限页码到真实数据索引
            val actualIndex = (page - initialPage).mod(imageUrls.size).let {
                if (it < 0) it + imageUrls.size else it
            }
            val imageUrl = imageUrls[actualIndex]
            Card(
                shape = RoundedCornerShape(cornerRadius.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .graphicsLayer {
                        // 计算页面偏移，实现缩放+淡入淡出切换效果
                        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        val absOffset = pageOffset.absoluteValue.coerceIn(0f, 1f)
                        alpha = 1f - 0.15f * absOffset
                        scaleX = 1f - 0.06f * absOffset
                        scaleY = 1f - 0.06f * absOffset
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { onItemClick(actualIndex) }
                    }
            ) {
                if (type == SwiperImageViewType.NET_IMAGE) {
                    AsyncImage(
                        model = imageUrl as String,
                        contentDescription = "轮播图$actualIndex",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (type == SwiperImageViewType.RES_IMAGE) {
                    // 本地composeResources资源加载：图片放在commonMain/composeResources/drawable/下
                    Image(
                        painterResource(imageUrl as DrawableResource),
                        contentDescription = "轮播图$actualIndex",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        if (isHot){
            // 底部指示器：带平滑选中动画
            CarouselDotIndicator(
                totalCount = imageUrls.size,
                currentIndex = (pagerState.currentPage - initialPage).mod(imageUrls.size).let {
                    if (it < 0) it + imageUrls.size else it
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }

    }

}

/**
 * 轮播图圆点指示器：全平台通用
 */
@Composable
private fun CarouselDotIndicator(
    totalCount: Int,
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalCount) { index ->
            val isSelected = index == currentIndex
            val dotWidth by animateDpAsState(
                targetValue = if (isSelected) 18.dp else 8.dp,
                label = "indicator_animation"
            )
            Box(
                modifier = Modifier
                    .width(dotWidth)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
                    )
            )
        }
    }
}