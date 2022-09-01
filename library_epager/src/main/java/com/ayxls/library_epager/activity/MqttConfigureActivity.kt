package com.ayxls.library_epager.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.ayxls.library_epager.R
import com.ayxls.library_epager.base.BaseMqttVmVbActivity
import com.ayxls.library_epager.constant.ARouterConstants
import com.ayxls.library_epager.constant.MqttConstant
import com.ayxls.library_epager.databinding.ActivityMqttConfigureBinding
import com.ayxls.library_epager.utils.mqtt.MqttConnectionStatusBean
import com.ayxls.library_epager.viewmodel.MqttConfigureViewModel
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.StringUtils

@Route(path = ARouterConstants.ARouterActivityMqttConfigure)
class MqttConfigureActivity : BaseMqttVmVbActivity<MqttConfigureViewModel, ActivityMqttConfigureBinding>() {

    private val mEditMqttSubscribeTopic by lazy { mViewBinding.editMqttSubscribeTopics }

    private val mButMqttSubscribe by lazy { mViewBinding.buttonSubscribeMqtt }

    private val mButMqttUnSubscribe by lazy { mViewBinding.buttonUnsubscribeMqtt }

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
        //连接mqtt
        mViewModel.onConnectMQTT()
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

    /**
     * 更新 MQTT 连接状态
     * @param bean
     */
    override fun onUpDateMqttStatus(bean: MqttConnectionStatusBean) {
        super.onUpDateMqttStatus(bean)
        SnackbarUtils.with(mViewBinding.root)
            .setMessage("MQTT 连接状态：${bean.getMqttStatusText()}")
            .setMessageColor(ColorUtils.getColor(R.color.white))
            .setBgColor(ColorUtils.getColor(R.color.colorPrimary))
            .show()
        //收到一条消息
        if (bean.status == MqttConstant.MqttStatusType.MQTT_RECEIVE_SUBSCRIBE_MESSAGE &&
            bean.message != null
        ) {
            showSubscribeMessage(bean.message!!)
        }
    }

    /**
     * 显示 收到的消息
     */
    private fun showSubscribeMessage(bean: MqttConnectionStatusBean.MqttMessageBean) {
        val str_message = StringBuilder()
        str_message.append("收到消息：\n")
        str_message.append("        时间 : ${bean.time}\n")
        str_message.append("        主题 : ${bean.topic}\n")
        str_message.append("        消息 : ${bean.message}\n")
        val old_message = mTvMqttSubscribeMessage.text.toString()
        if (old_message.length > 500) {
            str_message.append(old_message.substring(0, 500))
        } else {
            str_message.append(old_message)
        }
        mTvMqttSubscribeMessage.setText(str_message)
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
                        //订阅mqtt
                        mViewModel.onSubscribeTopicMQTT(topic)
                    } else {
                        showToastMessage("请输入订阅主题")
                    }
                }
                //mqtt取消订阅
                mButMqttUnSubscribe.id -> {
                    val topic = StringUtils.null2Length0(mEditMqttSubscribeTopic.text.toString())
                    if (topic.isNotEmpty()) {
                        //取消mqtt订阅
                        mViewModel.onUnSubscribeTopicMQTT(topic)
                    } else {
                        showToastMessage("请输入订阅主题")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //断开mqtt连接
        mViewModel.onDisConnectMQTT()
    }
}