package com.ayxls.library_epager.database

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

object RoomManager {
    private var db: Esp8266DeviceConfigureDatabase? = null

    /**
     * 初始化 数据库
     */
    fun initRoomManager(application: Application) {
        getRoomDatabase(application)
    }

    fun getRoomDatabase(application: Application): Esp8266DeviceConfigureDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                application,
                Esp8266DeviceConfigureDatabase::class.java,
                "epager.db"
            )
                .build()
        }
        return db!!
    }

}