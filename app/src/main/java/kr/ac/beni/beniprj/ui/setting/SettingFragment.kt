package kr.ac.beni.beniprj.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.ac.beni.beniprj.BuildConfig
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.FragmentSettingBinding
import kr.ac.beni.beniprj.ui.custom.OnBackPressListener
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.util.PreferenceUtil
import kr.ac.beni.beniprj.viewmodel.VFViewModel


class SettingFragment : Fragment(), OnBackPressListener {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var mVFViewModel: VFViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
        initObserve()
    }

    private fun initView(){
        binding.settingVersion.text = String.format(getString(R.string.intro_version), BuildConfig.VERSION_NAME)
        binding.settingSwitchPermission.isChecked = PreferenceUtil.getPrefPermission()
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    private fun setUIEvent(){
        binding.settingSwitchAlarm.setOnCheckedChangeListener { _, p1 ->

        }
        binding.settingPrivacy.onThrottleClick {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Const.PRIVACY_URL))
            startActivity(browserIntent)
        }
        binding.settingSwitchPermission.setOnCheckedChangeListener{ _, p1 ->

        }
    }

    private fun initObserve(){

    }

    override fun onBackPress(): Boolean {
        return false
    }
}