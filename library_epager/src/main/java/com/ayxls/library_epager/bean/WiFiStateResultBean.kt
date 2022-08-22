package com.ayxls.library_epager.bean

import java.net.InetAddress

/**
 * wifi 状态
 */
data class WiFiStateResultBean(
    var message: String? = null,

    /**
     * 是否已经授权
     */
    var permission_granted: Boolean = false,

    /**
     * wifi 是否已连接
     */
    var wifi_connected: Boolean = false,

    /**
     * 是否为 5G wifi
     */
    var is5G: Boolean = false,

    /**
     * wifi ip 地址
     */
    var wifi_ip_address: InetAddress? = null,

    /**
     * BSSID地址
     */
    var wifi_bssid: String? = null,

    /**
     * wifi名称
     */
    var wifi_ssid: String? = null,

    /**
     * wifi 密码
     */
    var wifi_password: String? = null,

    /**
     * 所需连接设备数量
     */
    var device_count: Int = 0,
) {

    /**
     * 清除 WiFi 数据
     */
    fun onClearWiFiData() {
        wifi_connected = false
        is5G = false
        wifi_ip_address = null
        wifi_bssid = null
        wifi_ssid = null
        wifi_password = null
        device_count = 0
    }
}