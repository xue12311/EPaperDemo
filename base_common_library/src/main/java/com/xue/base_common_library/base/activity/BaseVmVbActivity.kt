package com.xue.base_common_library.base.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.xue.base_common_library.base.viewmodel.BaseViewModel
import com.xue.base_common_library.ext.inflateBindingWithGeneric

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: 包含 ViewModel 和 ViewBinding ViewModelActivity基类，把ViewModel 和 ViewBinding 注入进来了
 * 需要使用 ViewBinding 的清继承它
 */
abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmActivity<VM>() {
    override fun layoutId(): Int = 0

    lateinit var mViewBinding: VB
    /**
     * 创建 ViewBinding
     */
    override fun initDataBind(): View? {
        mViewBinding = inflateBindingWithGeneric(layoutInflater)
        return mViewBinding.root
    }

}