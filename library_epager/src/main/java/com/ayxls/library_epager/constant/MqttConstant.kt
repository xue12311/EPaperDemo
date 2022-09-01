package com.ayxls.library_epager.constant

/**
 * MQTT 常量类
 */
object MqttConstant {
    const val MQTT_HOST = "ws://u3b8f729.cn-shenzhen.emqx.cloud:13191"
    const val MQTT_USERNAME = "zhangjiaxue"
    const val MQTT_PASSWORD = "zhangjiaxue"


    enum class MqttStatusType(val status: Int) {
        /**
         *  默认状态
         * MQTT 未连接
         */
        MQTT_NOT_CONNECTED(1),

        /**
         * MQTT 连接中
         */
        MQTT_CONNECTING(2),

        /**
         * MQTT 已连接
         */
        MQTT_CONNECTED(3),

        /**
         * MQTT 断开连接中
         */
        MQTT_DISCONNECTING(5),

        /**
         * MQTT 断开连接 失败
         */
        MQTT_DISCONNECTED_FAILED(7),


        /**
         * MQTT 订阅中
         */
        MQTT_TOPIC_SUBSCRIBING(8),

        /**
         * MQTT 订阅成功
         */
        MQTT_TOPIC_SUBSCRIBE_SUCCEEDED(9),

        /**
         * MQTT 订阅失败
         */
        MQTT_TOPIC_SUBSCRIBE_FAILED(10),


        /**
         * MQTT 取消订阅中
         */
        MQTT_TOPIC_UNSUBSCRIBING(11),

        /**
         * MQTT 取消订阅成功
         */
        MQTT_TOPIC_UNSUBSCRIBE_SUCCEEDED(12),

        /**
         * MQTT 取消订阅失败
         */
        MQTT_TOPIC_UNSUBSCRIBE_FAILED(13),

        /**
         * MQTT 消息发送中
         */
        MQTT_MESSAGE_SENDING(14),

        /**
         * MQTT 消息发送成功
         */
        MQTT_MESSAGE_SEND_SUCCEEDED(15),

        /**
         * MQTT 消息发送失败
         */
        MQTT_MESSAGE_SEND_FAILED(16),

        /**
         * MQTT 消息接收
         */
        MQTT_RECEIVE_SUBSCRIBE_MESSAGE(17)
    }

}