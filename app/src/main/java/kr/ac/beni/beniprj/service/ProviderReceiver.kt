package kr.ac.beni.beniprj.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import kr.ac.beni.beniprj.util.CommonUtils

class ProviderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)){
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            CommonUtils.commonLog("getAction "+ locationManager.allProviders.toString())
        }
    }
}