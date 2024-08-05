package kr.ac.beni.beniprj.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceUtil {

    companion object{
        private const val TAG  = "Preferences"
        private const val name = "hoseo"
        private lateinit var mApplicationContext: Context
        private lateinit var mSharedPreference: SharedPreferences

        private const val prefPermission                = "prefPermission"
        private const val prefShowPermission            = "prefShowPermission"

        fun init(context: Context) {
            mApplicationContext = context
            createSecretPreference()
        }
        //암호화
        private fun createSecretPreference() {
            try {
                val masterKey = MasterKey.Builder(mApplicationContext).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
                mSharedPreference = EncryptedSharedPreferences.create(mApplicationContext,
                        name,
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
            } catch (e: java.lang.NullPointerException) {
                CommonUtils.commonErrLog(TAG + " " + e.localizedMessage)
            }
        }

        private fun getPreferences(): SharedPreferences {
            return mSharedPreference
        }

        fun clearInfo(){
            //putPrefPlace("")
        }

        fun setPrefPermission(isUsed:Boolean){ putBoolean(prefPermission,isUsed) }
        fun getPrefPermission(): Boolean { return getBoolean(prefPermission, false) }

        fun setShowPermissionDialog(isUsed:Boolean){ putBoolean(prefShowPermission,isUsed) }
        fun getShowPermissionDialog(): Boolean { return getBoolean(prefShowPermission, false) }

        //init data
        private fun putString(key: String, value: String) {
            val pref: SharedPreferences = getPreferences()
            val editor = pref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        private fun putBoolean(key: String, value: Boolean) {
            val pref: SharedPreferences = getPreferences()
            val editor = pref.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        private fun putInt(key: String, value: Int) {
            val pref: SharedPreferences = getPreferences()
            val editor = pref.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        private fun getInt(key: String): Int {
            val pref: SharedPreferences = getPreferences()
            return pref.getInt(key, -1)
        }

        private fun putLong(key: String, value: Long) {
            val pref: SharedPreferences = getPreferences()
            val editor = pref.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        private fun getLong(key: String, defaultVal: Long): Long {
            val pref: SharedPreferences = getPreferences()
            return pref.getLong(key, defaultVal)
        }

        private fun getInt(key: String, defaultVal: Int): Int {
            val pref: SharedPreferences = getPreferences()
            return pref.getInt(key, defaultVal)
        }

        private fun getString(key: String): String? {
            val pref: SharedPreferences = getPreferences()
            return pref.getString(key, "")
        }

        private fun getString(key: String, defaultVal: String): String {
            val pref: SharedPreferences = getPreferences()
            return pref.getString(key, defaultVal).toString()
        }

        private fun getBoolean(key: String): Boolean {
            val pref: SharedPreferences = getPreferences()
            return pref.getBoolean(key, false)
        }

        private fun getBoolean(key: String, isDefault: Boolean): Boolean {
            val pref: SharedPreferences = getPreferences()
            return pref.getBoolean(key, isDefault)
        }

    }
}