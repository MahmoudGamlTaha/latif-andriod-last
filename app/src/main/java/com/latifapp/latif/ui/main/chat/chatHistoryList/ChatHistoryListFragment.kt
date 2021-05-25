package com.latifapp.latif.ui.main.chat.chatHistoryList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivityChatPageBinding
import com.latifapp.latif.databinding.ActivitySubscribBinding
import com.latifapp.latif.databinding.FragmentChatHistoryListBinding
import com.latifapp.latif.ui.main.chat.chatPage.ChatPageActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatHistoryListFragment : AppCompatActivity(), ChatHistoryListAdapter.Action {

    private lateinit var binding: ActivitySubscribBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscribBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title.text = getString(R.string.chat)
        binding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter=ChatHistoryListAdapter(this@ChatHistoryListFragment)
        }
    }

    override fun onItemClick() {
        startActivity(Intent(this,ChatPageActivity::class.java))
    }
}