package kr.ac.beni.beniprj.ui.users

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.UserInfo
import kr.ac.beni.beniprj.databinding.SignupFragmentBinding
import kr.ac.beni.beniprj.model.NewChatResult
import kr.ac.beni.beniprj.model.NewUserInfoResult
import kr.ac.beni.beniprj.model.request.Requests
import kr.ac.beni.beniprj.retrofit.ApiResponse
import kr.ac.beni.beniprj.ui.custom.BaseDialog
import kr.ac.beni.beniprj.ui.custom.CustomYesOrNoDialog
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.EventObserver
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFViewModel

class SignupFragment : Fragment(){
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding:SignupFragmentBinding
    private val binding get() = _binding
    private val userInfo = UserInfo("안근진",52,2,3,2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = SignupFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
        initObserve()
    }
    private fun initObserve(){
        setNewUserInfoObserve()
    }
    private fun setNewUserInfoObserve(){
        mVFViewModel.getSetNewUserInfo().observe(viewLifecycleOwner, EventObserver{
            if(it.isSuccess()) {
                val newUserInfoResult: NewUserInfoResult? = it.getResponse()?.body()
                CommonUtils.commonLog("결과 : ${newUserInfoResult.toString()}")
            } else {
                observeResponseError(it, Const.Api.SetNewUserInfo)
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
        CommonUtils.commonLog("signup fragment InitView..")
        _binding.editSignupName.setText(userInfo.name)
        _binding.editSignupAge.setText(userInfo.age.toString())
        updateSexUi(userInfo.sex)
        updateDiseaseUi(userInfo.diseaseNum)
        updateDiseaseLevelUi(userInfo.diseaseLevel)
    }

    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    private fun updateSexUi(sex:Int){
        when(sex){
            1->{
                toggleSignupSex(_binding.btnSignupSexMan,true)
                toggleSignupSex(_binding.textSignupSexMan,true)
                toggleSignupSex(_binding.btnSignupSexWoman,false)
                toggleSignupSex(_binding.textSignupSexWoman,false)
            }
            2->{
                toggleSignupSex(_binding.btnSignupSexMan,false)
                toggleSignupSex(_binding.textSignupSexMan,false)
                toggleSignupSex(_binding.btnSignupSexWoman,true)
                toggleSignupSex(_binding.textSignupSexWoman,true)
            }
        }

    }

    private fun updateDiseaseUi(diseaseNum:Int){
        when(diseaseNum){
            1->{
                toggleSignupDisease(_binding.btnSignupDisease1,true)
                toggleSignupDisease(_binding.btnSignupDisease2,false)
                toggleSignupDisease(_binding.btnSignupDisease3,false)
                toggleSignupDisease(_binding.btnSignupDisease4,false)
                toggleSignupDisease(_binding.textSignupDisease1,true)
                toggleSignupDisease(_binding.textSignupDisease2,false)
                toggleSignupDisease(_binding.textSignupDisease3,false)
                toggleSignupDisease(_binding.textSignupDisease4,false)
            }
            2->{
                CommonUtils.commonLog("btnSignupDisease2 click event..")
                toggleSignupDisease(_binding.btnSignupDisease1,false)
                toggleSignupDisease(_binding.btnSignupDisease2,true)
                toggleSignupDisease(_binding.btnSignupDisease3,false)
                toggleSignupDisease(_binding.btnSignupDisease4,false)
                toggleSignupDisease(_binding.textSignupDisease1,false)
                toggleSignupDisease(_binding.textSignupDisease2,true)
                toggleSignupDisease(_binding.textSignupDisease3,false)
                toggleSignupDisease(_binding.textSignupDisease4,false)
            }
            3->{
                toggleSignupDisease(_binding.btnSignupDisease1,false)
                toggleSignupDisease(_binding.btnSignupDisease2,false)
                toggleSignupDisease(_binding.btnSignupDisease3,true)
                toggleSignupDisease(_binding.btnSignupDisease4,false)
                toggleSignupDisease(_binding.textSignupDisease1,false)
                toggleSignupDisease(_binding.textSignupDisease2,false)
                toggleSignupDisease(_binding.textSignupDisease3,true)
                toggleSignupDisease(_binding.textSignupDisease4,false)
            }
            4->{
                toggleSignupDisease(_binding.btnSignupDisease1,false)
                toggleSignupDisease(_binding.btnSignupDisease2,false)
                toggleSignupDisease(_binding.btnSignupDisease3,false)
                toggleSignupDisease(_binding.btnSignupDisease4,true)
                toggleSignupDisease(_binding.textSignupDisease1,false)
                toggleSignupDisease(_binding.textSignupDisease2,false)
                toggleSignupDisease(_binding.textSignupDisease3,false)
                toggleSignupDisease(_binding.textSignupDisease4,true)
            }
        }

    }

    private fun updateDiseaseLevelUi(diseaseLevel:Int){
        when(diseaseLevel){
            1->{
                toggleSignupLevel(_binding.btnLevel1,true)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            2->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,true)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            3->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,true)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            4->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,true)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            5->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,true)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            6->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,true)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            7->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,true)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            8->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,true)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            9->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,true)
                toggleSignupLevel(_binding.btnLevel10,false)
            }
            10->{
                toggleSignupLevel(_binding.btnLevel1,false)
                toggleSignupLevel(_binding.btnLevel2,false)
                toggleSignupLevel(_binding.btnLevel3,false)
                toggleSignupLevel(_binding.btnLevel4,false)
                toggleSignupLevel(_binding.btnLevel5,false)
                toggleSignupLevel(_binding.btnLevel6,false)
                toggleSignupLevel(_binding.btnLevel7,false)
                toggleSignupLevel(_binding.btnLevel8,false)
                toggleSignupLevel(_binding.btnLevel9,false)
                toggleSignupLevel(_binding.btnLevel10,true)
            }
        }
    }
    private fun setUIEvent() {
        CommonUtils.commonLog("setUIEvent..")

        _binding.btnSignupCreateAccount.onThrottleClick {
            CommonUtils.run {
                commonLog("btnSignupCreateAccount click..")
                commonLog("user info name : ${userInfo.name} ,sex : ${userInfo.sex},age : ${userInfo.age},disease num : ${userInfo.diseaseNum},disease level : ${userInfo.diseaseLevel}")

                if (_binding.editSignupName.text.toString().isEmpty()) {
                    Toast.makeText(context, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    return@onThrottleClick
                }

                if (_binding.editSignupAge.text.toString().isEmpty()) {
                    Toast.makeText(context, "나이를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    return@onThrottleClick
                }

                mVFViewModel.callSetNewUserInfo(
                    Requests.SetNewUserInfo(
                        name = userInfo.name,
                        age = userInfo.age,
                        sex = userInfo.sex,
                        diseaseNum = userInfo.diseaseNum,
                        diseaseLevel = userInfo.diseaseLevel
                    )
                )

            }
        }

        _binding.editSignupName.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(name: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userInfo.name=name.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        _binding.editSignupAge.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()
                if (input.isNotEmpty() && input.all { it.isDigit() }) {
                    userInfo.age = input.toInt()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        _binding.btnSignupSexMan.onThrottleClick {
            CommonUtils.commonLog("btn_signup_sex_man click event..")
            updateSexUi(1)
            userInfo.sex = 1
        }
        _binding.btnSignupSexWoman.onThrottleClick {
            CommonUtils.commonLog("Woman click event..")
            updateSexUi(2)
            userInfo.sex = 2
        }

        _binding.btnSignupDisease1.onThrottleClick{
            CommonUtils.commonLog("btnSignupDisease1 click event..")
            updateDiseaseUi(1)
            userInfo.diseaseNum = 1
        }
        _binding.btnSignupDisease2.onThrottleClick{
            CommonUtils.commonLog("btnSignupDisease2 click event..")
            updateDiseaseUi(2)
            userInfo.diseaseNum = 2
        }
        _binding.btnSignupDisease3.onThrottleClick{
            CommonUtils.commonLog("btnSignupDisease3 click event..")
            updateDiseaseUi(3)
            userInfo.diseaseNum = 3
        }
        _binding.btnSignupDisease4.onThrottleClick{
            CommonUtils.commonLog("btnSignupDisease4 click event..")
            updateDiseaseUi(4)
            userInfo.diseaseNum = 4
        }
        /////////////level///////////
        _binding.btnLevel1.onThrottleClick {
            CommonUtils.commonLog("btnLevel1 click event..")
            updateDiseaseLevelUi(1)
            userInfo.diseaseLevel= 1
        }

        _binding.btnLevel2.onThrottleClick {
            CommonUtils.commonLog("btnLevel2 click event..")
            updateDiseaseLevelUi(2)
            userInfo.diseaseLevel= 2
        }

        _binding.btnLevel3.onThrottleClick {
            CommonUtils.commonLog("btnLevel3 click event..")
            updateDiseaseLevelUi(3)
            userInfo.diseaseLevel= 3
        }

        _binding.btnLevel4.onThrottleClick {
            CommonUtils.commonLog("btnLevel4 click event..")
            updateDiseaseLevelUi(4)
            userInfo.diseaseLevel= 4

        }

        _binding.btnLevel5.onThrottleClick {
            CommonUtils.commonLog("btnLevel5 click event..")
            updateDiseaseLevelUi(5)
            userInfo.diseaseLevel= 5
        }

        _binding.btnLevel6.onThrottleClick {
            CommonUtils.commonLog("btnLevel6 click event..")
            updateDiseaseLevelUi(6)
            userInfo.diseaseLevel= 6
        }

        _binding.btnLevel7.onThrottleClick {
            CommonUtils.commonLog("btnLevel7 click event..")
            updateDiseaseLevelUi(7)
            userInfo.diseaseLevel= 7
        }

        _binding.btnLevel8.onThrottleClick {
            CommonUtils.commonLog("btnLevel8 click event..")
            updateDiseaseLevelUi(8)
            userInfo.diseaseLevel= 8
        }

        _binding.btnLevel9.onThrottleClick {
            CommonUtils.commonLog("btnLevel9 click event..")
            updateDiseaseLevelUi(9)
            userInfo.diseaseLevel= 9
        }
        _binding.btnLevel10.onThrottleClick {
            CommonUtils.commonLog("btnLevel10 click event..")
            updateDiseaseLevelUi(10)
            userInfo.diseaseLevel= 10
        }


    }
    private fun toggleSignupSex(id:View,onOff:Boolean){
        when (id) {
            _binding.btnSignupSexMan ->  _binding.btnSignupSexMan.setBackgroundResource(if(onOff) R.drawable.signup_drawable_shape_8 else R.drawable.signup_drawable_shape_9)
            _binding.btnSignupSexWoman ->  _binding.btnSignupSexWoman.setBackgroundResource(if(onOff) R.drawable.signup_drawable_shape_8 else R.drawable.signup_drawable_shape_9)
            _binding.textSignupSexMan ->  _binding.textSignupSexMan.setTextColor(resources.getColor(if(onOff) R.color.signup_toggle_true_color else R.color.signup_toggle_false_color))
            _binding.textSignupSexWoman->  _binding.textSignupSexWoman.setTextColor(resources.getColor(if(onOff) R.color.signup_toggle_true_color else R.color.signup_toggle_false_color))
        }

    }
    private fun toggleSignupDisease(id:View,onOff:Boolean){
        when (id) {
            _binding.btnSignupDisease1 ->  _binding.btnSignupDisease1.setBackgroundResource(if(onOff) R.drawable.signup_drawable_shape_13 else R.drawable.signup_drawable_shape_14)
            _binding.btnSignupDisease2 ->  _binding.btnSignupDisease2.setBackgroundResource(if(onOff) R.drawable.signup_drawable_shape_13 else R.drawable.signup_drawable_shape_14)
            _binding.btnSignupDisease3 ->  _binding.btnSignupDisease3.setBackgroundResource(if(onOff) R.drawable.signup_drawable_shape_13 else R.drawable.signup_drawable_shape_14)
            _binding.btnSignupDisease4 ->  _binding.btnSignupDisease4.setBackgroundResource(if(onOff) R.drawable.signup_drawable_shape_13 else R.drawable.signup_drawable_shape_14)
            _binding.textSignupDisease1 ->  _binding.textSignupDisease1.setTextColor(resources.getColor(if(onOff) R.color.signup_toggle_true_color else R.color.signup_toggle_false_color))
            _binding.textSignupDisease2 ->  _binding.textSignupDisease2.setTextColor(resources.getColor(if(onOff) R.color.signup_toggle_true_color else R.color.signup_toggle_false_color))
            _binding.textSignupDisease3 ->  _binding.textSignupDisease3.setTextColor(resources.getColor(if(onOff) R.color.signup_toggle_true_color else R.color.signup_toggle_false_color))
            _binding.textSignupDisease4 ->  _binding.textSignupDisease4.setTextColor(resources.getColor(if(onOff) R.color.signup_toggle_true_color else R.color.signup_toggle_false_color))
        }

    }

    private fun toggleSignupLevel(id:View,onOff:Boolean){
        when (id) {
            _binding.btnLevel1 ->  _binding.btnLevel1.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel1 ->  _binding.textLevel1.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel2 ->  _binding.btnLevel2.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel2 ->  _binding.textLevel2.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel3 ->  _binding.btnLevel3.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel3 ->  _binding.textLevel3.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel4 ->  _binding.btnLevel4.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel4 ->  _binding.textLevel4.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel5 ->  _binding.btnLevel5.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel5 ->  _binding.textLevel5.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel6 ->  _binding.btnLevel6.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel6 ->  _binding.textLevel6.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel7 ->  _binding.btnLevel7.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel7 ->  _binding.textLevel7.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel8 ->  _binding.btnLevel8.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel8 ->  _binding.textLevel8.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel9 ->  _binding.btnLevel9.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel9 ->  _binding.textLevel9.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.btnLevel10 ->  _binding.btnLevel10.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
            _binding.textLevel10 ->  _binding.textLevel10.setBackgroundColor(resources.getColor(if(onOff) R.color.signup_level_true_color else R.color.signup_level_false_color))
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}