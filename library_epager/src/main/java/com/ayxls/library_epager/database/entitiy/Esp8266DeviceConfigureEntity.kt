package com.ayxls.library_epager.database.entitiy

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Esp8266 设备 配置信息
 */
@Entity(indices = [Index(value = ["device_chip_id"], unique = true),Index("wifi_local_ip")])
data class Esp8266DeviceConfigureEntity(
    /**
     * 主键 Id (自增)
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    /**
     * ip地址
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
)