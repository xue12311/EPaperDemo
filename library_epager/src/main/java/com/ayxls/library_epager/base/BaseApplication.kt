package com.ayxls.library_epager.base

import android.app.Application
import com.jeremyliao.liveeventbus.LiveEventBus

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    /**
     * 初始化 LiveEventBus
     */
    protected fun initLiveEventBus(isDebug: Boolean) {
        LiveEventBus.config()
            .lifecycleObserverAlwaysActive(true)
            .autoClear(true)
            .enableLogger(isDebug)
    }

}