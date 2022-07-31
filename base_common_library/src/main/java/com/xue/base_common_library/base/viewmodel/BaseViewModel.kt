package com.xue.base_common_library.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseViewModel : ViewModel() {

    //显示 加载中 提示框
    private val _show_loading_dialog = MutableStateFlow(false)

    //显示 加载中 提示框 提示语
    private val _loading_dialog_message = MutableStateFlow<String?>(null)

    // Toast 提示音
    private val _toast_message = MutableStateFlow<String?>(null)

    /**
     * 当前 是否显示 加载中 提示框
     */
    fun isShowLoadingDialog(): MutableStateFlow<Boolean> {
        return _show_loading_dialog
    }

    /**
     * 当前 加载中 提示框 提示语
     */
    fun getLoadingDialogMessage(): MutableStateFlow<String?> {
        return _loading_dialog_message
    }

    /**
     * Toast 提示音
     */
    fun getToastMessage(): MutableStateFlow<String?> {
        return _toast_message
    }

    /**
     * 显示 或 隐藏 加载中 提示框
     * @param show true 显示 false 隐藏
     */
    private fun setShowOrHideLoadingDialog(show: Boolean) {
        _show_loading_dialog.value = show
    }

    /**
     * 显示 加载中 提示框
     * @param message 提示语
     */
    fun showLoadingDialog(message: String?) {
        setLoadingMessage(message)
        showLoadingDialog()
    }

    /**
     * 显示 加载中 提示框
     */
    fun showLoadingDialog() {
        setShowOrHideLoadingDialog(true)
    }

    /**
     * 隐藏 加载中 提示框
     */
    fun hideLoadingDialog() {
        setShowOrHideLoadingDialog(false)
    }

    /**
     * 设置 加载中 提示框 提示语
     * @param message 提示语
     */
    fun setLoadingMessage(message: String?) {
        _loading_dialog_message.value = message ?: "加载中..."
    }

    /**
     * 设置 Toast 提示音
     * @param message
     */
    fun setToastMessage(message: String?) {
        _toast_message.value = message
    }

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        //viewModelScope 默认是 Dispatchers.Main
        viewModelScope.launch {
            block()
        }
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }
    /**
     * @param block     协程代码块，运行在UI线程
     * @param onError   异常回调，运行在UI线程
     * @param onStart   协程开始回调，运行在UI线程
     * @param onFinally 协程结束回调，不管成功/失败，都会回调，运行在UI线程
     */
    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((Throwable) -> Unit)? = null,
        onStart: (() -> Unit)? = null,
        onFinally: (() -> Unit)? = null
    ): Job {
        return viewModelScope.launch {
            try {
                coroutineScope {
                    onStart?.invoke()
                    block()
                }
            } catch (e: Throwable) {
                if (onError != null && isActive) {
                    try {
                        onError(e)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    e.printStackTrace()
                }
            } finally {
                onFinally?.invoke()
            }
        }
    }
}