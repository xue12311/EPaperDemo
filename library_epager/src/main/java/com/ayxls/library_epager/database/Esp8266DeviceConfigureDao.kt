package com.ayxls.library_epager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayxls.library_epager.database.entitiy.Esp8266DeviceConfigureEntity

/**
 * Esp8266 设备 配置信息
 */
@Dao
interface Esp8266DeviceConfigureDao {
    /**
     * 查询 所有 Esp8266 设备 配置信息 列表
     */
    @Query("SELECT * FROM Esp8266DeviceConfigureEntity")
    suspend fun getEsp8266DeviceList(): List<Esp8266DeviceConfigureEntity>

    @Query("SELECT * FROM Esp8266DeviceConfigureEntity WHERE wifi_local_ip = :local_ip LIMIT 1 OFFSET 1")
    suspend fun getEsp8266DeviceToLocalIp(local_ip: String): Esp8266DeviceConfigureEntity?

    @Query("SELECT * FROM Esp8266DeviceConfigureEntity WHERE wifi_local_ip in (:list_local_ip)")
    suspend fun getEsp8266DeviceToLocalIpList(list_local_ip: List<String>): List<Esp8266DeviceConfigureEntity>

    @Query("SELECT * FROM Esp8266DeviceConfigureEntity WHERE device_chip_id = :device_chip_id LIMIT 1 OFFSET 1")
    suspend fun getEsp8266DeviceToDeviceChipId(device_chip_id: String): Esp8266DeviceConfigureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEsp8266Device(entity: Esp8266DeviceConfigureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(users: List<Esp8266DeviceConfigureEntity>)

    @Delete
    suspend fun deleteEsp8266Device(vararg device: Esp8266DeviceConfigureEntity)

    /**
     * 删除 指定 Esp8266 设备
     */
    suspend fun deleteEsp8266Device(device_chip_id: String) {
        val bean = getEsp8266DeviceToDeviceChipId(device_chip_id)
        if (bean != null) {
            deleteEsp8266Device(bean)
        }
    }
}