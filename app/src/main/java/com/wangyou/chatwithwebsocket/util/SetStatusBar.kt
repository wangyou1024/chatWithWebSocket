package com.wangyou.chatwithwebsocket.util

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

object SetStatusBar {
    fun setStatusColor(color: Int, window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
            // setDarkStatusIcon(true,window);
        }
    }

    /**
     * 这是状态栏文字反色
     * @param isDark
     * @param window
     */
    internal fun setDarkStatusIcon(isDark: Boolean, window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            if (decorView != null) {
                var vis = decorView.systemUiVisibility
                vis = if (isDark) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                decorView.systemUiVisibility = vis
            }
        }
    }
}