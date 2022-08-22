package com.ayxls.library_epager.viewmodel

import android.Manifest
import com.ayxls.library_epager.R
import com.ayxls.library_epager.bean.WiFiStateResultBean
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.espressif.iot.esptouch.EsptouchTask
import com.espressif.iot.esptouch.IEsptouchResult
import com.xue.base_common_library.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * esp 配网
 */
class EspTouchViewModel : BaseViewModel() {

    private val wifi_result by lazy { WiFiStateResultBean() }


    /**
     * wifi信息 发送方式 是否为 广播，
     * true: 广播 ，false: 组播
     */
    val isBroadcastConnect: Boolean = false

    /**
     * wifi 定位权限
     */
    fun getAccessFineLocationPermission() = Manifest.permission.ACCESS_FINE_LOCATION


    fun getWiFiResult() = wifi_result

    /**
     * 开始配网
     */
    fun onStartEsptouchTask(
        mEsptouchTask: EsptouchTask?,
        onSuccess: ((IEsptouchResult) -> Unit)? = null,
        onFailed: (() -> Unit)? = null,
    ) {
        launch({
            val result = executeForResults(mEsptouchTask)
            if (result == null || ObjectUtils.isEmpty(result)) {
                setToastMessage(StringUtils.getString(R.string.esptouch1_configure_result_failed_port))
                onFailed?.invoke()
            } else {
                val firstResult = result.getOrNull(0)
                if (firstResult == null) {
                    setToastMessage(StringUtils.getString(R.string.esptouch1_configure_result_failed))
                    onFailed?.invoke()
                } else
                    if (firstResult.isCancelled ?: true) {
                        setToastMessage(StringUtils.getString(R.string.esptouch1_configure_result_cancelled))
                        onFailed?.invoke()
                        //配网失败
                    } else if (!firstResult.isSuc()) {
                        setToastMessage(StringUtils.getString(R.string.esptouch1_configure_result_failed))
                        onFailed?.invoke()
                    } else {//配网成功
                        val bssid = firstResult.getBssid()
                        val ip_address = firstResult.getInetAddress()?.getHostAddress()
                        setToastMessage(
                            StringUtils.getString(
                                R.string.esptouch1_configure_result_success_item,
                                bssid,
                                ip_address
                            )
                        )
                        onSuccess?.invoke(firstResult)
                    }
            }
        }, {
            LogUtils.e("配网失败 $it")
            onFailed?.invoke()
        },{
            showLoadingDialog("配网中...")
        },{
            hideLoadingDialog()
        })
    }


    /**
     * 开始wifi配网
     */
    suspend private fun executeForResults(mEsptouchTask: EsptouchTask?): List<IEsptouchResult>? =
        withContext(Dispatchers.IO) {
            //连接设备数量
            val device_count = if (wifi_result.device_count <= 0) 1 else wifi_result.device_count
            mEsptouchTask?.executeForResults(device_count)
        }
}