package com.ayxls.library_epager.utils.mqtt

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

class MyMqttCallback : MqttCallbackExtended {
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
        if (topic == null || StringUtils.isTrimEmpty(topic) || message == null) {
            return
        }
        val str_message = String(message.payload)
        LogUtils.e("订阅 主题 Topic : $topic")
        LogUtils.e("接受内容 message : $str_message")
//        response(topic,1,"消息已收到")
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

    /**
     *  响应
     *  收到服务端户端的消息后，响应给对方告知消息已到达或者消息有问题等
     * @param topic 主题
     * @param qos 服务质量
     * @param message 消息内容
     */
//    private fun onMqttResponse(topic: String, qos: Int, message: String) {
//        //是否保留
//        val isRetained = false
//        try {
//            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
//            mqttAndroidClient.publish(topic, message.toByte(), qos, isRetained)
//        } catch (e:MqttException) {
//            e.printStackTrace()
//        }
//    }
}