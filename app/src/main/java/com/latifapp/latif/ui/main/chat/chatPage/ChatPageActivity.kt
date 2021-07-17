package com.latifapp.latif.ui.main.chat.chatPage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latifapp.latif.MyFirebaseMessagingService
import com.latifapp.latif.R
import com.latifapp.latif.data.models.ChatResponseModel
import com.latifapp.latif.data.models.MsgNotification
import com.latifapp.latif.databinding.ActivityChatPageBinding
import com.latifapp.latif.databinding.CallDialogBinding
import com.latifapp.latif.databinding.ViewAdPopupBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.DetailsActivity
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatPageActivity : BaseActivity<ChatViewModel, ActivityChatPageBinding>() {
    companion object {
        var MSG_LIVE_DATA: MutableLiveData<MsgNotification>? = null
    }

    private var isLoadingData = false
    private lateinit var adapter_: ChatPageAdapter
    private var msgID: String? = null
    private lateinit var viewAdPopUp: PopupWindow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setList()

        if (intent.extras?.getString("adOwner_id") != null) {

            viewModel.adOwner_id = "${intent.extras?.getString("adOwner_id")}"
            viewModel.ad_id = "${intent.extras?.getString("ad_id")}"
            Utiles.log_D("dndndnndnlllll", "${intent.extras?.getString("adOwner_name")}")
            setUserInfo(
                "${intent.extras?.getString("adOwner_name")}",
                "${intent.extras?.getString("adOwner_pic")}"
            )
        } else if (intent.extras?.get("model") != null) {

            val model: MsgNotification = intent.extras?.get("model") as MsgNotification
            Utiles.log_D("nndndndnkddmdmdm", model)

            viewModel.ad_id = model.prod_id
            viewModel.room = model.chat_room
            if (model.chat_room.isNullOrEmpty())
                viewModel.adOwner_id = model.sender_id
            // setUserInfo(model?.sender_name, model?.sender_avater)

            setUserInfo(
                "${model.sender_name}",
                "${model.sender_avater}"
            )
        }
        binding.loader2.visibility = View.VISIBLE
        MSG_LIVE_DATA = MutableLiveData<MsgNotification>(null)
        if (!viewModel.room.isNullOrEmpty())
            getChat()
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.sendBtn.setOnClickListener {

            if (!binding.typeMsg.text.toString().isEmpty()) {
                sendMsg_()

            }
        }

        MSG_LIVE_DATA?.observe(this, Observer {
            if (it != null) {
                if (it.chat_room?.equals("${viewModel.room}") &&
                    !it?.sender_id.equals("${viewModel.userID}")
                )
                    addNewComment(it.message, "")
                MSG_LIVE_DATA?.value = null
            }

        })

        binding.viewAdBtn.setOnClickListener {
            viewAdDialogShow(it)
        }
    }

    private fun setUserInfo(name: String, image: String) {
        binding.nameTxt.text = name

        if (!image.isNullOrEmpty()) {
            Glide.with(this).load(image).placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person).into(binding.image)
        }
    }

    private fun getChat() {
        binding.loader2.visibility = View.VISIBLE
        viewModel.getchatRoomMsgs(msgID).observe(this, Observer {

            if (!it.isNullOrEmpty()) {
                adapter_.list.addAll(0, it.reversed())
                adapter_.notifyDataSetChanged()
                isLoadingData = false
                if (msgID.isNullOrEmpty())// first page
                    binding.list.scrollToPosition(adapter_.list.size - 1)
                else {
                    val allSize=adapter_.list.size - 1;
                    val newListSize=it.size
                    binding.list.scrollToPosition(allSize-newListSize)

                }

                msgID = it.get(it.size - 1).id
            }
            if (it != null)
                binding.loader2.visibility = View.GONE

        })
    }

    private fun addNewComment(message: String, sender: String) {
        adapter_.apply {
            addComment(
                ChatResponseModel(
                    message = message,
                    senderId = sender

                )
            )
            binding.list.scrollToPosition(itemCount - 1)
        }
    }

    private fun sendMsg_() {
        viewModel.sendMsg(binding.typeMsg.text.toString()).observe(this, Observer {
            if (it) {
                addNewComment(binding.typeMsg.text.toString(), viewModel.userID)
                binding.typeMsg.text = null
            }
        })
    }

    private fun setList() {
        adapter_ = ChatPageAdapter(viewModel.userID)
        binding.list.apply {
            layoutManager = LinearLayoutManager(this@ChatPageActivity)
            adapter = adapter_
            addOnScrollListener(scrollListener)
        }
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {


        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount


            if (!isLoadingData && layoutManager.findFirstCompletelyVisibleItemPosition() <= 1) {
                isLoadingData = true
                getChat()
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): ActivityChatPageBinding {
        return ActivityChatPageBinding.inflate(layoutInflater)
    }

    override fun showLoader() {
        binding.loader.visibility = View.VISIBLE
        binding.sendBtn.setEnabled(false)
        binding.typeMsg.setEnabled(false)
    }

    override fun hideLoader() {
        Utiles.log_D("ncncncncnncn111", "here1")
        binding.loader.visibility = View.GONE
        binding.sendBtn.setEnabled(true)
        binding.typeMsg.setEnabled(true)
        Utiles.log_D("ncncncncnncn111", "here")
      //  binding.loader2.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        MSG_LIVE_DATA = null;
    }

    private fun viewAdDialogShow(view: View) {
        if (!::viewAdPopUp.isInitialized) {
            val popupBinding = ViewAdPopupBinding.inflate(layoutInflater)
            viewAdPopUp = PopupWindow(
                popupBinding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            viewAdPopUp.setFocusable(true)
            viewAdPopUp.setOutsideTouchable(true)
            popupBinding.viewAd.setOnClickListener {
                Utiles.log_D("mvmvmvmvmv", viewModel.ad_id)
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("ID", viewModel.ad_id.toInt())
                startActivity(intent)
                viewAdPopUp.dismiss()
            }

        }
        if (viewAdPopUp.isShowing)
            viewAdPopUp.dismiss()
        else
            viewAdPopUp.showAsDropDown(view)
    }
}