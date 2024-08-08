package kr.ac.beni.beniprj.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.ActivityRunBinding
import kr.ac.beni.beniprj.service.BackgroundService
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.util.PreferenceUtil
import kr.ac.beni.beniprj.viewmodel.VFParamModel
import kr.ac.beni.beniprj.viewmodel.VFViewModel


class RunActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRunBinding
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var navView: BottomNavigationView
    private val sharedViewModel: VFParamModel by viewModels()
    companion object{
        fun startActivity(context: Context/*, applications: Applications*/) {
            val intent = Intent(context, RunActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        setContentView(binding.root)
        initView()
        setUIEvent()
        setupControls()
        sharedViewModel.updateUseModeType(Const.UserModeType.SKIN)

    }

    fun initView(){
        CommonUtils.commonLog("Run activate InitView..")
        navView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_run) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_running,R.id.navigation_mode_run,R.id.navigation_use_mode))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
        navView.selectedItemId = R.id.navigation_use_mode


        val intent = Intent(this, BackgroundService::class.java)
        startService(intent)
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    private fun setUIEvent() {

    }

    fun setRunFragmentWorkEnd(){
//        navView.selectedItemId = R.id.navigation_mode_run
    }

    override fun onDestroy() {
        stopService(Intent(this@RunActivity,BackgroundService::class.java))
        super.onDestroy()
        CommonUtils.commonLog("Run onDestroy")
    }

    private fun setupControls() {
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this)
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
        CommonUtils.commonLog("Run onPause")
        //nfcAdapter.disableReaderMode(this)
    }

    override fun onBackPressed() {
        return
    }
}