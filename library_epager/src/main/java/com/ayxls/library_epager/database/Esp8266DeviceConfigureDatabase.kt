package com.ayxls.library_epager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayxls.library_epager.database.entitiy.Esp8266DeviceConfigureEntity

@Database(entities = [Esp8266DeviceConfigureEntity::class], version = 1, exportSchema = true)
abstract class Esp8266DeviceConfigureDatabase : RoomDatabase() {

    /**
     * Esp8266 设备 配置信息
     */
    abstract fun esp8266DeviceDao(): Esp8266DeviceConfigureDao
}