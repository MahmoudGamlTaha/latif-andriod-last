package com.latifapp.latif.ui.main.chat.chatHistoryList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.R
import com.latifapp.latif.data.models.MsgNotification
import com.latifapp.latif.databinding.ActivitySubscribBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.main.chat.chatPage.ChatPageActivity
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatHistoryListActivity : BaseActivity<ChatHistoryViewModel,ActivitySubscribBinding>(), ChatHistoryListAdapter.Action {
    private var isLoadingData: Boolean=false
    private var index: Int=0
    private lateinit var adapter_:ChatHistoryListAdapter

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        binding.toolbar.title.text = getString(R.string.chat)
        binding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter_=ChatHistoryListAdapter(this@ChatHistoryListActivity)
            adapter=adapter_
            addOnScrollListener(scrollListener)
        }

        getMyRooms()
    }


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true
                getMyRooms()
            }
        }
    }
    private fun getMyRooms() {

        viewModel.getAllMyRoomsChat(index).observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                index++
                adapter_.list.addAll(it)
                adapter_.notifyDataSetChanged()
                isLoadingData=false
            }
        })
    }

    override fun onItemClick(model: MsgNotification) {
        val intent =Intent(this,ChatPageActivity::class.java)
        model.device_model="android"
        intent.putExtra("model",model)
        startActivity(intent)
    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySubscribBinding {
        return ActivitySubscribBinding.inflate(layoutInflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}