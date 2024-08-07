package kr.ac.beni.beniprj.ui.run

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.SendParamInfo
import kr.ac.beni.beniprj.UserInfo
import kr.ac.beni.beniprj.databinding.UseModeFragmentBinding
import kr.ac.beni.beniprj.model.Chat
import kr.ac.beni.beniprj.model.GetMedicalHistResult
import kr.ac.beni.beniprj.model.NewChatResult
import kr.ac.beni.beniprj.model.NewUserInfoResult
import kr.ac.beni.beniprj.model.request.Requests
import kr.ac.beni.beniprj.retrofit.ApiResponse
import kr.ac.beni.beniprj.ui.adapter.ChatListAdapter
import kr.ac.beni.beniprj.ui.adapter.MedicalHistdapter
import kr.ac.beni.beniprj.ui.custom.BaseDialog
import kr.ac.beni.beniprj.ui.custom.CustomYesOrNoDialog
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.EventObserver
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFViewModel
import kr.ac.beni.beniprj.model.MedicalHistInfo
import kr.ac.beni.beniprj.model.MedicalHistItem
import kr.ac.beni.beniprj.viewmodel.VFParamModel
import javax.annotation.Nonnull
import javax.annotation.Nullable

class UseModeFragment : Fragment(){
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding: UseModeFragmentBinding
    private lateinit var mAdapter: MedicalHistdapter
    private val binding get() = _binding
    private lateinit var sharedViewModel: VFParamModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = UseModeFragmentBinding.inflate(inflater,container,false)
        sharedViewModel = ViewModelProvider(requireActivity())[VFParamModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
        initObserve()
        mAdapter.addItem(
            MedicalHistInfo(
                medicalMainName = "피부 미용",
                medicalMainCode = "M01",
                medicalSubName = "얼굴",
                medicalSubCode = "S01",
                medicalDate = "2024.07.18",
                operationDuration = "60",
                operationHz = "100",
                operationRate = "60",
                id=1
            )
        )
    }
    private fun setUIEvent(){
        CommonUtils.commonLog("use mode fragment setUIEvent..")
        sharedViewModel.getUseModeType().observe(viewLifecycleOwner, Observer {
            CommonUtils.commonLog("UseModeFragment useModeType : ${it.toString()}")
        })
        _binding.btnUseModeSkin.onThrottleClick {
            sharedViewModel.updateUseModeType("M01")
            findNavController().navigate(R.id.navigation_mode_run)
        }
    }
    private fun initObserve(){
//        getMedicalListObserve()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getMedicalListObserve(){
        mVFViewModel.getGetMedicalHist().observe(viewLifecycleOwner, EventObserver{
            if(it.isSuccess()) {
                val getMedicalHistResult: GetMedicalHistResult? = it.getResponse()?.body()
//                val gson = Gson()
//                val userListType = object : TypeToken<List<MedicalHistInfo>>() {}.type
//                val users: List<MedicalHistInfo> = gson.fromJson(getMedicalHistResult.medicalHistList, userListType)
//
//                // 결과 출력
//                users.forEach { user ->
//                    CommonUtils.commonLog("User ID: ${user.id}, Name: ${user.medicalMainName}")
//                }
                val medicalHistList: List<MedicalHistItem>? = getMedicalHistResult?.medicalHistList
                medicalHistList?.forEach { item ->
                    CommonUtils.commonLog("User ID: ${item.id}, Name: ${item.medicalMainName}")
                    mAdapter.addItem(
                        MedicalHistInfo(
                            id=item.id,
                            medicalMainName = item.medicalMainName,
                            medicalMainCode = item.medicalMainCode,
                            medicalSubName = item.medicalSubName,
                            medicalSubCode = item.medicalSubCode,
                            medicalDate = item.medicalDate,
                            operationDuration = item.operationDuration,
                            operationHz = item.operationHz,
                            operationRate = item.operationRate
                        )
                    )
                }
                mAdapter.notifyDataSetChanged()
                binding.rvUserMode.scrollToPosition(mAdapter.itemCount - 1)
            } else {
                observeResponseError(it, Const.Api.GetMedicalList)
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

    private fun initView() {
        CommonUtils.commonLog("use mode fragment InitView..")
        _binding.rvUserMode.layoutManager = LinearLayoutManager(context)
        mAdapter = MedicalHistdapter(object : MedicalHistdapter.OnItemClickListener{
            override fun onClick(data: MedicalHistInfo) {
                //
            }
        })
        binding.rvUserMode.adapter = mAdapter

        mVFViewModel.callGetMedicalHist(
            Requests.GetMedicalHist(
                fkUserId = 1,
            )
        )
//        for(i in 1..10) {
//            mAdapter.addItem(
//                MedicalHistInfo(
//                    medicalMainName = "피부 미용",
//                    medicalMainCode = "M01",
//                    medicalSubName = "얼굴",
//                    medicalSubCode = "S01",
//                    medicalDate = "2024.07.18",
//                    operationDuration = "60",
//                    operationHz = "100",
//                    operationRage = 60
//                )
//            )
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