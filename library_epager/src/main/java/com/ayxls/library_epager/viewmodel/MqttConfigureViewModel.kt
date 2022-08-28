package com.ayxls.library_epager.viewmodel

import com.ayxls.library_epager.repository.MqttConfigureRepository
import com.ayxls.library_epager.utils.mqtt.MQTTManager
import com.xue.base_common_library.base.viewmodel.BaseViewModel

class MqttConfigureViewModel : BaseViewModel() {
    private val repository by lazy { MqttConfigureRepository() }

    //mqtt 服务端
    private val mqttHost = "ws://u3b8f729.cn-shenzhen.emqx.cloud:13191"

    // mqtt 登录 用户名称
    private val mqttUserName = "zhangjiaxue"

    // mqtt 登录 用户密码
    private val mqttPassword = "zhangjiaxue"


    private val mMQTTManager by lazy { MQTTManager.getInstance() }

    /**
     * 连接 MQTT 服务器
     */
    fun onMqttConnect() {
        mMQTTManager.createConnect(mqttHost, mqttUserName, mqttPassword)
    }

    /**
     * 断开 MQTT 连接
     */
    fun onMqttDisconnect() {
        mMQTTManager.disConnect()
    }

    /**
     * 订阅 MQTT 主题
     * @param topic mqtt主题
     */
    fun onMqttSubscribe(topic: String) {
        mMQTTManager.subscribeTopic(topic)
    }

    /**
     * 取消订阅 MQTT 主题
     * @param topic mqtt主题
     */
    fun onMqttUnSubscribe(topic: String) {
        mMQTTManager.unSubscribeTopic(topic)
    }
}