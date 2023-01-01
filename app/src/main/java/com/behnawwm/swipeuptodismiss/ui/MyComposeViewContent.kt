package com.behnawwm.swipeuptodismiss.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

const val DISMISS_Y_OFFSET = -500f
const val STATUS_BAR_SIZE = 56

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyComposeViewContent(
    onDismissButtonClicked: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    val swipeableState = rememberSwipeableState(0)

    val indicatorHeight = 100.dp
//    val indicatorHeightPx = indicatorHeight.toLocalDensityPx()
    val anchors = mapOf(0f to 0, DISMISS_Y_OFFSET to 1)

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
        modifier = Modifier
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
            .padding(top = 48.dp)
    ) {
        Box(
            Modifier
                .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
                .height(indicatorHeight)
                .fillMaxWidth()
                .background(Color.Red)
                .clickable {
                    onDismissButtonClicked()
                }
        )
    }
}

enum class SwipeableStates(val number: Int) {
    Canceled(0),
    Finished(1),
}