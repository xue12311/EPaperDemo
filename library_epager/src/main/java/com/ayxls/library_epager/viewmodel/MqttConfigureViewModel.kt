package com.ayxls.library_epager.viewmodel

import com.ayxls.library_epager.repository.MqttConfigureRepository
import com.xue.base_common_library.base.viewmodel.BaseViewModel

class MqttConfigureViewModel : BaseViewModel() {
    private val repository by lazy { MqttConfigureRepository() }
}