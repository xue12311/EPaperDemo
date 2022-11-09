package com.ayxls.library_epager.bean

import com.xue.base_common_library.network.BaseNetworkResponse

open class BaseRequestBean<T>(
    val code: Int = -1,
    val data: T? = null,
    val msg: String? = null,
) : BaseNetworkResponse<BaseRequestBean<T>>() {
    override fun isSucces(): Boolean = code == 200
    override fun getResponseData(): BaseRequestBean<T> = this
    override fun getResponseCode(): Int = code
    override fun getResponseMsg(): String? = msg

}