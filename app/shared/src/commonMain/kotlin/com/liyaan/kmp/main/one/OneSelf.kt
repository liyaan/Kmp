package com.liyaan.kmp.main.one

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.liyaan.kmp.weight.StickyHeaderTabScaffold

@Composable
fun OneSelfView(){
    // 模拟测试数据：3个Tab页，每个页50条数据
    val tabTitles = listOf("推荐", "热门", "最新")
    val listData = remember {
        List(3) { page ->
            List(50) { index ->
                "Tab${page+1} 第${index+1}条内容"
            }
        }
    }

    StickyHeaderTabScaffold(
        tabTitles = tabTitles,
        // 头部高度200dp
        headerHeight = 200.dp,
        listData = listData,
        headerContent = { progress ->
            // 自定义头部内容（可根据progress做动画）
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "折叠进度: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    // 示例：折叠时文字逐渐缩小+透明
                    modifier = Modifier.graphicsLayer {
                        alpha = 1f - progress * 0.5f
                        scaleX = 1f - progress * 0.3f
                        scaleY = 1f - progress * 0.3f
                    }
                )
            }
        },
        listItemContent = { _, _, content ->
            // 自定义列表项样式
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(80.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = content,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}
