package com.example.discordcloneapp.view
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomTabBar(
    tabs: List<String>,
    modifier: Modifier = Modifier,
    gapWidth: Dp = 4.dp,
    defaultSelectedIndex: Int = 0,
    onTabChange: (Int) -> Unit = {}
) {

    var selectedIndex by remember {
        mutableStateOf(defaultSelectedIndex)
    }

    BoxWithConstraints(
        Modifier
            .height(48.dp)
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(8.dp))
            .padding(4.dp)
            .then(modifier)
    ) {

        val density = LocalDensity.current
        val width = constraints.maxWidth
        val gap = remember {
            with(density) { gapWidth.roundToPx() }
        }
        val tabWidthTotal = width - gap
        val indicatorWidth = tabWidthTotal / tabs.size
        val indicatorWidthDp = remember {
            with(density) { indicatorWidth.toDp() }
        }
        val indicatorPosition by animateDpAsState(
            targetValue = with(density) { ((indicatorWidth - gap) * selectedIndex).toDp() },
            animationSpec = tween(300)
        )

        Box(
            Modifier
                .fillMaxHeight()
                .width(indicatorWidthDp)
                .offset(x = indicatorPosition)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.background)
        )

        Row(
            Modifier.fillMaxWidth(),
            Arrangement.spacedBy(gapWidth)
        ) {

            tabs.forEachIndexed { index, value ->

                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {

                            if (index != selectedIndex) {
                                selectedIndex = index
                                onTabChange(selectedIndex)
                            }

                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        value,
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold
                    )

                }

            }

        }

    }

}