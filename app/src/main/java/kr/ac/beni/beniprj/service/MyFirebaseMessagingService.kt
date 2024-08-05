package kr.ac.beni.beniprj.service

//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.media.RingtoneManager
//import android.os.Bundle
//import androidx.lifecycle.*
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import kr.ac.beni.beniprj.R
//import kr.ac.beni.beniprj.ui.RunActivity
//import kr.ac.beni.beniprj.util.CommonUtils
//import kr.ac.beni.beniprj.util.PreferenceUtil
//import java.util.*

//class MyFirebaseMessagingService : FirebaseMessagingService(), LifecycleObserver{
//    var title: String? = null
//    private var body: String? = null
//    private var command: String? = null
//    private val IN_SPOT_NOTIFY_ID = 3366
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        CommonUtils.commonLog("MyFirebaseMessagingService ==> ${remoteMessage.data["title"]}" + "${remoteMessage.data["body"]}")
//        CommonUtils.commonLog("MyFirebaseMessagingService ==> ${remoteMessage.data["command"]}") //EXPIRED
//        if (remoteMessage.data.isNotEmpty()) {
//            title = remoteMessage.data["title"]
//            body = remoteMessage.data["body"]
//            command = remoteMessage.data["command"]
//            if(command == "EXPIRED") sendLocalIntent()
//            if(PreferenceUtil.getIsNotify()) sendNotification()
//        }
//    }
//    override fun onNewToken(token: String) {
//        CommonUtils.commonLog("onNewToken ==> $token")
//        PreferenceUtil.settPrefFCMToken(token)
//    }
//
//    private fun sendNotification() {
//        val intent = Intent(this, RunActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
//        val channelId = getString(R.string.app_channel_id_fcm)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//
//        val notificationBuilder  = androidx.core.app.NotificationCompat.Builder(this, getString(R.string.app_channel_id_location))
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
//            .setContentTitle(title)
//            .setContentText(body)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val channel = NotificationChannel(channelId, "HVFG_ALARM", NotificationManager.IMPORTANCE_DEFAULT)
//        notificationManager.createNotificationChannel(channel)
//
//        if(command == "IN_SPOT"){
//            notificationManager.notify(IN_SPOT_NOTIFY_ID,notificationBuilder.build())
//        }else{
//            notificationManager.notify(System.currentTimeMillis().toInt(),notificationBuilder.build())
//        }
//    }
//
//    private fun sendLocalIntent() {
//        val intent = Intent(BackgroundService.ACTION_BROADCAST)
//        val bundle = Bundle()
//        bundle.putBoolean(BackgroundService.BUNDLE_KEY_WORK_EXPIRED, true)
//        intent.putExtras(bundle)
//        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
//    }
//}