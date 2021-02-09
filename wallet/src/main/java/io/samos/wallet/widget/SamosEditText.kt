package io.samos.wallet.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.widget.TextView
import io.samos.wallet.R



/**
 * Created by Administrator
 * on 2018/6/4.
 */
class SamosEditText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setBackgroundResource(R.drawable.et_line_normal)
//        setHintTextColor(resources.getColor(R.color.C5))
        try {//修改光标的颜色（反射）
            val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.isAccessible = true
            f.set(this, R.drawable.et_cursor_drawable)


        } catch (ignored: Exception) {
        }
    }


}