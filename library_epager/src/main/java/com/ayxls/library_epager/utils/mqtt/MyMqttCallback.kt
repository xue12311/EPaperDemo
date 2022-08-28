package com.ayxls.library_epager.utils.mqtt

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

open class MyMqttCallback : MqttCallbackExtended {
    /**
     * 在连接已经连上且丢失后走这里
     *
     * @param cause
     */
    override fun connectionLost(cause: Throwable?) {
        LogUtils.e("connection lost")
    }

    /**
     * 当消息从服务器到达时调用此方法
     *
     * @param topic 消息主题的名称
     * @param message 实际信息内容
     */
    override fun messageArrived(topic: String?, message: MqttMessage?) {
        if (topic == null || StringUtils.isTrimEmpty(topic) || message == null) {
            return
        }
        val str_message = String(message.payload)
        LogUtils.e("订阅 主题 Topic : $topic")
        LogUtils.e("接受内容 message : $str_message")
    }

    /**
     * 在完成消息传递并且已收到所有确认时调用此方法
     *
     * @param token
     */
    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        LogUtils.e("msg delivered")
    }

    /**
     * 在丢失重连成功后会触发该方法
     *
     * @param reconnect
     * @param serverURI
     */
    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
        LogUtils.e("connect complete")
    }

}