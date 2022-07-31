package com.xue.base_common_library.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun layoutId(): Int

    open fun initWindowParam() {}

    open fun initView(savedInstanceState: Bundle?){}

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

}