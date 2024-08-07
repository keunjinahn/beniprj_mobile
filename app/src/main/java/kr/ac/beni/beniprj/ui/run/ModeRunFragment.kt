package kr.ac.beni.beniprj.ui.run
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.beni.beniprj.databinding.ModeRunFragmentBinding
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.EventObserver
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFParamModel
import kr.ac.beni.beniprj.viewmodel.VFViewModel

class ModeRunFragment : Fragment(){
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding: ModeRunFragmentBinding
    private val binding get() = _binding
    private lateinit var sharedViewModel: VFParamModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = ModeRunFragmentBinding.inflate(inflater,container,false)
        sharedViewModel = ViewModelProvider(requireActivity())[VFParamModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
    }
    private fun setUIEvent(){
        CommonUtils.commonLog("mode run fragment setUIEvent..")
    }

    private fun initView() {
        CommonUtils.commonLog("mode run fragment InitView..")
        sharedViewModel.getUseModeType().observe(viewLifecycleOwner, Observer {
            CommonUtils.commonLog("mode run fragment useModeType : ${it.toString()}")
        })
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}