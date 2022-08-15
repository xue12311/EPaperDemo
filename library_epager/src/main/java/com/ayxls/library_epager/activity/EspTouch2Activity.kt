package com.ayxls.library_epager.activity

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.R
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.databinding.ActivityEsptouch2Binding
import com.ayxls.library_epager.ext.showPermissionDialog
import com.ayxls.library_epager.viewmodel.EspTouch2ViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.xue.base_common_library.base.activity.BaseVmVbActivity

@Route(path = ARouterConstants.ARouterActivityEspTouch2)
class EspTouch2Activity : BaseVmVbActivity<EspTouch2ViewModel, ActivityEsptouch2Binding>() {
    //wifi 管理器
    private val mWifiManager by lazy { applicationContext.getSystemService(WIFI_SERVICE) as WifiManager }
    private val mConnectivityManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getSystemService(ConnectivityManager::class.java)) as ConnectivityManager
        } else {
            (getSystemService(Context.CONNECTIVITY_SERVICE)) as ConnectivityManager
        }
    }
    private val mNetworkRequest by lazy {
        NetworkRequest
            .Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

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
        //获取权限
        onCheckLocationPermission()
    }


    //---------------------    获取wifi信息     -------------------------------------------------------------------------------


    /**
     * 获取 wifi信息
     */
    private fun getCurrentWifiInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && mNetworkCallback != null) {
            mConnectivityManager.requestNetwork(mNetworkRequest, mNetworkCallback)
        } else {
            setWifiInfoData(mWifiManager.connectionInfo)
        }
    }

    /**
     * 获取wifi信息
     */
    private fun setWifiInfoData(wifiInfo: WifiInfo) {
        LogUtils.e("wifiInfo.ssid = ${wifiInfo.ssid}")
        LogUtils.e("wifiInfo.bssid = ${wifiInfo.bssid}")
        LogUtils.e("wifiInfo.ipAddress = ${wifiInfo.ipAddress}")
    }

    private val mNetworkCallback: ConnectivityManager.NetworkCallback? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val wifiInfo = networkCapabilities.transportInfo as WifiInfo
                    setWifiInfoData(wifiInfo)
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val wifiInfo = networkCapabilities.transportInfo as WifiInfo
                    setWifiInfoData(wifiInfo)
                }
            }
        } else {
            null
        }

    fun unregisterNetWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mConnectivityManager.bindProcessToNetwork(null)
        }
        if (mNetworkCallback != null) {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback)
        }
    }
//---------------------    申请权限     -------------------------------------------------------------------------------

    /**
     * 权限申请成功
     */
    private fun onLocationPermissionGranted() {
        showToastMessage("获取权限成功")
        getCurrentWifiInfo()
    }

    /**
     * 被拒绝且未勾选不再询问
     */
    private fun onLocationPermissionDenied() {
        showToastMessage("获取权限失败")
    }

    /**
     * 被拒绝且勾选不再询问
     */
    private fun onLocationPermissionExplained() {
        showToastMessage("获取权限失败 不再询问")
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
                    val isDenied = shouldShowRequestPermissionRationale(mViewModel.getAccessFineLocationPermission())
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetWork()
    }
}