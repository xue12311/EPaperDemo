package com.fxdehui.epaper_demo

import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.ayxls.library_epager.base.BaseApplication
import com.ayxls.library_epager.database.RoomManager
import com.ayxls.library_epager.utils.http_sender.RxHttpManager
import com.blankj.utilcode.util.*

class MyApplication : BaseApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        SPStaticUtils.setDefaultSPUtils(SPUtils.getInstance(AppUtils.getAppPackageName()))
        LogUtils.getConfig()
            //设置 log 总开关
            .setLogSwitch(AppConstant.isAppDebug)
            //设置 log 控制台开关
            .setConsoleSwitch(AppConstant.isAppDebug)
            //设置 log 全局 tag
            .setGlobalTag(AppUtils.getAppName())
        //网络请求
        RxHttpManager.init(this, AppConstant.isAppDebug)
        //数据库
        RoomManager.initRoomManager(this)
        if (AppConstant.isAppDebug) {
            // 打印日志
            ARouter.openLog()
            //开启 调试模式
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}