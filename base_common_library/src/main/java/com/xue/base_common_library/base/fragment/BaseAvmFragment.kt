package com.xue.base_common_library.base.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.xue.base_common_library.R
import com.xue.base_common_library.base.viewmodel.BaseViewModel
import com.xue.base_common_library.constant.AppBaseConstant
import com.xue.base_common_library.ext.getVmClazz

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment
 */

abstract class BaseAvmFragment<AVM : BaseViewModel> : Fragment() {
    private val mHandler = Handler(Looper.getMainLooper())
    private val mLoadingDialog by lazy {
        XPopup.Builder(requireContext()).asLoading(StringUtils.getString(R.string.text_loading_message))
    }

    //是否第一次加载
    private var isFirst: Boolean = true

    lateinit var mViewModel: AVM

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    open fun initListener() {}

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun firstLazyLoadData() {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = initDataBind(inflater, container, savedInstanceState)
        if (view != null) {
            return view
        } else {
            return inflater.inflate(layoutId(), container, false)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = getActivityViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        initListener()
        onVisible()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }


    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            mHandler.postDelayed({
                if (isFirst) {
                    firstLazyLoadData()
                    isFirst = false
                } else {
                    lazyLoadData()
                }
            }, lazyLoadTime())
        }
    }


    /**
     * 获得activity中的 ViewModel
     */
    private fun <AVM : BaseViewModel> getActivityViewModel(): AVM =
        ViewModelProvider(requireActivity()).get(getVmClazz(requireActivity()))


    /**
     * 创建viewModel
     */
    private fun <VM : BaseViewModel> createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 创建LiveData数据观察者
     */
    open fun createObserver(){}

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

    /**
     * 供子类BaseVmDbFragment 初始化Databinding操作
     */
    open fun initDataBind(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return AppBaseConstant.FragmentLazyLoadTime
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}