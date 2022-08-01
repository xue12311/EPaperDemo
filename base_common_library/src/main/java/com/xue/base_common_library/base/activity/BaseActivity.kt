package com.xue.base_common_library.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.xue.base_common_library.R

abstract class BaseActivity : AppCompatActivity() {
    private val mLoadingDialog by lazy {
        XPopup.Builder(this)
            .asLoading(StringUtils.getString(R.string.text_loading_message))
    }


    abstract fun layoutId(): Int

    open fun initWindowParam() {}

    open fun initView(savedInstanceState: Bundle?) {}

    open fun initListener() {}

    open fun initData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowParam()
        setContentView(layoutId())
        initView(savedInstanceState)
        initListener()
        initData()
    }

    /**
     * 显示 加载中 提示框
     */
    private fun showLoadingDialog() {
        setShowOrHideLoadingDialog(true)
    }

    /**
     * 显示 加载中 提示框
     * @param message 提示语
     */
    private fun showLoadingDialog(message: String?) {
        setLoadingMessage(message)
        setShowOrHideLoadingDialog(true)
    }

    /**
     * 隐藏 加载中 提示框
     */
    private fun hideLoadingDialog() {
        setShowOrHideLoadingDialog(false)
    }

    /**
     * 显示 或 隐藏 加载中 提示框
     * @param show true 显示 false 隐藏
     */
    private fun setShowOrHideLoadingDialog(show: Boolean) {
        if (show) {
            mLoadingDialog.show()
        } else {
            mLoadingDialog.dismiss()
        }
    }

    /**
     * 设置 加载中 提示框 提示语
     * @param message 提示语
     */
    private fun setLoadingMessage(message: String?) {
        mLoadingDialog.setTitle(message)
    }


    /**
     * 设置 Toast 提示音
     * @param message
     */
    private fun showToast(message: String?) {
        if (!StringUtils.isTrimEmpty(message)) {
            ToastUtils.showShort(message)
        }
    }

}