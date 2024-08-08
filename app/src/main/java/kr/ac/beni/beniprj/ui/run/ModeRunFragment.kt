package kr.ac.beni.beniprj.ui.run
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.ModeRunFragmentBinding
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.CommonUtils.Companion.bundleToMap
import kr.ac.beni.beniprj.util.CommonUtils.Companion.commonLog
import kr.ac.beni.beniprj.util.CommonUtils.Companion.mapToBundle
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFParamModel
import kr.ac.beni.beniprj.viewmodel.VFViewModel

class ModeRunFragment : Fragment(){
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding: ModeRunFragmentBinding
    private val binding get() = _binding
    private val sharedViewModel: VFParamModel by activityViewModels()
    private var _dataMapInfo: MutableMap<String,Any> = mutableMapOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = ModeRunFragmentBinding.inflate(inflater,container,false)
        arguments?.let { bundleToMap(_dataMapInfo, it) }
        val userID = _dataMapInfo[Const.MapKeyString.UserID.toString()]
        val useModeType = _dataMapInfo[Const.MapKeyString.UserModeType.toString()]
        CommonUtils.commonLog("userId : $userID")
        CommonUtils.commonLog("useModeType : $useModeType")
        //CommonUtils.logMap(_dataMapInfo,"mode run fragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
    }
    private fun setUIEvent(){
        CommonUtils.commonLog("mode run fragment setUIEvent..")
        _binding.btnModeRunFace.onThrottleClick {
            CommonUtils.commonLog("mode run skin click.")
            updateMode(Const.RunModeType.FACE)
        }
        _binding.btnModeRunNeck.onThrottleClick {
            CommonUtils.commonLog("mode run nec click.")
            updateMode(Const.RunModeType.NECK)
        }
        _binding.btnModeRunStomach.onThrottleClick {
            CommonUtils.commonLog("mode run Stomach click.")
            updateMode(Const.RunModeType.STOMACH)
        }
        _binding.btnModeRunCellulite.onThrottleClick {
            CommonUtils.commonLog("mode run Cellulite click.")
            updateMode(Const.RunModeType.CELLULITE)
        }
        _binding.btnModeRunSkinStart.onThrottleClick {
            val bundle = mapToBundle(_dataMapInfo)
            findNavController().navigate(R.id.navigation_running,bundle)
        }
    }

    private fun updateMode(mode: Const.RunModeType){
        when(mode){
            Const.RunModeType.FACE->{
                toggleMode(_binding.btnModeRunFace,true)
                toggleMode(_binding.btnModeRunNeck,false)
                toggleMode(_binding.btnModeRunStomach,false)
                toggleMode(_binding.btnModeRunCellulite,false)
            }
            Const.RunModeType.NECK->{
                toggleMode(_binding.btnModeRunFace,false)
                toggleMode(_binding.btnModeRunNeck,true)
                toggleMode(_binding.btnModeRunStomach,false)
                toggleMode(_binding.btnModeRunCellulite,false)
            }
            Const.RunModeType.STOMACH ->{
                toggleMode(_binding.btnModeRunFace,false)
                toggleMode(_binding.btnModeRunNeck,false)
                toggleMode(_binding.btnModeRunStomach,true)
                toggleMode(_binding.btnModeRunCellulite,false)
            }
            Const.RunModeType.CELLULITE ->{
                toggleMode(_binding.btnModeRunFace,false)
                toggleMode(_binding.btnModeRunNeck,false)
                toggleMode(_binding.btnModeRunStomach,false)
                toggleMode(_binding.btnModeRunCellulite,true)
            }
        }
        _dataMapInfo[Const.MapKeyString.RunModeType.toString()] = mode.value
        commonLog("click run type : ${_dataMapInfo[Const.MapKeyString.RunModeType.toString()]}")
        //sharedViewModel.updateRunModeType(mode)

    }

    private fun toggleMode(id:View,onOff:Boolean){
        when (id) {
            _binding.btnModeRunFace -> {
                _binding.btnModeRunFace.setBackgroundResource(if(onOff) R.drawable.mode_run_skin_drawable_shape_2 else R.drawable.mode_run_skin_drawable_shape_3)
            }
            _binding.btnModeRunNeck -> {
                _binding.btnModeRunNeck.setBackgroundResource(if(onOff) R.drawable.mode_run_skin_drawable_shape_2 else R.drawable.mode_run_skin_drawable_shape_3)
            }
            _binding.btnModeRunStomach -> {
                _binding.btnModeRunStomach.setBackgroundResource(if(onOff) R.drawable.mode_run_skin_drawable_shape_2 else R.drawable.mode_run_skin_drawable_shape_3)
            }
            _binding.btnModeRunCellulite -> {
                _binding.btnModeRunCellulite.setBackgroundResource(if(onOff) R.drawable.mode_run_skin_drawable_shape_2 else R.drawable.mode_run_skin_drawable_shape_3)
            }
        }

    }
    private fun initView() {
        CommonUtils.commonLog("mode run fragment InitView..")
//        sharedViewModel.getUseModeType().observe(viewLifecycleOwner){
//            CommonUtils.commonLog("mode run fragment useModeType : ${it.toString()}")
//        }
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}