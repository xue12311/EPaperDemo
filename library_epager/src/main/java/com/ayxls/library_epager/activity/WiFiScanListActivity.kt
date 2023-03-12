package com.ayxls.library_epager.activity

import android.os.Bundle
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.databinding.ActivityWifiScanListBinding
import com.ayxls.library_epager.viewmodel.WiFiScanListViewModel
import com.xue.base_common_library.base.activity.BaseVmVbActivity

/**
 * Wifi 扫描 列表
 */
@Route(path = ARouterConstants.ARouterActivityWiFiScanList)
class WiFiScanListActivity : BaseVmVbActivity<WiFiScanListViewModel, ActivityWifiScanListBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //左侧添加 返回键
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //返回按钮
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}