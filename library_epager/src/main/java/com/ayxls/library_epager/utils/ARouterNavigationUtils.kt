package com.ayxls.library_epager.utils

import com.alibaba.android.arouter.launcher.ARouter
import com.ayxls.library_epager.constant.ARouterConstants

/**
 * ARouter 跳转 工具类
 */
object ARouterNavigationUtils {
    /**
     * 配置 wifi
     */
    fun onEspTouch2Activity() {
        startActivity(ARouterConstants.ARouterActivityEspTouch2)
    }
    /**
     * wifi 列表
     */
    fun onWiFiScanListActivity() {
        startActivity(ARouterConstants.ARouterActivityWiFiScanList)
    }


    /**
     * mqtt 测试
     */
    fun onMqttConfigureActivity() {
        startActivity(ARouterConstants.ARouterActivityMqttConfigure)
    }


    private fun startActivity(path: String) {
        ARouter.getInstance().build(path).navigation()
    }
}