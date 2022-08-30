package com.ayxls.library_epager.bean.live_event_bus

import com.jeremyliao.liveeventbus.core.LiveEvent

class MqttLiveEvent() : LiveEvent {
    var topic: String? = null
    var message: String? = null
}