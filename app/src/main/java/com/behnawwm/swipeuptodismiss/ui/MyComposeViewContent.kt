package com.behnawwm.swipeuptodismiss.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.behnawwm.swipeuptodismiss.ui.data.Item
import com.behnawwm.swipeuptodismiss.ui.data.SwipeableStates
import kotlin.math.roundToInt

const val ITEM_HEIGHT = 80
const val ITEM_SPACE_SIZE = 8
const val DISMISS_Y_OFFSET = -500f
const val STATUS_BAR_SIZE = 56

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyComposeViewContent(
    index: Int,
    onDismissButtonClicked: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d("TAG", "content: $index")
    val swipeableState = rememberSwipeableState(0)
//    val indicatorHeightPx = indicatorHeight.toLocalDensityPx()

    val anchors = mapOf(
        0f to 0,
        DISMISS_Y_OFFSET to 1
    )

    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeableState.currentValue) {
                    SwipeableStates.Canceled.number -> onCancel()
                    SwipeableStates.Finished.number -> onDismiss()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical,
            )
//            .background(Color.White)
//            .offset { IntOffset(0, STATUS_BAR_SIZE) }
            .padding(
                top = calculateTopPadding(index)
            )
    ) {
        Box(
            Modifier
                .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
                .height(ITEM_HEIGHT.dp)
                .fillMaxWidth()
                .background(Color.Red)
                .clickable {
                    onDismissButtonClicked()
                }
        ) {

        }
    }
}

fun calculateTopPadding(index: Int): Dp {
    Log.d("TAG", "index: $index height:${16.dp + (index * ITEM_HEIGHT).dp + (index * ITEM_SPACE_SIZE).dp}")
    if (index <= 0)
        return 16.dp
    return 16.dp + (index * ITEM_HEIGHT).dp + (index * ITEM_SPACE_SIZE).dp
}
