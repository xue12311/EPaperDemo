package com.ayxls.library_epager.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.blankj.utilcode.util.LogUtils

/**
 * wifi状态 广播接收者
 */
class WifiReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            when (intent.action) {
                //wifi信号强度变化
                WifiManager.RSSI_CHANGED_ACTION -> {
                    LogUtils.i("wifi信号强度变化")
                }
                //wifi连接上与否
                WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                    LogUtils.i("wifi网络状态变化")
                    val mNetWorkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                    when (mNetWorkInfo?.state) {
                        NetworkInfo.State.CONNECTING -> {
                            LogUtils.i("wifi正在连接")
                        }

                        NetworkInfo.State.CONNECTED -> {
                            LogUtils.i("wifi连接上了")
                        }

                        NetworkInfo.State.DISCONNECTED -> {
                            LogUtils.i("wifi断开了")
                        }

                        NetworkInfo.State.DISCONNECTING->{
                            LogUtils.i("wifi正在断开")
                        }
                        NetworkInfo.State.SUSPENDED->{
                            LogUtils.i("wifi暂停")
                        }
                        NetworkInfo.State.UNKNOWN->{
                            LogUtils.i("wifi未知状态")
                        }
                        else -> {
                            LogUtils.i("wifi状态 其他")
                        }
                    }
                }
                //wifi打开与否
                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    val wifi_state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED)
                    if(wifi_state == WifiManager.WIFI_STATE_DISABLED) {
                        LogUtils.i("关闭wifi")
                    }else if(wifi_state == WifiManager.WIFI_STATE_ENABLED) {
                        LogUtils.i("打开wifi")
                    }
                }
            }
        }
    }
}