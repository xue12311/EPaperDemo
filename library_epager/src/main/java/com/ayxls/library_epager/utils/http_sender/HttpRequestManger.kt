package com.ayxls.library_epager.utils.http_sender

import com.ayxls.library_epager.bean.Esp8266ConfigureRequestBean
import com.ayxls.library_epager.constant.HttpApiConstants
import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp

/**
 * 网络请求管理类
 */
object HttpRequestManger {
    /**
     * 获取 esp8266 配置信息
     */
    suspend fun onSettingsUserConfigure(): Esp8266ConfigureRequestBean =
        RxHttp.patchForm(HttpApiConstants.OBTAIN_ESP8266_CONFIGURE_URL)
            .tag(HttpApiConstants.OBTAIN_ESP8266_CONFIGURE_URL)
            .toAwait<Esp8266ConfigureRequestBean>()
            .await()
}
