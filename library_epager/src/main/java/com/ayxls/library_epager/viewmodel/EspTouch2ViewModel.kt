package com.ayxls.library_epager.viewmodel

import android.Manifest
import com.ayxls.library_epager.bean.WiFiStateResultBean
import com.xue.base_common_library.base.viewmodel.BaseViewModel

/**
 * esp 配网
 */
class EspTouch2ViewModel : BaseViewModel() {

    private val wifi_result by lazy { WiFiStateResultBean() }


    /**
     * wifi 定位权限
     */
    fun getAccessFineLocationPermission() = Manifest.permission.ACCESS_FINE_LOCATION


    fun getWiFiResult() = wifi_result


}