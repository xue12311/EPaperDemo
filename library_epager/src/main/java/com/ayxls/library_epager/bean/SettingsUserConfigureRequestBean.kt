package com.ayxls.library_epager.bean

/**
 * Esp8266 设置 用户配置信息
 */
data class SettingsUserConfigureRequestBean(
    /**
     * mqtt 配置信息
     */
    var mqtt_configure: MQTTConfigureRequestBean? = null,
)

/**
 * MQTT 配置信息
 */
data class MQTTConfigureRequestBean(
    /**
     * mqtt 服务器地址
     */
    var mqtt_server: String? = null,
    /**
     * mqtt 服务器端口
     */
    var mqtt_port: Int = 0,
    /**
     * mqtt 用户名
     */
    var mqtt_username: String? = null,
    /**
     * mqtt 密码
     */
    var mqtt_password: String? = null,
    /**
     * mqtt 客户端id
     */
    var mqtt_client_id: String? = null,
    /**
     * mqtt 主题 前缀
     */
    var mqtt_topic_prefix: String? = null,
)