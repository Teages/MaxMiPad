package com.yifeplayte.maxmipadinput.activity.pages

import android.view.View
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.yifeplayte.maxmipadinput.R
import com.yifeplayte.maxmipadinput.utils.Terminal

@BMMainPage(titleId = R.string.app_name)
class MainPage : BasePage() {
    override fun onCreate() {
        TitleText(textId = R.string.input)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.no_magic_pointer,
                tipsId = R.string.no_magic_pointer_tips
            ),
            SwitchV("no_magic_pointer", true)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.restore_esc,
                tipsId = R.string.restore_esc_tips
            ),
            SwitchV("restore_esc", true)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_stylus_bluetooth_restriction,
                tipsId = R.string.remove_stylus_bluetooth_restriction_tips
            ),
            SwitchV("remove_stylus_bluetooth_restriction", true)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.ignore_stylus_key_gesture,
                tipsId = R.string.ignore_stylus_key_gesture_tips
            ),
            SwitchV("ignore_stylus_key_gesture", false)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.set_gesture_need_finger_num_to_4,
                tipsId = R.string.set_gesture_need_finger_num_to_4_tips
            ),
            SwitchV("set_gesture_need_finger_num_to_4", false)
        )
        Line()
        TitleText(textId = R.string.screen)
        val bindingDisableFixedOrientation =
            GetDataBinding({
                MIUIActivity.safeSP.getBoolean("disable_fixed_orientation", true)
            }) { view, flags, data ->
                when (flags) {
                    1 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.disable_fixed_orientation,
                tipsId = R.string.disable_fixed_orientation_tips
            ),
            SwitchV(
                key = "disable_fixed_orientation",
                defValue = true,
                dataBindingSend = bindingDisableFixedOrientation.bindingSend
            )
        )
        TextSummaryArrow(
            TextSummaryV(
                textId = R.string.disable_fixed_orientation_scope,
                tipsId = R.string.disable_fixed_orientation_scope_tips
            ) {
                showFragment("DisableFixedOrientationPage")
            },
            dataBindingRecv = bindingDisableFixedOrientation.getRecv(1)
        )
        Line()
        TitleText(textId = R.string.reboot)
        TextSummaryArrow(
            TextSummaryV(
                textId = R.string.reboot_system
            ) {
                MIUIDialog(activity) {
                    setTitle(R.string.warning)
                    setMessage(R.string.reboot_tips)
                    setLButton(R.string.cancel) {
                        dismiss()
                    }
                    setRButton(R.string.done) {
                        Terminal.exec("/system/bin/sync;/system/bin/svc power reboot || reboot")
                    }
                }.show()
            }
        )
    }
}