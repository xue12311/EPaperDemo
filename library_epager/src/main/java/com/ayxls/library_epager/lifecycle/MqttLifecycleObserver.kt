package com.ayxls.library_epager.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ayxls.library_epager.bean.live_event_bus.MqttLiveEvent
import com.ayxls.library_epager.utils.mqtt.MQTTManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import org.eclipse.paho.client.mqttv3.*

/**
 * 包含 MQTT 的 ViewModel
 *
 */
open class MqttLifecycleObserver : DefaultLifecycleObserver {

    //mqtt 服务端
    private val mqttHost = "ws://u3b8f729.cn-shenzhen.emqx.cloud:13191"

    // mqtt 登录 用户名称
    private val mqttUserName = "zhangjiaxue"

    // mqtt 登录 用户密码
    private val mqttPassword = "zhangjiaxue"

    /**
     * MQTT 单例
     */
    private val mMQTTManager by lazy { MQTTManager.getInstance() }

//------------------------------------------------------------------------------------------------

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        onConnect(mqttHost, mqttUserName, mqttPassword)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onDisConnect()
    }

    /**
     * 连接MQTT
     */
    fun onConnect(mqttHost: String, mqttUserName: String, mqttPassword: String) {
        mMQTTManager.createConnect(mqttHost, mqttUserName, mqttPassword, mqttCallback)
    }

    /**
     * 断开 MQTT连接
     *
     */
    fun onDisConnect() {
        mMQTTManager.disConnect()
    }


    /**
     * 订阅主题
     * @param topic 订阅消息的主题
     */
    fun onSubscribeTopic(topic: String) {
        mMQTTManager.subscribeTopic(topic, mqttSubscribeListener)
    }

    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    fun onUnSubscribeTopic(topic: String) {
        mMQTTManager.unSubscribeTopic(topic)
    }


    /**
     * 取消订阅
     * @param topic 订阅消息的主题
     */
    fun onUnSubscribeTopic(topic: Array<String>) {
        mMQTTManager.unsubscribeTopic(topic)
    }

    /**
     * MQTT 订阅监听
     */
    private val mqttSubscribeListener = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            LogUtils.e("订阅成功")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            LogUtils.e("订阅失败 ${exception?.message}")
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
            LogUtils.e("MQTT 断开连接")
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

            LiveEventBus.get(MqttLiveEvent::class.java)
                .post(MqttLiveEvent().apply {
                    this.topic = topic
                    this.message = str_message
                })
        }

        /**
         * 在完成消息传递并且已收到所有确认时调用此方法
         * @param token
         */
        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            LogUtils.e("MQTT 消息发送完成")
        }

        /**
         * 在丢失重连成功后会触发该方法
         * @param reconnect
         * @param serverURI
         */
        override fun connectComplete(reconnect: Boolean, serverURI: String?) {
            LogUtils.e("MQTT 连接成功")
        }
    }
}