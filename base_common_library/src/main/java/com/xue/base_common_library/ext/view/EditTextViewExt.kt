package com.xue.base_common_library.ext.view


import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils

/**
 * 检测文本改变之后的状态 文本内容输入完成之后调用
 */
fun EditText.afterTextChange(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        /**
         *  检测文本改变之后的状态 文本内容输入完成之后调用
         * @param s Editable  输入结束呈现在输入框中的信息
         */
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

/**
 * 设置 [EditText] 文本，并且把光标定位到末尾
 */
fun EditText.setTextAndSelectionTextEnd(text: CharSequence?) {
    setTextAndSelectionTextEnd(text?.toString())
}

fun EditText.setTextAndSelectionTextEnd(text: String?) {
    setText(StringUtils.null2Length0(text))
    setSelectionTextEnd()
}

/**
 * 设置 [EditText] 文本，并且全选文本
 */
fun EditText.setTextWithSelection(text: CharSequence) {
    setText(text)
    setSelectionAllText()
}

/**
 * 移动光标至结尾
 */
fun EditText.setSelectionTextEnd() {
    if (!TextUtils.isEmpty(text)) {
        setSelection(getText().length)
    }
}

/**
 * [EditText] 文本 全选
 */
fun EditText.setSelectionAllText() {
    setSelection(0, getText().length)
}

/**
 * 获取文本
 */
fun EditText.textString(): String {
    return this.text.toString()
}

/**
 * 获取去除空字符串的文本
 */
fun EditText.textStringTrim(): String {
    return this.textString().trim()
}

/**
 * 文本是否为空
 */
fun EditText.isEmpty(): Boolean {
    return this.textString().isEmpty()
}

/**
 * 去空字符串后文本是否为空
 */
fun EditText.isTrimEmpty(): Boolean {
    return this.textStringTrim().isEmpty()
}


/**
 * 去空字符串后文本是否为空
 */
fun EditText.isNoTrimEmpty(): Boolean = !isTrimEmpty()

/**
 * 获取文本
 */
fun TextView.textString(): String {
    return this.text.toString()
}

/**
 * 获取去除空字符串的文本
 */
fun TextView.textStringTrim(): String {
    return this.textString().trim()
}

/**
 * 文本是否为空
 */
fun TextView.isEmpty(): Boolean {
    return this.textString().isEmpty()
}

/**
 * 文本是否为空
 */
fun TextView.isNoEmpty(): Boolean = !isEmpty()

/**
 * 去空字符串后文本是否为空
 */
fun TextView.isTrimEmpty(): Boolean {
    return this.textStringTrim().isEmpty()
}

/**
 * 去空字符串后文本是否为空
 */
fun TextView.isNoTrimEmpty(): Boolean = !isTrimEmpty()