package com.behnawwm.swipeuptodismiss.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.util.*

@SuppressLint("ViewConstructor")
class MyComposeView(
    composeView: View,
    saveID: UUID? = null,
    isNoLimitFlagEnabled: Boolean = false,
) : AbstractComposeView(composeView.context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var params: WindowManager.LayoutParams =
        WindowManager.LayoutParams().apply {
            gravity = Gravity.TOP
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            token = composeView.applicationWindowToken
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            format = PixelFormat.TRANSLUCENT
            flags =
                if (isNoLimitFlagEnabled)
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                else
                    flags
            windowAnimations = android.R.style.Animation_Translucent
        }

    private var isViewShowing = false

    init {
        ViewTreeLifecycleOwner.set(this, ViewTreeLifecycleOwner.get(composeView))
        ViewTreeViewModelStoreOwner.set(this, ViewTreeViewModelStoreOwner.get(composeView))
        setViewTreeSavedStateRegistryOwner(composeView.findViewTreeSavedStateRegistryOwner())
        saveID?.let {
            setTag(R.id.compose_view_saveable_id_tag, "ComposeView:$it")
        }
    }

    private var content: @Composable (modifier: Modifier) -> Unit by mutableStateOf({})
    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    @Composable
    override fun Content() {
        content(Modifier)
    }

    fun setCustomContent(
        parent: CompositionContext? = null,
        content: @Composable (modifier: Modifier) -> Unit
    ) {
        parent?.let {
            setParentCompositionContext(it)
        }
        this.content = content
        shouldCreateCompositionOnAttachedToWindow = true
    }

    fun show() {
        if (isViewShowing) dismiss()
        windowManager.addView(this, params)
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        windowManager.updateViewLayout(this, params)
        isViewShowing = true
    }

    fun dismiss() {
        if (!isViewShowing) return
        disposeComposition()
        windowManager.removeViewImmediate(this)
        isViewShowing = false
    }

    fun dispose() {
        dismiss()
        setViewTreeSavedStateRegistryOwner(null)
        ViewTreeLifecycleOwner.set(this, null)
        ViewTreeViewModelStoreOwner.set(this, null)
    }
}
