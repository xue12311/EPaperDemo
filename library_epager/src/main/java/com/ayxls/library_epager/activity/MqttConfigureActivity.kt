package com.ayxls.library_epager.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.bean.live_event_bus.MqttLiveEvent
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.databinding.ActivityMqttConfigureBinding
import com.ayxls.library_epager.viewmodel.MqttConfigureViewModel
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.StringUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.xue.base_common_library.base.activity.BaseVmVbActivity

@Route(path = ARouterConstants.ARouterActivityMqttConfigure)
class MqttConfigureActivity : BaseVmVbActivity<MqttConfigureViewModel, ActivityMqttConfigureBinding>() {

    private val mEditMqttSubscribeTopic by lazy { mViewBinding.editMqttSubscribeTopics }

    private val mButMqttSubscribe by lazy { mViewBinding.buttonSubscribeMqtt }

    private val mButMqttUnSubscribe by lazy { mViewBinding.buttonUnsubscribeMqtt }

    private val mTvMqttSubscribeMessage by lazy { mViewBinding.tvSubscribeMessage }

    //mqtt 连接
    private val mMQTTLifecycle: MqttLifecycleObserver by lazy { MqttLifecycleObserver() }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //左侧添加 返回键
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun createObserver() {
        super.createObserver()
        LiveEventBus.get(MqttLiveEvent::class.java).observe(this) {
            showToastMessage("收到消息：${it.message}")
        }
    }

    override fun initListener() {
        super.initListener()
        lifecycle.addObserver(mMQTTLifecycle)
        ClickUtils.applySingleDebouncing(
            arrayOf(
                mButMqttSubscribe,
                mButMqttUnSubscribe,
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
                //mqtt订阅
                mButMqttSubscribe.id -> {
                    val topic = StringUtils.null2Length0(mEditMqttSubscribeTopic.text.toString())
                    if (topic.isNotEmpty()) {
                        mMQTTLifecycle.onSubscribeTopic(topic)
                    } else {
                        showToastMessage("请输入订阅主题")
                    }
                }
                //mqtt取消订阅
                mButMqttUnSubscribe.id -> {
                    val topic = StringUtils.null2Length0(mEditMqttSubscribeTopic.text.toString())
                    if (topic.isNotEmpty()) {
                        mMQTTLifecycle.onUnSubscribeTopic(topic)
                    } else {
                        showToastMessage("请输入订阅主题")
                    }
                }
            }
        }
    }
}