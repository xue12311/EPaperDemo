package com.ayxls.library_epager.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.R
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.databinding.ActivityEsptouchBinding
import com.ayxls.library_epager.ext.showPermissionDialog
import com.ayxls.library_epager.ext.toDefaultInt
import com.ayxls.library_epager.viewmodel.EspTouchViewModel
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.StringUtils
import com.espressif.iot.esptouch2.provision.TouchNetUtil
import com.xue.base_common_library.base.activity.BaseVmVbActivity

@Route(path = ARouterConstants.ARouterActivityEspTouch2)
class EspTouchActivity : BaseVmVbActivity<EspTouchViewModel, ActivityEsptouchBinding>() {
    /**
     * 密码输入框
     */
    private val mEtWifiPassword by lazy { mViewBinding.editApPassword }

    /**
     * 设备数量输入框
     */
    private val mEtDeviceCount by lazy { mViewBinding.editDeviceCount }

    //wifi 管理器
    private val mWifiManager by lazy { applicationContext.getSystemService(WIFI_SERVICE) as WifiManager }

    //界面跳转
    private var mActivityLauncher: ActivityResultLauncher<Intent>? = null

    //确认按钮
    private val mButConfirm by lazy { mViewBinding.btnConfirm }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //左侧添加 返回键
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), {
            setActivityResultCallback(it)
        })
    }

    override fun createObserver() {
        super.createObserver()
    }

    override fun initListener() {
        super.initListener()
        ClickUtils.applySingleDebouncing(arrayOf(mButConfirm), mClickListener)
    }

    override fun initData() {
        super.initData()
        //获取权限
        onCheckLocationPermission()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_library_epager_esp_touch, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //返回按钮
            android.R.id.home -> {
                finish()
                return true
            }
            //切换wifi
            R.id.menu_wifi_toggle -> {
                //onToggleWifi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                //确认按钮
                mButConfirm.id -> {
                    launchWiFiProvisioning()
                }
            }

        }
    }

    /**
     * Activity 回调
     */
    private fun setActivityResultCallback(mActivityResult: ActivityResult) {

    }

    /**
     * 更新布局信息
     */
    private fun onUpDateWiFiStatusUI() {
        val result = mViewModel.getWiFiResult()
        mViewBinding.tvApSsidText.setText(StringUtils.null2Length0(result.wifi_ssid))
        mViewBinding.tvApBssidText.setText(StringUtils.null2Length0(result.wifi_bssid))
        mViewBinding.tvIpText.setText(StringUtils.null2Length0(result.wifi_ip_address?.hostAddress))
    }


    //---------------------    传递wifi信息到esp8266开发版     -------------------------------------------------------------------------------

    /**
     * 启用 wifi 配置
     */
    private fun launchWiFiProvisioning() {
        val result = mViewModel.getWiFiResult()
        if (!result.permission_granted) {
            //获取权限
            onCheckLocationPermission()
        } else if (!result.wifi_connected || result.is5G) {
            showToastMessage(result.message)
        } else if (StringUtils.isTrimEmpty(mEtWifiPassword.text.toString())) {
            showToastMessage(StringUtils.getString(R.string.esptouch2_password_label_hint))
        } else if (StringUtils.isTrimEmpty(mEtDeviceCount.text.toString())) {
            showToastMessage(StringUtils.getString(R.string.esptouch2_device_count_label_hint))
        } else {
            result.wifi_password = StringUtils.null2Length0(mEtWifiPassword.text.toString())
            result.device_count = StringUtils.null2Length0(mEtDeviceCount.text.toString()).toDefaultInt()
        }
    }

    //---------------------    获取wifi信息     -------------------------------------------------------------------------------


    /**
     * 获取 wifi信息
     */
    private fun getCurrentWifiInfo() {
        setWifiInfoData(mWifiManager.connectionInfo)
        onUpDateWiFiStatusUI()
    }

    /**
     * 获取wifi信息
     */
    private fun setWifiInfoData(wifiInfo: WifiInfo) {
        val result = mViewModel.getWiFiResult()
        result.onClearWiFiData()
        result.wifi_connected = isWifiConnected(wifiInfo)
        if (!result.wifi_connected) {
            result.message = getString(R.string.esptouch_message_wifi_connection)
            return
        }
        result.is5G = TouchNetUtil.is5G(wifiInfo.frequency)
        if (result.is5G) {
            result.message = getString(R.string.esptouch_message_wifi_frequency)
            return
        }

        result.wifi_ssid = TouchNetUtil.getSsidString(wifiInfo)

        result.wifi_ssid_bytes = TouchNetUtil.getRawSsidBytes(wifiInfo)

        result.wifi_bssid = wifiInfo.bssid

        if (wifiInfo.ipAddress != 0) {
            result.wifi_ip_address = TouchNetUtil.getAddress(wifiInfo.ipAddress)
        } else {
            result.wifi_ip_address = TouchNetUtil.getIPv4Address()
            if (result.wifi_ip_address == null) {
                result.wifi_ip_address = TouchNetUtil.getIPv6Address()
            }
        }
    }

    /**
     * 判断 wifi 是否已经连接
     * @param wifi
     * @return true 已连接 false 未连接
     */
    private fun isWifiConnected(wifi: WifiInfo?): Boolean {
        return wifi != null && wifi.networkId != -1 && !"<unknown ssid>".equals(wifi.ssid)
    }

//---------------------    申请权限     -------------------------------------------------------------------------------

    /**
     * 权限申请成功
     */
    private fun onLocationPermissionGranted() {
        mViewModel.getWiFiResult().permission_granted = true
        getCurrentWifiInfo()
    }

    /**
     * 被拒绝且未勾选不再询问
     */
    private fun onLocationPermissionDenied() {
        showToastMessage("获取权限失败")
        mViewModel.getWiFiResult().message = StringUtils.getString(R.string.text_not_authorize_please_authorize_first)
        mViewModel.getWiFiResult().permission_granted = false
    }

    /**
     * 被拒绝且勾选不再询问
     */
    private fun onLocationPermissionExplained() {
        showToastMessage("获取权限失败 不再询问")
        mViewModel.getWiFiResult().message = StringUtils.getString(R.string.text_not_authorize_please_authorize_first)
        mViewModel.getWiFiResult().permission_granted = false
    }

    /**
     * 申请权限
     */
    private fun onCheckLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(
                    this,
                    mViewModel.getAccessFineLocationPermission()
                ) == PackageManager.PERMISSION_GRANTED
            ) {//已经授权
                onLocationPermissionGranted()
            } else {//申请授权
                requestLocationPermission.launch(mViewModel.getAccessFineLocationPermission())
            }
        } else {
            onLocationPermissionGranted()
        }
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            //权限获取成功
            if (isGranted) {
                onLocationPermissionGranted()
            } else { //权限获取失败
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //是否已经勾选 禁止后不再询问
                    val isDenied =
                        shouldShowRequestPermissionRationale(mViewModel.getAccessFineLocationPermission())
                    if (isDenied) {
                        //被拒接 且 没勾选不再询问
                        showLocationPermissionDialog()
                    } else {
                        //被拒接 且 勾选了不再询问
                        showLocationPermissionSettingDialog()
                    }
                } else {
                    onLocationPermissionGranted()
                }
            }
        }

    /**
     * 位置权限 提示框 - 未勾选不再询问
     */
    private fun showLocationPermissionDialog() {
        showPermissionDialog(
            getString(R.string.text_dialog_location_permission_title),
            getString(R.string.text_dialog_location_permission_message),
            getString(R.string.text_dialog_permission_authorize), {
                requestLocationPermission.launch(mViewModel.getAccessFineLocationPermission())
            }, {
                onLocationPermissionDenied()
            }
        )
    }

    /**
     * 位置权限 提示框 - 勾选了不再询问
     */
    private fun showLocationPermissionSettingDialog() {
        showPermissionDialog(
            getString(R.string.text_dialog_location_permission_title),
            getString(R.string.text_dialog_location_permission_message2),
            getString(R.string.text_dialog_permission_setting), {
                PermissionUtils.launchAppDetailsSettings()
            }, {
                onLocationPermissionExplained()
            })
    }
}