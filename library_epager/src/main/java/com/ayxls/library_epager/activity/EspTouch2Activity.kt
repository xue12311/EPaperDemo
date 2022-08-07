package com.ayxls.library_epager.activity

import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.R
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.databinding.ActivityEsptouch2Binding
import com.ayxls.library_epager.ext.requestPermission
import com.ayxls.library_epager.ext.showPermissionDialog
import com.ayxls.library_epager.viewmodel.EspTouch2ViewModel
import com.blankj.utilcode.util.PermissionUtils
import com.xue.base_common_library.base.activity.BaseVmVbActivity

@Route(path = ARouterConstants.ARouterActivityEspTouch2)
class EspTouch2Activity : BaseVmVbActivity<EspTouch2ViewModel, ActivityEsptouch2Binding>() {
    //wifi 管理器
    private val mWifiManager by lazy { getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun createObserver() {
        super.createObserver()
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initData() {
        super.initData()
        onCheckLocationPermission()
    }

    private fun onCheckLocationPermission() {
        requestPermission(mViewModel.getAccessFineLocationPermission(), {
            showToastMessage("获取权限成功")
        }, {
            showPermissionDialog(
                getString(R.string.text_dialog_location_permission_title),
                getString(R.string.text_dialog_location_permission_message),
                getString(R.string.text_dialog_permission_authorize), {
                    PermissionUtils.permission(it)
                        .callback(object : PermissionUtils.FullCallback {
                            override fun onGranted(granted: MutableList<String>) {
                                showToastMessage("获取权限成功")
                            }
                            override fun onDenied(
                                deniedForever: MutableList<String>,
                                denied: MutableList<String>
                            ) {
                                if (deniedForever.isNotEmpty()) {
                                    showToastMessage("获取权限失败 不再询问")
                                } else {
                                    showToastMessage("获取权限失败")
                                }
                            }
                        }).request()
                }, {
                    showToastMessage("获取权限失败")
                }
            )
        }, {
            showPermissionDialog(
                getString(R.string.text_dialog_location_permission_title),
                getString(R.string.text_dialog_location_permission_message2),
                getString(R.string.text_dialog_permission_setting), {
                    PermissionUtils.launchAppDetailsSettings()
                }, {
                    showToastMessage("获取权限失败 不再询问")
                })
        })
    }
}