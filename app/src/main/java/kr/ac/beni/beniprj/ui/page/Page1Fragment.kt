package kr.ac.beni.beniprj.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.ac.beni.beniprj.databinding.Page1FragmentBinding
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFViewModel

class Page1Fragment : Fragment() {
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding:Page1FragmentBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = Page1FragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    private fun setUIEvent() {
        CommonUtils.commonLog("page 1  setUIEvent..")
        _binding.textOk.onThrottleClick {
            CommonUtils.commonLog("page 1 click event..")
        }
    }

    private fun initView() {
        CommonUtils.commonLog("page 1 framgment InitView..")
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}