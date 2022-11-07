package com.ayxls.library_epager.constant

import rxhttp.wrapper.annotation.DefaultDomain

object HttpApiConstants {
    @JvmField
    @DefaultDomain //设置为默认域名
    var baseUrl = "https://www.wanandroid.com/"

    /**
     * 设置 设备配置
     */
    const val SETTINGS_USER_CONFIGURE_URL = "api/settings_user_configure"

    /**
     * 清除 设备配置
     */
    const val CLEAR_DEVICE_CONFIGURE_URL= "api/clear_device_configure"
}