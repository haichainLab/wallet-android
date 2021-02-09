package com.donkor.gank4camp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by luch on 2018/5/10.
 * Inten使用
 */
inline fun <reified T : Activity?> Activity.switchActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}
