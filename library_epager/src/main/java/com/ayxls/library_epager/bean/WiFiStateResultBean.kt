package com.ayxls.library_epager.bean

import android.net.InetAddresses

/**
 * wifi 状态
 */
data class WiFiStateResultBean(
    var message: CharSequence? = null,
    var is_wifi_enable: Boolean = true,
    var permission_granted: Boolean = false,
    /**
     * wifi 是否已连接
     */
    var wifi_connected: Boolean = false,
    var is5G: Boolean = false,
    var wifi_ip_address: InetAddresses? = null,
    var wifi_ssid: String? = null,
    var wifi_bssid: String? = null,
    var wifi_rssi: Int = 0,
    var wifi_ssid_bytes: Array<Byte>? = null,
)