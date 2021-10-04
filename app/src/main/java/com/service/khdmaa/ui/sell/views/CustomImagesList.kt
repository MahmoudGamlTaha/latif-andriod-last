package com.service.khdmaa.ui.sell.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.service.khdmaa.databinding.AddImagesListBinding
import com.service.khdmaa.ui.sell.adapters.ImagesAdapter

class CustomImagesList (context_: Context, label: String, val adapter: ImagesAdapter, action: ViewAction<View>) :
    CustomParentView<View>(context_, label,action){


    override fun createView() {
        val binding = AddImagesListBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 40, 0, 5)
            root.layoutParams = params
            addImageBtn.setOnClickListener {
                action?.getActionId(addImageBtn)
                imagesList.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL
                    ,false)
                imagesList.adapter=adapter
            }
            label.text=this@CustomImagesList.label
        }


        view=binding.root
    }
}