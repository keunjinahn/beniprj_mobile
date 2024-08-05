package kr.ac.beni.beniprj.ui

import android.Manifest
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kr.ac.beni.beniprj.BuildConfig
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.ActivityIntroBinding
import kr.ac.beni.beniprj.ui.custom.BaseDialog
import kr.ac.beni.beniprj.ui.custom.PermissionDialog
import kr.ac.beni.beniprj.ui.custom.PrivacyDialog
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.PreferenceUtil
import kr.ac.beni.beniprj.viewmodel.VFViewModel


class IntroActivity : AppCompatActivity() {

    private lateinit var mVFViewModel: VFViewModel
    private lateinit var mRequirePermissions: Array<String>
    private lateinit var mBinding: ActivityIntroBinding

    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        initView()
        initObserve()

        setupControls()

        Handler(Looper.getMainLooper()).postDelayed({
            if(Const.DEBUG_MODE){
                checkPermission()
            }else{
                if(checkSecurity()) checkPermission()
            }
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
//        mVFViewModel.getSetApplication().removeObservers(this@IntroActivity)
//        mVFViewModel.getUpdate().removeObservers(this@IntroActivity)
//        mVFViewModel.getApplicationsFromToken().removeObservers(this@IntroActivity)
//        mVFViewModel.getGetWorkLastLiveData().removeObservers(this@IntroActivity)
    }

    private fun initView(){
        mBinding.progress.visibility = View.VISIBLE
        mBinding.version.text = String.format(getString(R.string.intro_version),BuildConfig.VERSION_NAME)
    }

    private fun initObserve() {
        mVFViewModel = ViewModelProvider(this@IntroActivity)[VFViewModel::class.java]
    }

    //보안
    private fun checkSecurity(): Boolean{
        if(!Const.TEST_MODE) {
            if(CommonUtils.isRooting()){
                Toast.makeText(this,getString(R.string.toast_dont_rooting),Toast.LENGTH_SHORT).show()
                finish()
                return false
            }
            if(!CommonUtils.installedByStore(this)){
                Toast.makeText(this,getString(R.string.toast_market_check),Toast.LENGTH_SHORT).show()
//                MarketConfirmDialog(this,object : BaseDialog.OnItemClickListener{
//                    override fun onItemClick(position: Int) {
//                        finish()
//                    }
//                }).show()
                return false
            }
            if(CommonUtils.isDevMode(this)){
//                DevModeDialogActivity.startActivity(this@IntroActivity,false)
                finish()
                return false
            }
        }
        return true
    }

    //권한이 제일 마지막
    private fun checkPermission(){
        mRequirePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO)
        }else{
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO)
        }

        if(hasPermission(mRequirePermissions)) {
            moveJoinActivity()
        }else{
            if(PreferenceUtil.getShowPermissionDialog()){
                dialogTedPermission()
            }else{
                permissionDialog()
            }
        }

//        moveJoinActivity()
    }

    private fun permissionDialog(){
        PermissionDialog(this@IntroActivity, object : PermissionDialog.OnItemClickListener{
            override fun onClick(flag: Int, isChecked:Boolean) {
                if(BaseDialog.CONFIRM == flag){
                    PreferenceUtil.setShowPermissionDialog(true)
                    PreferenceUtil.setPrefPermission(isChecked)
                    dialogTedPermission()


                }else if(BaseDialog.CANCEL == flag){
                    PreferenceUtil.setShowPermissionDialog(false)
                    finish()
                }
            }
        }).show()
    }

    private fun dialogTedPermission(){
        TedPermission.create().setPermissionListener(permissionListener)
            .setPermissions(*mRequirePermissions)
            .setDeniedMessage(R.string.permission_denied)
            .check()
    }

    private val permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            moveJoinActivity()
        }
        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(this@IntroActivity,getString(R.string.permission_denied),Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun hasPermission(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    //개인정보처리방침
    private fun checkPrivacy(){
        PrivacyDialog(this,object : PrivacyDialog.OnItemClickListener{
            override fun onClick(flag: Int, isChecked:Boolean) {
                if(BaseDialog.CONFIRM == flag){
                    //callSetApplication()
                }else{
                    finish()
                }
            }
        }).show()
    }

    private fun moveJoinActivity(){
        JoinActivity.startActivity(this@IntroActivity)
        finish()
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
        //nfcAdapter.disableReaderMode(this)
    }
}