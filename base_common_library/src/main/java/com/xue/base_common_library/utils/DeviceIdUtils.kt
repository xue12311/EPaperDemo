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
        val mDeviceIdFile = getDeviceIdFile()
        if (mDeviceIdFile.exists()) {
            //文件读取
            val mFileContent = FileIOUtils.readFile2String(mDeviceIdFile)
            if (mFileContent.isNotEmpty()) {
                return mFileContent
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
        val mDeviceIdFile = getDeviceIdFile()
        //判断文件是否存在，不存在则判断是否创建成功
        if (FileUtils.createOrExistsFile(mDeviceIdFile)) {
            //文件读取
            val mFileContent = FileIOUtils.readFile2String(mDeviceIdFile)
            if (mFileContent.isNullOrEmpty()) {
                val mDeviceUniqueId = UUID.randomUUID().toString()
                //文件写入
                FileIOUtils.writeFileFromString(mDeviceIdFile, mDeviceUniqueId)
                return mDeviceUniqueId
            } else {
                return mFileContent
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