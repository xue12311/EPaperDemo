package com.ayxls.library_epager.constant

object HttpApiConstants {
//    @JvmField
//    @DefaultDomain //设置为默认域名
//    var baseUrl = "https://www.wanandroid.com/"

    /**
     * 获取 开发版信息
     */
    const val OBTAIN_ESP8266_CONFIGURE_URL = "/api/obtain_esp8266_configure"
    /**
     * 设置 设备 wifi配置信息 用户配置信息
     */
    const val SETTINGS_DEVICE_CONFIGURE_URL= "/api/setting_device_configure"

    /**
     * 清除 设备 wifi配置 用户配置信息
     */
    const val CLEAR_DEVICE_CONFIGURE_URL= "/api/clear_device_configure"
}