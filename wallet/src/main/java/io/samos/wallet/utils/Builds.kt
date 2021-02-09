package io.samos.wallet.utils

import android.os.Build
import io.samos.wallet.BuildConfig

/**
 * 系统版本相关方法
 *
 * @author 方丈
 * @date 2017/8/14 14:50
 * @version v4.0.0
 */

/**
 * 版本是否高于或等于4.4.2
 */
fun hasKitKat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
}

/**
 * 版本是否高于或等于5.1
 */
fun hasLollipop(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

/**
 * 版本是否高于或等于6.0
 */
fun hasMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

/**
 * 版本是否高于或等于7.0
 */
fun hasNougat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun getVersion() : Int{
    return BuildConfig.VERSION_CODE
}