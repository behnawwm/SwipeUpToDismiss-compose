package com.behnawwm.swipeuptodismiss

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                    val myComposeView = remember {
                        MyComposeView(view).apply {
                            setCustomContent(parentComposition) {
                                MyComposeViewContent(
                                    onCancel = {
                                        Toast.makeText(baseContext, "Canceled", Toast.LENGTH_SHORT).show()
                                    },
                                    onDismiss = {
                                        Toast.makeText(baseContext, "Dismissed", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    },
                                    onDismissButtonClicked = {
                                        Toast.makeText(baseContext, "Dismissed", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    }
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { myComposeView.show() }) {
                            Text(text = "Open")
                        }
                    }

                    DisposableEffect(myComposeView) {
                        onDispose {
                            myComposeView.dispose()
                        }
                    }
                }
            }
        }
    }
}
