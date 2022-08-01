package com.xue.base_common_library.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.XPopup
import com.xue.base_common_library.R
import com.xue.base_common_library.base.viewmodel.BaseViewModel

abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {
    private val mLoadingDialog by lazy {
        XPopup.Builder(this)
            .asLoading(StringUtils.getString(R.string.text_loading_message))
    }

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    open fun initWindowParam() {}

    open fun initView(savedInstanceState: Bundle?) {}

    open fun initListener() {}

    open fun initData() {}

}