package com.ayxls.library_epager.viewmodel

import com.ayxls.library_epager.base.BaseMqttViewModel
import com.ayxls.library_epager.constant.MqttConstant
import com.ayxls.library_epager.repository.MqttConfigureRepository
import com.xue.base_common_library.base.viewmodel.BaseViewModel

class MqttConfigureViewModel : BaseMqttViewModel() {
    private val repository by lazy { MqttConfigureRepository() }

    /**
     * 连接mqtt
     */
    fun onConnectMQTT() {
        onConnect(MqttConstant.MQTT_HOST, MqttConstant.MQTT_USERNAME, MqttConstant.MQTT_PASSWORD)
    }

    /**
     * 断开 MQTT连接
     *
     */
    fun onDisConnectMQTT() {
        onDisConnect()
    }

    /**
     * 订阅 MQTT主题
     * @param topic 订阅消息的主题
     */
    fun onSubscribeTopicMQTT(topic: String) {
        onSubscribeTopic(topic)
    }


    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    fun onUnSubscribeTopicMQTT(topic: String) {
        onUnSubscribeTopic(topic)
    }
}