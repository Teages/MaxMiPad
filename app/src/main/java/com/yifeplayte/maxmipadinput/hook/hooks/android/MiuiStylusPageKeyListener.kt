package com.yifeplayte.maxmipadinput.hook.hooks.android

import android.os.Build
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yifeplayte.maxmipadinput.hook.hooks.BaseHook

object MiuiStylusPageKeyListener : BaseHook() {
    override fun init() {
        val pageKeyListenerClassName =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                "com.miui.server.input.stylus.MiuiStylusPageKeyListener"
            else "com.miui.server.stylus.MiuiStylusPageKeyListener"

        findMethod(pageKeyListenerClassName) {
            name == "isPageKeyEnable"
        }.hookReturnConstant(false)

        findMethod(pageKeyListenerClassName) {
            name == "needInterceptBeforeDispatching"
        }.hookReturnConstant(false)

        findMethod(pageKeyListenerClassName) {
            name == "interceptScreenShotKey"
        }.hookReturnConstant(false)

        findMethod(pageKeyListenerClassName) {
            name == "interceptQuickNoteKey"
        }.hookReturnConstant(false)
    }
}