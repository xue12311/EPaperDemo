package com.xue.base_common_library.utils

import android.os.Environment
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import java.io.File
import java.util.*

object DeviceIdUtils {
    private const val DEVICE_UUID_FILE_NAME = "device_uuid_configure"

    /**
     * 获取设备唯一标识
     */
    fun getDeviceId(): String {
        val deviceIdFile = getDeviceIdFile()
        if (deviceIdFile.exists()) {
            //文件读取
            val fileContent = FileIOUtils.readFile2String(deviceIdFile)
            if (fileContent.isNotEmpty()) {
                return fileContent
            } else {
                return createDeviceId()
            }
        } else {
            return createDeviceId()
        }
    }

    /**
     * 创建设备唯一id
     */
    private fun createDeviceId(): String {
        val deviceIdFile = getDeviceIdFile()
        //判断文件是否存在，不存在则判断是否创建成功
        if (FileUtils.createOrExistsFile(deviceIdFile)) {
            //文件读取
            val fileContent = FileIOUtils.readFile2String(deviceIdFile)
            if (fileContent.isNullOrEmpty()) {
                val deviceUniqueId = UUID.randomUUID().toString()
                //文件写入
                FileIOUtils.writeFileFromString(deviceIdFile, deviceUniqueId)
                return deviceUniqueId
            } else {
                return fileContent
            }
        } else {
            LogUtils.e("设备 唯一值 创建失败")
            return UUID.randomUUID().toString()
        }
    }

    private fun getDeviceIdFile(): File {
        var dirPath =
            Utils.getApp().applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        if (dirPath == null) {
            dirPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath + "/" + AppUtils.getAppPackageName()
        }
        return File(dirPath, DEVICE_UUID_FILE_NAME)
    }
}