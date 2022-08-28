package com.ayxls.library_epager.utils.mqtt

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.xue.base_common_library.utils.DeviceIdUtils
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

//https://blog.csdn.net/weixin_42324979/article/details/118030790

class MQTTManager {

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
     * 订阅 MQTT 主题名称
     */
    private var mqtt_topic_name: String = ""

    /**
     * 连接到MQTT服务器的 客户端id
     */
    private val mqtt_service_client_id: String by lazy { DeviceIdUtils.getDeviceId() }


    /**
     * MQTT客户端
     */
    private var mMqttClient: MqttAndroidClient? = null

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

    fun createConnect(host: String, username: String, password: String) {
        createConnect(host, username, password, null)
    }

    fun createConnect(host: String, username: String, password: String, topic: String?) {
        mqtt_service_host = host
        mqtt_service_username = username
        mqtt_service_password = password
        mqtt_topic_name = StringUtils.null2Length0(topic)
        initMqttConnect()
    }

    /**
     * 初始化  MQTT连接参数
     */
    private fun initMqttConnect() {
        try {
            //创建MQTT客户端
            mMqttClient = MqttAndroidClient(Utils.getApp(), mqtt_service_host, mqtt_service_client_id)
            //设置MQTT 回调监听 并且 接受消息
            mMqttClient?.setCallback(mCallback)

            //创建MQTT连接
            mMqttConnectOptions = MqttConnectOptions()
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            mMqttConnectOptions?.isCleanSession = true
            //设置超时时间 单位为 秒
            mMqttConnectOptions?.connectionTimeout = 10
            //设置会话心跳时间 单位为 秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            mMqttConnectOptions?.keepAliveInterval = 20
            //设置自动重连 如果连接丢失，客户端是否会自动尝试重新连接到服务器
            mMqttConnectOptions?.isAutomaticReconnect = true
            //用户名 密码 不为 null
            if (mqtt_service_username.isNotEmpty() && mqtt_service_password.isNotEmpty()) {
                //设置连接的用户名
                mMqttConnectOptions?.userName = mqtt_service_username
                //设置连接的密码
                mMqttConnectOptions?.password = mqtt_service_password.toCharArray()
            }
            //连接 MQTT服务器
            /**
             * options：用来携带连接服务器的一系列参数，例如用户名、密码等
             * userContext：可选对象，用于向回调传递上下文。一般传null即可
             * callback：用来监听MQTT是否连接成功的回调
             * */
            mMqttClient?.connect(mMqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    LogUtils.e("MQTT连接成功")
                    if (mqtt_topic_name.isNotEmpty()) {
                        //订阅主题
                        subscribeTopic(mqtt_topic_name)
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    LogUtils.e("MQTT连接失败")
                }
            })


        } catch (e: MqttException) {
            LogUtils.e("MQTT连接失败", e)
        }
    }

    private val iMqttActionListener = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            LogUtils.e("MQTT订阅成功")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            LogUtils.e("MQTT订阅失败 ${exception?.message}")
        }
    }

    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     */
    fun subscribeTopic(topic: String) {
        subscribeTopic(topic, 0)
    }

    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     * @param qos 订阅消息的服务质量
     */
    fun subscribeTopic(topic: String, qos: Int): Boolean {
        if (mMqttClient == null || !mMqttClient!!.isConnected) {
            LogUtils.e("MQTT未连接,无法订阅")
            return false
        }
        if (topic.isEmpty()) {
            LogUtils.e("订阅的topic为空")
            return false
        }
        try {
            //订阅主题
            if (topic.isNotEmpty()) {
                mMqttClient?.subscribe(topic, qos, null, iMqttActionListener)
                return true
            }
        } catch (e: MqttException) {
            LogUtils.e("订阅主题失败", e)
        }
        return false
    }

    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    fun unSubscribeTopic(topic: String): Boolean {
        if (mMqttClient == null || !mMqttClient!!.isConnected) {
            LogUtils.e("MQTT未连接,无法订阅")
            return false
        }
        if (topic.isEmpty()) {
            LogUtils.e("订阅的topic为空")
            return false
        }
        try {
            //取消订阅
            if (topic.isNotEmpty()) {
                mMqttClient?.unsubscribe(topic)
                return true
            }
        } catch (e: MqttException) {
            LogUtils.e("取消订阅失败", e)
        }
        return false
    }
    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    fun unsubscribeTopic(topic: Array<String>): Boolean {
        if (mMqttClient == null || !mMqttClient!!.isConnected) {
            LogUtils.e("MQTT未连接,无法订阅")
            return false
        }
        if (topic.isEmpty()) {
            LogUtils.e("订阅的topic为空")
            return false
        }
        try {
            //取消订阅
            mMqttClient?.unsubscribe(topic)
            return true
        } catch (e: MqttException) {
            LogUtils.e("取消订阅失败", e)
        }
        return false
    }

    /**
     * 发送信息
     * @param topic 发送消息的主题
     * @param payload 消息内容
     * @param qos   提供消息的服务质量
     * @param retained  是否在服务器保留断开连接后的最后一条消息
     */
    fun publishMessage(topic: String, payload: String, qos: Int, retained: Boolean): Boolean {
        if (mMqttClient == null || !mMqttClient!!.isConnected) {
            LogUtils.e("MQTT未连接,无法订阅")
            return false
        }
        if (topic.isEmpty()) {
            LogUtils.e("订阅的topic为空")
            return false
        }
        try {
            //订阅主题
            if (topic.isNotEmpty()) {
                mMqttClient?.publish(topic, payload.toByteArray(), qos, retained, null, iMqttActionListener)
                return true
            }
        } catch (e: MqttException) {
            LogUtils.e("订阅主题失败", e)
        }
        return false
    }

    /**
     * 断开 MQTT连接
     */
    fun disConnect() {
        if (mMqttClient != null && mMqttClient!!.isConnected) {
            try {
                mMqttClient?.disconnect()
            } catch (e: MqttException) {
                LogUtils.e("MQTT断开连接失败", e)
            }
        }
    }
}