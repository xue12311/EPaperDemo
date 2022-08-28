package com.ayxls.library_epager.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.databinding.ActivityMqttConfigureBinding
import com.ayxls.library_epager.viewmodel.MqttConfigureViewModel
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.StringUtils
import com.xue.base_common_library.base.activity.BaseVmVbActivity

@Route(path = ARouterConstants.ARouterActivityMqttConfigure)
class MqttConfigureActivity : BaseVmVbActivity<MqttConfigureViewModel, ActivityMqttConfigureBinding>() {

    private val mButMqttConnect by lazy { mViewBinding.buttonConnectMqtt }

    private val mEditMqttSubscribeTopic by lazy { mViewBinding.editMqttSubscribeTopics }

    private val mButMqttSubscribe by lazy { mViewBinding.buttonSubscribeMqtt }

    private val mButMqttUnSubscribe by lazy { mViewBinding.buttonUnsubscribeMqtt }

    private val mButMqttDisconnect by lazy { mViewBinding.buttonDisconnectMqtt }

    private val mTvMqttSubscribeMessage by lazy { mViewBinding.tvSubscribeMessage }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //左侧添加 返回键
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun createObserver() {
        super.createObserver()
    }

    override fun initListener() {
        super.initListener()
        ClickUtils.applySingleDebouncing(
            arrayOf(
                mButMqttConnect,
                mButMqttSubscribe,
                mButMqttUnSubscribe,
                mButMqttDisconnect
            ), mClickListener
        )
    }

    override fun initData() {
        super.initData()
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

    private val mClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                //mqtt连接
                mButMqttConnect.id -> {
                    mViewModel.onMqttConnect()
                }
                //mqtt订阅
                mButMqttSubscribe.id -> {
                    val topic = StringUtils.null2Length0(mEditMqttSubscribeTopic.text.toString())
                    if (topic.isNotEmpty()) {
                        mViewModel.onMqttSubscribe(topic)
                    } else {
                        showToastMessage("请输入订阅主题")
                    }
                }
                //mqtt取消订阅
                mButMqttUnSubscribe.id -> {
                    val topic = StringUtils.null2Length0(mEditMqttSubscribeTopic.text.toString())
                    if (topic.isNotEmpty()) {
                        mViewModel.onMqttUnSubscribe(topic)
                    } else {
                        showToastMessage("请输入订阅主题")
                    }
                }
                //mqtt断开连接
                mButMqttDisconnect.id -> {
                    mViewModel.onMqttDisconnect()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.onMqttDisconnect()
    }
}