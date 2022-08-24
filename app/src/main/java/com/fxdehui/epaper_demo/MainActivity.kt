package com.fxdehui.epaper_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayxls.library_epager.utils.ARouterNavigationUtils

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
                R.id.sbut_mqtt_deploy->{

                }
            }
        }
    }
}