package com.service.khdmaa

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.service.khdmaa.data.models.MsgNotification
import com.service.khdmaa.ui.main.chat.chatPage.ChatPageActivity
import com.service.khdmaa.ui.main.chat.chatPage.ChatPageActivity.Companion.MSG_LIVE_DATA
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var pendingIntent: PendingIntent? = null
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        Utiles.log_D("onMessageReceived", msg)
        if (msg.notification != null && msg.data.isNullOrEmpty()) {
            Utiles.log_D("onMessageReceived", Gson().toJson(msg.notification))
            showNotification(
                msg.notification?.getTitle(),
                msg.notification?.getBody()
            )
        } else showNotification(msg.data)
    }

    private fun showNotification(data: Map<String, String>) {
        Log.d("nbnbnbnbnbnnbnnb2", data.toString())
        var title = ""
        var body = ""
        try {
            val jsonObject = JSONObject(data)
            title = jsonObject["sender_name"].toString()
            body = jsonObject["message"].toString()
            val sender_avater = jsonObject["sender_avater"].toString()
            val prod_id = jsonObject["prod_id"].toString()
            val chat_room = jsonObject["chat_room"].toString()
            val prod_name = jsonObject["prod_name"].toString()
            val sender_id = jsonObject["sender_id"].toString()

            val body = MsgNotification(
                sender_name = title,
                message = body,
                sender_avater = sender_avater,
                prod_id = prod_id,
                chat_room = chat_room,
                prod_name = prod_name,
                sender_id = sender_id
            )


            val intent = Intent(this, ChatPageActivity::class.java)
            intent.putExtra("model", body)
            pendingIntent = PendingIntent.getActivity(
                this, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            GlobalScope.launch {
                withContext(Dispatchers.Main) {

                    MSG_LIVE_DATA?.value = body
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("nbnbnbnbnbnnbnnb2", e.toString())
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val Notification_channelID = "com.service.khdmaa"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Notification_channelID,
                "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "5dmaa Notification"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = getColor(R.color.colorAccent)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, Notification_channelID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setContentInfo(getString(R.string.app_name))
            .setColorized(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setColor(resources.getColor(R.color.white))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setContentIntent(pendingIntent)
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val Notification_channelID = "com.service.khdmaa"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Notification_channelID,
                "Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "Latif Notification"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = getColor(R.color.colorAccent)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, Notification_channelID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setContentInfo(getString(R.string.app_name))
            .setColorized(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setColor(resources.getColor(R.color.white))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("TokenFirebase", s)
    }
}