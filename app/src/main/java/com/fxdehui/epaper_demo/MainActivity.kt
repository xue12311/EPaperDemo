package com.fxdehui.epaper_demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ayxls.library_epager.utils.ARouterNavigationUtils
import com.blankj.utilcode.util.ClickUtils
import com.fxdehui.epaper_demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        ClickUtils.applySingleDebouncing(arrayOf(binding.sbutEsptouch2, binding.sbutMqttDeploy), { view ->
            when (view.id) {
                //配网
                R.id.sbut_esptouch2 -> {
                    ARouterNavigationUtils.onEspTouch2Activity()
                }
                //MQTT
                R.id.sbut_mqtt_deploy -> {
                    ARouterNavigationUtils.onMqttConfigureActivity()
                }
            }
        })
    }
}