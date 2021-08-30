package com.latifapp.latif.ui.zommingImage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.latifapp.latif.databinding.ActivityZoomingImageBinding
import kotlinx.android.synthetic.main.country_dialog.*

class ZoomingImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
         var binding = ActivityZoomingImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        val images = intent.extras?.getSerializable("images") as List<String>
        val position: Int? =intent.extras?.getInt("position")

        binding.imagesList.apply {
            layoutManager=LinearLayoutManager(this@ZoomingImageActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter=ZomingImagesAdapter(images)
            scrollToPosition(position?:0)
        }
        PagerSnapHelper().attachToRecyclerView(binding.imagesList)
    }
}