package com.ayxls.library_epager.viewmodel

import com.ayxls.library_epager.repository.WiFiScanListRepository
import com.xue.base_common_library.base.viewmodel.BaseViewModel
/**
 * Wifi 扫描 列表
 */
class WiFiScanListViewModel : BaseViewModel() {
    private val repository by lazy { WiFiScanListRepository() }
}