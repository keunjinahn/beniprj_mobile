package kr.ac.beni.beniprj.ui.run
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.databinding.RunningFragmentBinding
import kr.ac.beni.beniprj.model.GetMedicalHistResult
import kr.ac.beni.beniprj.model.MedicalHistInfo
import kr.ac.beni.beniprj.model.MedicalHistItem
import kr.ac.beni.beniprj.model.SetMedicalHistResult
import kr.ac.beni.beniprj.model.request.Requests
import kr.ac.beni.beniprj.retrofit.ApiResponse
import kr.ac.beni.beniprj.ui.custom.BaseDialog
import kr.ac.beni.beniprj.ui.custom.CustomYesOrNoDialog
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.CommonUtils.Companion.bundleToMap
import kr.ac.beni.beniprj.util.CommonUtils.Companion.getCurrentTimeAsString
import kr.ac.beni.beniprj.util.EventObserver
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFParamModel
import kr.ac.beni.beniprj.viewmodel.VFViewModel

class RunningFragment : Fragment(){
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding: RunningFragmentBinding
    private val binding get() = _binding
    private val sharedViewModel: VFParamModel by activityViewModels()
    private var _dataMapInfo: MutableMap<String,Any> = mutableMapOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = RunningFragmentBinding.inflate(inflater,container,false)
        arguments?.let { bundleToMap(_dataMapInfo, it) }
        val runMode = _dataMapInfo[Const.MapKeyString.RunModeType.toString()]
        CommonUtils.commonLog("runModeType : $runMode")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
        initObserve()
    }
    private fun setUIEvent(){
        CommonUtils.commonLog("running fragment setUIEvent..")
    }

    private fun initView() {
        CommonUtils.commonLog("running fragment InitView..")
        mVFViewModel.callSetMedicalHist(
            Requests.SetMedicalHist(
                fkUserId = _dataMapInfo[Const.MapKeyString.UserID.toString()],
                medicalMainName = "피부",
                medicalMainCode = _dataMapInfo[Const.MapKeyString.UserModeType.toString()].toString(),
                medicalSubName = "얼굴",
                medicalSubCode = _dataMapInfo[Const.MapKeyString.RunModeType.toString()].toString(),
                operationDuration = "90",
                operationHz = "20",
                operationRate = "80",
                medicalDate = getCurrentTimeAsString()
            )
        )
     }
    private fun initObserve(){
        setMedicalInfoObserve()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMedicalInfoObserve(){
        mVFViewModel.getSetMedicalHist().observe(viewLifecycleOwner, EventObserver{
            if(it.isSuccess()) {
                val setMedicalHistResult: SetMedicalHistResult? = it.getResponse()?.body()
                CommonUtils.commonLog("setMedicalHistResult : ${setMedicalHistResult.toString()}")
            } else {
                observeResponseError(it, Const.Api.SetMedicalInfo)
            }
        })
    }
    private fun <T> observeResponseError(it: ApiResponse<T>, name: Const.Api) {
        val errorBody = it.getErrorBody()
        if(errorBody != null){

            if(errorBody.message == null) networkFailRefresh("",name)
            else networkFailRefresh(errorBody.message.toString(),name)

        }else{
            if(it.hasException) CommonUtils.commonErrLog("[observe] " + name + " HTTP FAILED => " + it.exceptionMessage)
            networkFailRefresh("",name)
        }
    }

    private fun networkFailRefresh(errMsg:String, name : Const.Api){
        val msg = errMsg.ifEmpty {
            "네트워크 문제가 발생했습니다. 다시 시도해주세요"
        }
        CustomYesOrNoDialog(requireActivity(),"문제 발생", msg,"닫기","재시도", object: CustomYesOrNoDialog.OnItemYesOrNoClickListener{
            override fun onItemClick(flag: Int) {
                if(flag == BaseDialog.CONFIRM){
                    when (name) {
                        Const.Api.SetNewUserInfo -> {
                        }
                        else -> {
                            Toast.makeText(requireActivity(), "호출 타입이 존재하지 않습니다.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    when (name) {
                        Const.Api.SetNewUserInfo -> {

                        }
                        else -> {
                            //
                        }
                    }
                }
            }
        }).show()
    }
    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}