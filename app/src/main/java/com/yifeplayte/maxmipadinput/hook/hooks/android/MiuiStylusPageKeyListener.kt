package com.yifeplayte.maxmipadinput.hook.hooks.android

import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import com.github.kyuubiran.ezxhelper.utils.*
import com.yifeplayte.maxmipadinput.hook.hooks.BaseHook

object MiuiStylusPageKeyListener : BaseHook() {
    override fun init() {
        val pageKeyListenerClassName =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                "com.miui.server.input.stylus.MiuiStylusPageKeyListener"
            else "com.miui.server.stylus.MiuiStylusPageKeyListener"

        var primaryKey = false
        var secondaryKey = false

        findMethod(pageKeyListenerClassName) {
            name == "isPageKeyEnable"
        }.hookReturnConstant(false)

        findMethod(pageKeyListenerClassName) {
            name == "needInterceptBeforeDispatching"
        }.hookReturnConstant(false)

        findMethod(pageKeyListenerClassName) {
            name == "interceptScreenShotKey"
        }.hookReplace {
            for (arg in it.args) {
                val event : KeyEvent = arg as? KeyEvent ?: continue
                secondaryKey = event.action == KeyEvent.ACTION_DOWN
            }
            Log.i("penKey", secondaryKey.toString())
            return@hookReplace true
        }

        findMethod(pageKeyListenerClassName) {
            name == "interceptQuickNoteKey"
        }.hookReplace {
            for (arg in it.args) {
                val event : KeyEvent = arg as? KeyEvent ?: continue
                primaryKey = event.action == KeyEvent.ACTION_DOWN
            }
            Log.i("penKey", primaryKey.toString())
            Log.i("TouchCatch", "Working")
            return@hookReplace true
        }

        findMethod("android.view.InputEventReceiver") {
            name == "dispatchInputEvent"
        }.hookBefore {
            for ((i, arg) in it.args.withIndex()) {
                val event : MotionEvent = arg as? MotionEvent ?: continue
                if (MotionEvent.TOOL_TYPE_STYLUS != event.getToolType(0)) {
                    break
                }
                if (primaryKey || secondaryKey) {
                    val pointerProperties = MotionEvent.PointerProperties()
                    event.getPointerProperties(0, pointerProperties)
                    val pointerCoords = MotionEvent.PointerCoords()
                    event.getPointerCoords(0, pointerCoords)

                    var buttonState = event.buttonState
                    if (primaryKey) {
                        buttonState = buttonState or MotionEvent.BUTTON_STYLUS_PRIMARY
                    }
                    if (secondaryKey) {
                        buttonState = buttonState or MotionEvent.BUTTON_STYLUS_SECONDARY
                    }

                    val newEvent = event.copy(buttonState = buttonState)
                    it.args[i] = newEvent
                }
                break
            }
        }
    }
}

fun MotionEvent.copy(
    downTime: Long = getDownTime(),
    eventTime: Long = getEventTime(),
    action: Int = getAction(),
    pointerCount: Int = getPointerCount(),
    pointerProperties: Array<MotionEvent.PointerProperties>? =
        (0 until getPointerCount())
            .map { index ->
                MotionEvent.PointerProperties().also { pointerProperties ->
                    getPointerProperties(index, pointerProperties)
                }
            }
            .toTypedArray(),
    pointerCoords: Array<MotionEvent.PointerCoords>? =
        (0 until getPointerCount())
            .map { index ->
                MotionEvent.PointerCoords().also { pointerCoords ->
                    getPointerCoords(index, pointerCoords)
                }
            }
            .toTypedArray(),
    metaState: Int = getMetaState(),
    buttonState: Int = getButtonState(),
    xPrecision: Float = getXPrecision(),
    yPrecision: Float = getYPrecision(),
    deviceId: Int = getDeviceId(),
    edgeFlags: Int = getEdgeFlags(),
    source: Int = getSource(),
    flags: Int = getFlags()
): MotionEvent =
    MotionEvent.obtain(
        downTime,
        eventTime,
        action,
        pointerCount,
        pointerProperties,
        pointerCoords,
        metaState,
        buttonState,
        xPrecision,
        yPrecision,
        deviceId,
        edgeFlags,
        source,
        flags
    )

