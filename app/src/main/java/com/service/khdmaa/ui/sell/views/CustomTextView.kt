package com.service.khdmaa.ui.sell.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import com.service.khdmaa.R

class CustomTextView(context_: Context, label: String) :
    CustomParentView<String>(context_, label,null) {

    override fun createView() {
        val textView = TextView(context)
        view = textView.apply {
            text = label
            textSize = 15f
            setTextColor(Color.BLACK)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface= ResourcesCompat.getFont(context, R.font.poppins_medium)
            params.setMargins(0, 40, 0, 5)
            layoutParams = params

        }

    }
}