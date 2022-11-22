package com.ayxls.library_epager.utils.http_sender

import com.ayxls.library_epager.bean.Esp8266ConfigureRequestBean
import com.ayxls.library_epager.bean.SettingsUserConfigureRequestBean
import com.ayxls.library_epager.constant.HttpApiConstants
import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp

/**
 * 网络请求管理类
 */
object HttpRequestManger {

    fun setBaseURL(url: String) {
        HttpApiConstants.baseUrl = url
    }

    /**
     * 获取 esp8266 配置信息
     */
    suspend fun onObtainUserConfigure(): Esp8266ConfigureRequestBean =
        RxHttp.patchForm(HttpApiConstants.OBTAIN_ESP8266_CONFIGURE_URL)
            .tag(HttpApiConstants.OBTAIN_ESP8266_CONFIGURE_URL)
            .toAwait<Esp8266ConfigureRequestBean>()
            .await()

    /**
     * 设置 esp8266 配置信息（ wifi配置信息，用户配置信息 ）
     */
    suspend fun onSettingsUserConfigure(mUserConfigure: SettingsUserConfigureRequestBean): Esp8266ConfigureRequestBean =
        RxHttp.patchForm(HttpApiConstants.SETTINGS_DEVICE_CONFIGURE_URL)
            .tag(HttpApiConstants.SETTINGS_DEVICE_CONFIGURE_URL)
            .add("data", mUserConfigure)
            .toAwait<Esp8266ConfigureRequestBean>()
            .await()

    /**
     * 删除 esp8266 配置信息（ wifi配置信息，用户配置信息 ）
     * @param clear_type Int 0: 清除全部配置信息 1:清除 WIFI 配置信息  2: 清除 MQTT 配置信息
     * @return
     */
    suspend fun onClearUserConfigure(clear_type: Int): Esp8266ConfigureRequestBean =
        RxHttp.patchForm(HttpApiConstants.CLEAR_DEVICE_CONFIGURE_URL)
            .tag(HttpApiConstants.CLEAR_DEVICE_CONFIGURE_URL)
            .add("type", clear_type)
            .toAwait<Esp8266ConfigureRequestBean>()
            .await()
}
