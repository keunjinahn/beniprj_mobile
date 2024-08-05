package kr.ac.beni.beniprj

import android.app.Application
import kr.ac.beni.beniprj.util.PreferenceUtil
class VFApplication : Application() {
    override fun onCreate() {
        PreferenceUtil.init(this)
        super.onCreate()
    }
}