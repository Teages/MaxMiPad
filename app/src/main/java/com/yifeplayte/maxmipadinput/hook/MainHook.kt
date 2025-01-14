package com.yifeplayte.maxmipadinput.hook

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import com.yifeplayte.maxmipadinput.hook.hooks.BaseHook
import com.yifeplayte.maxmipadinput.hook.hooks.android.*
import com.yifeplayte.maxmipadinput.hook.hooks.home.GestureOperationHelper
import com.yifeplayte.maxmipadinput.utils.XSharedPreferences
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

private const val TAG = "MaxMiPad"
private val PACKAGE_NAME_HOOKED = setOf(
    "android",
    "com.miui.home",
)

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName in PACKAGE_NAME_HOOKED) {
            // Init EzXHelper
            EzXHelperInit.initHandleLoadPackage(lpparam)
            EzXHelperInit.setLogTag(TAG)
            EzXHelperInit.setToastTag(TAG)
            // Init hooks
            when (lpparam.packageName) {
                "android" -> {
                    if (XSharedPreferences.getBoolean("no_magic_pointer", true)) {
                        initHooks(MiuiMagicPointerUtils)
                        initHooks(SystemServerImpl)
                    }
                    if (XSharedPreferences.getBoolean("restore_esc", true)) {
                        initHooks(SwitchPadMode)
                        initHooks(SetPadMode)
                    }
                    if (XSharedPreferences.getBoolean("remove_stylus_bluetooth_restriction", true)) {
                        initHooks(MiuiStylusDeviceListener)
                    }
                    if (XSharedPreferences.getBoolean("ignore_stylus_key_gesture", false)) {
                        initHooks(MiuiStylusPageKeyListener)
                    }
                    if (XSharedPreferences.getBoolean("disable_fixed_orientation", true)) {
                        initHooks(MiuiFixedOrientationController)
                    }
                    if (XSharedPreferences.getBoolean("set_gesture_need_finger_num_to_4", false)) {
                        initHooks(BaseMiuiMultiFingerGesture)
                    }
                }
                "com.miui.home" -> {
                    if (XSharedPreferences.getBoolean("set_gesture_need_finger_num_to_4", false)) {
                        initHooks(GestureOperationHelper)
                    }
                }
            }
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.ix("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed init hook: ${it.javaClass.simpleName}")
        }
    }
}
