package com.liyaan.kmp.weight


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

/**
 * @param dataItems 数据列表 包含置顶数据的集合 对应T泛型
 * @param dataHeader 添加header数据集合 对应的H泛型
 * @param headerComposable 添加headerView
 * @param itemTopView 置顶数据的布局
 * @param itemView 指定数据下面的子布局
 * @param moreItemView 添加底部数据Footer
 *
 * ItemDataEntity 数据集合可以继承此类添加自己需要的数据 下次例子
 * data class Title(
 *     val headerContent: String,
 *     val indexNumber: Int,
 *     val contents: MutableList<Content>
 * ) :
 *     ItemDataEntity<Content>(headerContent, contents)
 * */
@Composable
fun <T, H> StickyHeaderView(
    dataItems: MutableList<ItemDataEntity<T>>,
    dataHeader: List<H>? = null,
    headerComposable: @Composable (Int) -> Unit = {},
    itemTopView: @Composable (ItemDataEntity<T>) -> Unit,
    itemView: @Composable (Int, T) -> Unit,
    moreItemView: @Composable () -> Unit = {}
) {
    LazyColumn {
        dataHeader?.let {
            items(dataHeader.size) { index ->
                headerComposable(index)
            }
        } ?: run {
            item {
                headerComposable(0)
            }
        }

        dataItems.forEach { data ->
            stickyHeader {
                itemTopView(data)
            }
            items(data.dataList.size) { index ->
                itemView(index, data.dataList[index])
            }
        }
        item {
            moreItemView()
        }
    }
}

open class ItemDataEntity<T>(val title: String, val dataList: MutableList<T>)