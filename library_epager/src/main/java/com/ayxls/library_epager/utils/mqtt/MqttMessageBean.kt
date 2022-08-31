package com.ayxls.library_epager.utils.mqtt

import android.os.Parcelable
import com.ayxls.library_epager.constant.MqttConstant
import kotlinx.parcelize.Parcelize

/**
 * MQTT 连接状态
 */
@Parcelize
data class MqttConnectionStatusBean(
    /**
     * 状态 默认 未连接
     */
    var status: MqttConstant.MqttStatusType = MqttConstant.MqttStatusType.MQTT_NOT_CONNECTED,

    /**
     * mqtt 消息
     */
    var message: MqttMessageBean? = null,

    ) : Parcelable {

    /**
     * MQTT 状态
     */
    fun getMqttStatusText(): String =
        when (status) {
            //MQTT 未连接
            MqttConstant.MqttStatusType.MQTT_NOT_CONNECTED -> "未连接"
            //MQTT 连接中
            MqttConstant.MqttStatusType.MQTT_CONNECTING -> "连接中"
            //MQTT 已连接
            MqttConstant.MqttStatusType.MQTT_CONNECTED -> "已连接"

            //MQTT 断开连接中
            MqttConstant.MqttStatusType.MQTT_DISCONNECTING -> "断开连接中"
            //MQTT 断开连接 失败
            MqttConstant.MqttStatusType.MQTT_DISCONNECTED_FAILED -> "断开连接失败"

            //MQTT 订阅中
            MqttConstant.MqttStatusType.MQTT_TOPIC_SUBSCRIBING -> "订阅中"
            //MQTT 订阅成功
            MqttConstant.MqttStatusType.MQTT_TOPIC_SUBSCRIBE_SUCCEEDED -> "订阅成功"
            //MQTT 订阅失败
            MqttConstant.MqttStatusType.MQTT_TOPIC_SUBSCRIBE_FAILED -> "订阅失败"

            //MQTT 取消订阅中
            MqttConstant.MqttStatusType.MQTT_TOPIC_UNSUBSCRIBING -> "取消订阅中"
            //MQTT 取消订阅成功
            MqttConstant.MqttStatusType.MQTT_TOPIC_UNSUBSCRIBE_SUCCEEDED -> "取消订阅成功"
            //MQTT 取消订阅失败
            MqttConstant.MqttStatusType.MQTT_TOPIC_UNSUBSCRIBE_FAILED -> "取消订阅失败"

            //MQTT 消息发送中
            MqttConstant.MqttStatusType.MQTT_MESSAGE_SENDING -> "消息发送中"
            //MQTT 消息发送成功
            MqttConstant.MqttStatusType.MQTT_MESSAGE_SEND_SUCCEEDED -> "消息发送成功"
            //MQTT 消息发送失败
            MqttConstant.MqttStatusType.MQTT_MESSAGE_SEND_FAILED -> "消息发送失败"

            //MQTT 消息接收
            MqttConstant.MqttStatusType.MQTT_RECEIVE_SUBSCRIBE_MESSAGE -> "收到消息"
        }


    /**
     * MQTT 消息
     */
    @Parcelize
    data class MqttMessageBean(
        /**
         * mqtt 主题
         */
        var topic: String? = null,
        /**
         * mqtt 消息 内容
         */
        var message: String? = null,
    ) : Parcelable
}