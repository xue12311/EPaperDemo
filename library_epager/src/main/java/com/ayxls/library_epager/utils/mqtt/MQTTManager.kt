package com.ayxls.library_epager.utils.mqtt

import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import java.util.UUID

//https://blog.csdn.net/weixin_42324979/article/details/118030790

class MQTTManager {
    private val TAG = MQTTManager::class.java.name

    /**
     * 服务器地址（协议+地址+端口号）
     */
    private var mqtt_service_host: String = ""

    /**
     * 连接到MQTT服务器的 用户名
     */
    private var mqtt_service_username: String = ""

    /**
     * 连接到MQTT服务器的 密码
     */
    private var mqtt_service_password: String = ""

    /**
     * 连接到MQTT服务器的 客户端id
     */
    private var mqtt_service_client_id: String = ""

    /**
     * 订阅主题
     */
    private var mqtt_service_topic: Array<String>? = null

    /**
     * MQTT客户端
     */
    private var mMqttClient: MqttClient? = null

    /**
     * MQTT连接选项
     */
    private var mMqttConnectOptions: MqttConnectOptions? = null

    //消息回调
    private val mCallback: MqttCallback = MyMqttCallback()


    companion object {
        private var mInstance: MQTTManager? = null

        fun getInstance(): MQTTManager {
            if (mInstance == null) {
                mInstance = MQTTManager()
            }
            return mInstance as MQTTManager
        }
    }

    fun createConnect(host:String,username:String,password:String,topic:Array<String>){
        mqtt_service_host = host
        mqtt_service_username = username
        mqtt_service_password = password
        mqtt_service_topic = topic

    }
}