package com.ayxls.library_epager.bean

/**
 * Esp8266 相关信息
 */
data class Esp8266ConfigureRequestBean(
    /**
     * esp8266 wifi 地址
     */
    var wifi_local_ip: String? = null,

    /**
     * esp8266 mac 地址
     */
    var device_mac: String? = null,

    /**
     * esp8266 设备 唯一值
     */
    var device_chip_id: String? = null,
) : BaseRequestBean<Esp8266ConfigureRequestBean>()