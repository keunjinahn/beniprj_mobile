package kr.ac.beni.beniprj.ui

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.ActivityJoinBinding
import kr.ac.beni.beniprj.service.BackgroundService
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFViewModel
import java.net.NetworkInterface


class JoinActivity : AppCompatActivity() {

    private lateinit var mVFViewModel: VFViewModel
    private lateinit var mBinding: ActivityJoinBinding

    private lateinit var nfcAdapter: NfcAdapter

    private var mAudioManager: AudioManager? = null

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, JoinActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            //intent.putExtra(Const.EXTRA_APPLICATION, applications)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        stopService(Intent(this@JoinActivity,BackgroundService::class.java))
        initView()
        initObserve()

        setupControls()
    }

    private fun initView(){
        setUIEvent()

        printIpAddressList()
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    private fun setUIEvent() {

        mBinding.textOk.onThrottleClick {
            moveRunActivity()
        }
    }

    private fun initObserve(){

    }

    private fun moveRunActivity(){
        RunActivity.startActivity(this@JoinActivity)
        finish()
    }

    private fun setupControls() {
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        mAudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override fun onResume() {
        super.onResume()
        /*
        nfcAdapter.enableReaderMode(
            this,
            {
                // DO Nothing.
            },
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
            null
        )

         */
    }

    override fun onPause() {
        super.onPause()
        //nfcAdapter.disableReaderMode(this)
    }

    private fun printIpAddressList() {

        var ipListText = ""
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()

                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()

                    val hostAddress = inetAddress.hostAddress
                    Log.d("VFAPPLOG", "[printIpAddressList] IP : [$hostAddress]")

                    if (hostAddress != null && hostAddress.contains(".")) {
                        ipListText += "$hostAddress\n"
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("VFAPPLOG", "[printIpAddressList] exception : $e")
            ipListText = "Exception"
        }

        mBinding.textIplist.text = ipListText
    }
}

