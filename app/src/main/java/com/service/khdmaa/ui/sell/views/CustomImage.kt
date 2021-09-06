package com.service.khdmaa.ui.sell.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.service.khdmaa.databinding.CustomImageBinding

class CustomImage (context_: Context, label: String, action: ViewAction<ImageView>) :
    CustomParentView<ImageView>(context_, label,action){
    override fun createView() {
        val image=CustomImageBinding.inflate(LayoutInflater.from(context))
        view=image.apply {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(0, 40, 0, 5)
            root.layoutParams = params
            imageview?.setOnClickListener {
                action?.getActionId(imageview!!)

            }
            label.text=this@CustomImage.label
        }.root

    }
}