package com.behnawwm.swipeuptodismiss

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.behnawwm.swipeuptodismiss.ui.MyComposeView
import com.behnawwm.swipeuptodismiss.ui.MyComposeViewContent
import com.behnawwm.swipeuptodismiss.ui.theme.SwipeUpToDismissTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeUpToDismissTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    val view = LocalView.current
                    val parentComposition = rememberCompositionContext()

                    val myComposeViewList = remember {
                        mutableStateListOf(
                            MyComposeView(view).apply {
                                setCustomContent(parentComposition) {
                                    MyComposeViewContent(
                                        index = 0,
                                        onCancel = {},
                                        onDismiss = {
                                            dismiss()
                                        },
                                        onDismissButtonClicked = {
                                            dismiss()
                                        },
                                        modifier = it
                                    )
                                }
                            }
                        )
                    }
                    myComposeViewList.forEach {
                        it.show()
                    }

                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Button(
                            onClick = {
//                                myComposeViewList.forEach {
//                                    it.show()
//                                }
                            }
                        ) {
                            Text(text = "Open")
                        }
                        Button(
                            onClick = {
                                Log.d(
                                    "TAG",
                                    "currentList: ${myComposeViewList.size} lastIndex: ${myComposeViewList.lastIndex}"
                                )
                                myComposeViewList.add(
                                    MyComposeView(view).apply {
                                        setCustomContent(parentComposition) {
                                            MyComposeViewContent(
                                                index = myComposeViewList.lastIndex,
                                                onCancel = {},
                                                onDismiss = {
                                                    myComposeViewList.removeLastOrNull()
                                                    dismiss()
                                                },
                                                onDismissButtonClicked = {
                                                    myComposeViewList.removeLastOrNull()
                                                    dismiss()
                                                },
                                                modifier = it
                                            )
                                        }
                                    }
                                )
                            }
                        ) {
                            Text(text = "Add item")
                        }
                        Button(
                            onClick = {
                                val removedITem = myComposeViewList.removeLastOrNull()
                                removedITem?.dismiss()
                            }
                        ) {
                            Text(text = "Remove item")
                        }
                    }

                    DisposableEffect(myComposeViewList) {
                        onDispose {
                            myComposeViewList.forEach {
                                it.dispose()
                            }
                        }
                    }
                }
            }
        }
    }
}
