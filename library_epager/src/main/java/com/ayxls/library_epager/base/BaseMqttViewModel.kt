package com.ayxls.library_epager.base

import com.ayxls.library_epager.constant.MqttConstant
import com.ayxls.library_epager.utils.mqtt.MQTTManager
import com.ayxls.library_epager.utils.mqtt.MqttConnectionStatusBean
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.xue.base_common_library.base.viewmodel.BaseViewModel
import com.xue.base_common_library.utils.DeviceIdUtils
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.flow.MutableStateFlow
import org.eclipse.paho.client.mqttv3.*

open class BaseMqttViewModel : BaseViewModel() {

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

    /**
     * MQTT 连接状态
     */
    private val _mqtt_connection_status = MutableStateFlow(MqttConnectionStatusBean())

    /**
     * MQTT 连接状态
     */
    fun getMqttConnectionStatus() = _mqtt_connection_status

    /**
     * 设置 MQTT 连接状态
     * @param status 状态
     */
    private fun setMqttConnectionStatus(status: MqttConstant.MqttStatusType) {
        _mqtt_connection_status.value = MqttConnectionStatusBean(status)
    }

    /**
     * 连接MQTT
     */
    protected fun onConnect(mqttHost: String, mqttUserName: String, mqttPassword: String) {
        setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_CONNECTING)
        initMqttConnect(mqttHost, mqttUserName, mqttPassword, mqttCallback, null)
    }

    /**
     * 断开 MQTT连接
     *
     */
    protected fun onDisConnect() {
        disConnect()
    }


    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     */
    protected fun onSubscribeTopic(topic: String) {
        subscribeTopic(topic, mqttSubscribeListener)
    }

    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    protected fun onUnSubscribeTopic(topic: String) {
        unSubscribeTopic(topic)
    }


    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    protected fun onUnSubscribeTopic(topic: Array<String>) {
        if (!isConnected()) {
            setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_NOT_CONNECTED)
            return
        }
        setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_TOPIC_UNSUBSCRIBING)
        val isSuccess = unsubscribeTopic(topic)


    }

    /**
     * MQTT 订阅监听
     */
    private val mqttSubscribeListener = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            LogUtils.e("订阅成功")
            setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_TOPIC_SUBSCRIBE_SUCCEEDED)
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            LogUtils.e("订阅失败 ${exception?.message}")
            setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_TOPIC_SUBSCRIBE_FAILED)
        }
    }


    /**
     * MQTT 消息监听
     */
    private val mqttCallback = object : MqttCallbackExtended {
        /**
         * 在连接已经连上且丢失后，会调用这个方法
         * @param cause
         */
        override fun connectionLost(cause: Throwable?) {
            LogUtils.e("MQTT 断开连接 ${cause?.message}")
            setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_NOT_CONNECTED)
        }

        /**
         * 订阅消息
         * @param topic 订阅主题
         * @param message 消息内容
         */
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            if (topic == null || StringUtils.isTrimEmpty(topic) || message == null) {
                return
            }
            val str_message = String(message.payload)
            LogUtils.e("订阅 主题 Topic : $topic")
            LogUtils.e("接受内容 message : $str_message")

            _mqtt_connection_status.value = MqttConnectionStatusBean(
                MqttConstant.MqttStatusType.MQTT_RECEIVE_SUBSCRIBE_MESSAGE,
                MqttConnectionStatusBean.MqttMessageBean(topic, str_message)
            )

        }

        /**
         * 在完成消息传递并且已收到所有确认时调用此方法
         * @param token
         */
        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            LogUtils.e("MQTT 消息发送完成")
            setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_MESSAGE_SEND_SUCCEEDED)
        }

        /**
         * 在丢失重连成功后会触发该方法
         * @param reconnect
         * @param serverURI
         */
        override fun connectComplete(reconnect: Boolean, serverURI: String?) {
            LogUtils.e("MQTT 连接成功")
            setMqttConnectionStatus(MqttConstant.MqttStatusType.MQTT_CONNECTED)
        }
    }

//-------------------------------------------------------------------------------------------------

    /**
     * 初始化  MQTT连接参数
     */
    private fun initMqttConnect(
        mqtt_service_host: String, mqtt_service_username: String, mqtt_service_password: String,
        mCallback: MqttCallback?, iMqttActionListener: IMqttActionListener?
    ): Boolean {
        if (isConnected()) {
            LogUtils.e("MQTT已经连接，不需要重新连接")
            return true
        }

        try {
            //创建MQTT客户端
            mMqttClient = MqttAndroidClient(Utils.getApp(), mqtt_service_host, mqtt_service_client_id)
            if (mCallback != null) {
                //设置MQTT 回调监听 并且 接受消息
                mMqttClient?.setCallback(mCallback)
            }

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
            mMqttClient?.connect(mMqttConnectOptions!!, null, iMqttActionListener)
            return true
        } catch (e: MqttException) {
            LogUtils.e("MQTT连接失败", e)
            return false
        }
    }


    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     */
    private fun subscribeTopic(topic: String) = subscribeTopic(topic, 0, null)


    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     */
    private fun subscribeTopic(topic: String, listener: IMqttActionListener?) =
        subscribeTopic(topic, 0, listener)

    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     * @param qos 订阅消息的服务质量
     */
    private fun subscribeTopic(topic: String, qos: Int, listener: IMqttActionListener?): Boolean {
        if (!isConnected()) {
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
                mMqttClient?.subscribe(topic, qos, null, listener)
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
    private fun unSubscribeTopic(topic: String): Boolean {
        if (!isConnected()) {
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
    private fun unsubscribeTopic(topic: Array<String>): Boolean {

        if (!isConnected()) {
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
    private fun publishMessage(topic: String, payload: String, qos: Int, retained: Boolean): Boolean {
        return publishMessage(topic, payload, qos, retained, null)
    }

    /**
     * 发送信息
     * @param topic 发送消息的主题
     * @param payload 消息内容
     * @param qos   提供消息的服务质量
     * @param retained  是否在服务器保留断开连接后的最后一条消息
     */
    private fun publishMessage(
        topic: String,
        payload: String,
        qos: Int,
        retained: Boolean,
        iMqttActionListener: IMqttActionListener?
    ): Boolean {
        if (!isConnected()) {
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
    private fun disConnect(): Boolean {
        if (isConnected()) {
            try {
                mMqttClient?.disconnect()
                return true
            } catch (e: MqttException) {
                LogUtils.e("MQTT断开连接失败", e)
                return false
            }
        }
        return true
    }

    /**
     * Mqtt 已连接
     */
    private fun isConnected(): Boolean {
        return mMqttClient != null && mMqttClient!!.isConnected
    }

    /**
     * Mqtt 关闭
     */
    private fun close() {
        if (mMqttClient != null) {
            mMqttClient?.unregisterResources()
            mMqttClient?.close()
            mMqttClient = null
        }
    }
}