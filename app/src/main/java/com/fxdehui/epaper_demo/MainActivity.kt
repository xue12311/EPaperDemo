package com.fxdehui.epaper_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayxls.library_epager.utils.ARouterNavigationUtils
import com.xue.base_common_library.utils.DeviceIdUtils
import com.blankj.utilcode.util.ToastUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onViewClick(view: View?) {
        if (view != null) {
            when (view.id) {
                //配网
                R.id.sbut_esptouch2 -> {
                    ARouterNavigationUtils.onEspTouch2Activity()
                }
                //MQTT
                R.id.sbut_mqtt_deploy -> {
                    val device_id = DeviceIdUtils.getDeviceId()
                    ToastUtils.showShort("设备唯一标识：$device_id")
                }
            }
        }
    }
}