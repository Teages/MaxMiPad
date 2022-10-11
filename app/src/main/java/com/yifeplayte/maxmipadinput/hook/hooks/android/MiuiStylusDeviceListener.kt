package com.yifeplayte.maxmipadinput.hook.hooks.android

import com.github.kyuubiran.ezxhelper.utils.*
import com.yifeplayte.maxmipadinput.hook.hooks.BaseHook
import de.robv.android.xposed.XposedBridge

object MiuiStylusDeviceListener : BaseHook() {
    override fun init() {
        try {
            findAllConstructors("com.miui.server.stylus.MiuiStylusDeviceListener") { true }.hookAfter {
                val ITouchFeature = loadClass("miui.util.ITouchFeature")
                val mTouchFeature = findMethod(ITouchFeature) {
                    name == "getInstance"
                }.invoke(null)
                findMethod(ITouchFeature) {
                    name == "setTouchMode" && paramCount == 3
                }.invoke(mTouchFeature, 0, 20, 1)
            }
            findAllMethods("com.miui.server.stylus.MiuiStylusDeviceListener") { true }.hookAfter {
                val ITouchFeature = loadClass("miui.util.ITouchFeature")
                val mTouchFeature = findMethod(ITouchFeature) {
                    name == "getInstance"
                }.invoke(null)
                findMethod(ITouchFeature) {
                    name == "setTouchMode" && paramCount == 3
                }.invoke(mTouchFeature, 0, 20, 1)
            }
            XposedBridge.log("MaxMiPadInput: Hook MiuiStylusDeviceListener success!")
        } catch (e: Throwable) {
            XposedBridge.log("MaxMiPadInput: Hook MiuiStylusDeviceListener failed!")
        }
    }
}
