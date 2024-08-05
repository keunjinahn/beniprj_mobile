package kr.ac.beni.beniprj.util

import android.content.Context
import android.content.Intent
import android.content.pm.InstallSourceInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import kr.ac.beni.beniprj.Const
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommonUtils {
    companion object {

        private const val LOG = "###"
        private const val LOG_GPS = "GGG"
        private const val LOG_BLE = "BBB"
        fun commonLogGps(str: String){
            if(Const.DEBUG_MODE || Const.TEST_MODE) Log.d(LOG_GPS,str)
        }
        fun commonLogBle(str: String){
            if(Const.DEBUG_MODE || Const.TEST_MODE) Log.d(LOG_BLE,str)
        }
        fun commonLog(str: String){
            if(Const.DEBUG_MODE || Const.TEST_MODE) Log.i(LOG,str)
        }
        fun commonErrLog(str: String){
            if(Const.DEBUG_MODE || Const.TEST_MODE) Log.e(LOG,str)
        }

        fun isValidCarNumber(carNum: String): Boolean {
            var returnValue = false
            var regex = "^\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$"
            var p: Pattern = Pattern.compile(regex)
            var m: Matcher = p.matcher(carNum)
            if (m.matches()) {
                returnValue = true
            } else {
                regex = "^\\d{3}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$"
                p = Pattern.compile(regex)
                m = p.matcher(carNum)
                if (m.matches()) {
                    returnValue = true
                }else{
                    regex = "^[서울|부산|대구|인천|대전|광주|울산|제주|경기|강원|충남|전남|전북|경남|경북|세종]{2}\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}$"
                    p = Pattern.compile(regex)
                    m = p.matcher(carNum)
                    if (m.matches()) {
                        returnValue = true
                    }
                }
            }
            return returnValue
        }

        fun isRooting(): Boolean {
            var flag = false
            try {
                Runtime.getRuntime().exec("su")
                flag = true
            } catch (e: IOException) {
                commonErrLog("Rooting check = " + e.localizedMessage)
            }
            return flag
        }

        fun installedByStore(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val info: InstallSourceInfo = context.packageManager.getInstallSourceInfo(context.packageName)
                info.installingPackageName == "com.android.vending"       // 구글플레이 패키지명
            } else {
                val installer: String? = context.packageManager.getInstallerPackageName(context.packageName)
                installer == "com.android.vending"
            }
        }

        fun isDevMode(activity: Context): Boolean {
            return Settings.Secure.getInt(activity.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
        }

        private val HEX = "0123456789ABCDEF".toCharArray()
        fun toHexString(bytes: ByteArray): String {
            if (bytes.isEmpty()) {
                return ""
            }
            val hexChars = CharArray(bytes.size * 2)
            for (j in bytes.indices) {
                val v = (bytes[j].toInt() and 0xFF)
                hexChars[j * 2] = HEX[v ushr 4]
                hexChars[j * 2 + 1] = HEX[v and 0x0F]
            }
            return String(hexChars)
        }

        fun getNowTime():String{
            val tz = TimeZone.getTimeZone("Asia/Seoul")
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA) // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            return df.format(Date())
        }

        fun restart(context: Context) {
            val packageManager: PackageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent?.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }
}