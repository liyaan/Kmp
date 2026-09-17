package com.liyaan.kmp.main.`class`

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.liyaan.kmp.weight.ItemDataEntity
import com.liyaan.kmp.weight.StickyHeaderView

@Composable
fun ClassView() {
    val items = listOf("A", "B", "C", "D", "E") // 模拟数据
    StickyHeaderView<Content, String>(
        data(),
        dataHeader = items,
        headerComposable = { index->
            ListItem(index = items[index])
        },
        itemTopView = {
            ListTopItem(it as Title)
        },
        itemView = { index, item ->
            ListItem(index = item.content)
        },
        moreItemView = {
            items.forEach {
                ListItem(index = "$it moreItemView")
            }
        }
    )
}



@Composable
fun ListTopItem(data: Title) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text("我是吸顶头部${data.headerContent} ${data.indexNumber}", color = Color.White)
    }
}

@Composable
fun ListItem(index: String) {
    Text(
        text = "Item $index",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

data class Title(
    val headerContent: String,
    val indexNumber: Int,
    val contents: MutableList<Content>
) :
    ItemDataEntity<Content>(headerContent, contents)

data class Content(val content: String)

fun data(): MutableList<ItemDataEntity<Content>> {
    val list = mutableListOf<ItemDataEntity<Content>>()

    list.add(Title("A", 1, contentData(2)))
    list.add(Title("B", 2, contentData(3)))
    list.add(Title("C", 3, contentData(3)))
    list.add(Title("D", 4, contentData(10)))
    list.add(Title("E", 5, contentData(6)))
    return list
}

fun contentData(number: Int): MutableList<Content> {
    val data = mutableListOf<Content>()
    for (i in 0 until number) {
        data.add(Content("ssssss=$i"))
    }
    return data
}