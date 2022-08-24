package com.ayxls.library_epager.utils.mqtt

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

class MyMqttCallback:MqttCallbackExtended{
    /**
     * 在连接已经连上且丢失后走这里
     *
     * @param cause
     */
    override fun connectionLost(cause: Throwable?) {
    }

    /**
     * 当消息从服务器到达时调用此方法
     *
     * @param topic 消息主题的名称
     * @param message 实际信息内容
     */
    override fun messageArrived(topic: String?, message: MqttMessage?) {
    }

    /**
     * 在完成消息传递并且已收到所有确认时调用此方法
     *
     * @param token
     */
    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }

    /**
     * 在丢失重连成功后会触发该方法
     *
     * @param reconnect
     * @param serverURI
     */
    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
    }
}