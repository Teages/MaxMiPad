package com.yifeplayte.maxmipadinput.activity

import android.annotation.SuppressLint
import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import com.yifeplayte.maxmipadinput.R
import com.yifeplayte.maxmipadinput.activity.pages.DisableFixedOrientationPage
import com.yifeplayte.maxmipadinput.activity.pages.MainPage
import kotlin.system.exitProcess

class MainActivity : MIUIActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        checkLSPosed()
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this) {
                setTitle(R.string.warning)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    init {
        activity = this
        registerPage(MainPage::class.java)
        registerPage(DisableFixedOrientationPage::class.java)
    }
}