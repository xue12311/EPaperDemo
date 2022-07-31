package com.xue.base_common_library.network
/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 服务器返回数据的基类
 * 如果需要框架帮你做脱壳处理请继承它！！请注意：
 * 2.必须实现抽象方法，根据自己的业务判断返回请求结果是否成功
 */
abstract class BaseNetworkResponse<T> {
    abstract fun isSucces(): Boolean

    abstract fun getResponseData(): T?

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String?
}