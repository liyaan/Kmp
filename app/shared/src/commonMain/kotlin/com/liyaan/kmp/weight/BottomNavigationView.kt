package com.liyaan.kmp.weight

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun BottomNavigationView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    modifierIcon: Modifier,
    fontSize: TextUnit,
    items: List<BottomNavItem>
) {
    var currentRoute by rememberSaveable { mutableStateOf(items[0].route) }
    NavigationBar(
        modifier = modifier,
        containerColor = NavigationBarDefaults.containerColor,
        contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor),
        tonalElevation = NavigationBarDefaults.Elevation,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        currentRoute = item.route
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true // 保存Tab切换前的状态
                            }
                            launchSingleTop = true
                            restoreState = true // 切回Tab时恢复状态
                        }
                    }
                },
                icon = {
                    Icon(if (currentRoute==item.route) item.selectIcon else item.icon,
                        contentDescription = item.label, modifier =modifierIcon
                    )
                },
                label = { Text(item.label,
                    color = if (currentRoute==item.route) item.labelColor else item.labelColored,
                    fontSize = fontSize) }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageBitmap,
    val label: String,
    val selectIcon: ImageBitmap,
    val labelColor: Color,
    val labelColored: Color
)