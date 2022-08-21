package com.ayxls.library_epager.ext

fun String?.toDefaultInt(): Int = toDefaultInt(0)

/**
 * 带默认值的字符串转换为整数
 * @param default
 * @return
 */
fun String?.toDefaultInt(default: Int): Int =
    if (this == null) default else kotlin.runCatching { this.toInt() }.getOrDefault(default)

