package com.xue.base_common_library.base.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.xue.base_common_library.R
import com.xue.base_common_library.base.viewmodel.BaseViewModel
import com.xue.base_common_library.ext.getVmClazz
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {
    private val mLoadingDialog by lazy {
        XPopup.Builder(this).asLoading(StringUtils.getString(R.string.text_loading_message))
    }

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    open fun initWindowParam() {}

    open fun initView(savedInstanceState: Bundle?) {}

    open fun initListener() {}

    open fun initData() {}

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind(): View? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowParam()
        val view = initDataBind()
        if (view != null) {
            setContentView(view)
        } else {
            setContentView(layoutId())
        }
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        initListener()
        initData()
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        lifecycleScope.launchWhenCreated {
            mViewModel.isShowLoadingDialog().collect { show ->
                if (show) {
                    mLoadingDialog.show()
                } else {
                    mLoadingDialog.dismiss()
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            mViewModel.getLoadingDialogMessage().collect { message ->
                mLoadingDialog.setTitle(message)
            }
        }

        lifecycleScope.launchWhenCreated {
            mViewModel.getToastMessage().collect { message ->
                if (!StringUtils.isTrimEmpty(message)) {
                    ToastUtils.showShort(message)
                }
            }
        }
    }


    /**
     * 显示 加载中 提示框
     */
    fun showLoadingDialog() {
        mViewModel.showLoadingDialog()
    }

    /**
     * 显示 加载中 提示框
     * @param message 提示语
     */
    fun showLoadingDialog(message: String?) {
        mViewModel.showLoadingDialog(message)
    }


    /**
     * 隐藏 加载中 提示框
     */
    fun hideLoadingDialog() {
        mViewModel.hideLoadingDialog()
    }


    /**
     * 设置 加载中 提示框 提示语
     * @param message 提示语
     */
    fun setLoadingMessage(message: String?) {
        mViewModel.setLoadingMessage(message)
    }

    /**
     * 设置 Toast 提示音
     * @param message
     */
    fun showToastMessage(message: String?) {
        mViewModel.setToastMessage(message)
    }
}