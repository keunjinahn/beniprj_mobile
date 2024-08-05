package kr.ac.beni.beniprj.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.ui.RunActivity


class BackgroundService : Service() {

    companion object{
        private const val PACKAGE_NAME                                  = "kr.ac.beni.beniprj"
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(getString(R.string.app_channel_id_location), getString(R.string.app_channel_id_location), NotificationManager.IMPORTANCE_LOW)
        channel.setShowBadge(false)
        channel.enableVibration(false)
        channel.setSound(null, null)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
        startForeground(Const.FOREGROUND_ID, getNotification(applicationContext.getString(R.string.app_name)))

        return START_STICKY
    }

    private var builder: NotificationCompat.Builder? = null
    private fun getNotification(title: String): Notification? {
        val intent = Intent(this, RunActivity::class.java)
        intent.putExtra("", true)
        val activityPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, Intent(this, RunActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        builder = NotificationCompat.Builder(this, getString(R.string.app_channel_id_location))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setContentText(getString(R.string.noti_foregorund_msg))
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(false)
            .setSound(null)
        return builder?.build()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private val binder = LocalBinder()
    inner class LocalBinder : Binder() {
        val service: BackgroundService
            get() = this@BackgroundService
    }
}