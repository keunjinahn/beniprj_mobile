package kr.ac.beni.beniprj.ui.run

import android.content.Context
import android.content.Intent
import android.media.AudioFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.FragmentRunBinding
import kr.ac.beni.beniprj.model.Chat
import kr.ac.beni.beniprj.model.GetBeniPrjResult
import kr.ac.beni.beniprj.model.NewChatResult
import kr.ac.beni.beniprj.model.SendChatResult
import kr.ac.beni.beniprj.model.request.Requests
import kr.ac.beni.beniprj.retrofit.ApiResponse
import kr.ac.beni.beniprj.service.BackgroundService
import kr.ac.beni.beniprj.ui.JoinActivity
import kr.ac.beni.beniprj.ui.adapter.ChatListAdapter
import kr.ac.beni.beniprj.ui.custom.BaseDialog
import kr.ac.beni.beniprj.ui.custom.CustomChatRegisterDialog
import kr.ac.beni.beniprj.ui.custom.CustomYesOrNoDialog
import kr.ac.beni.beniprj.ui.custom.OnBackPressListener
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.EventObserver
import kr.ac.beni.beniprj.util.OnThrottleClickListener
import kr.ac.beni.beniprj.viewmodel.VFViewModel
import kr.ac.beni.beniprj.viewmodel.sip.AudioEvent
import kr.ac.beni.beniprj.viewmodel.sip.SipViewModel
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.StandardCharsets


class RunFragment : Fragment(), OnBackPressListener, AudioEvent {

    private lateinit var binding: FragmentRunBinding
    private lateinit var mAdapter: ChatListAdapter

    private lateinit var mSipViewModel: SipViewModel
    private lateinit var mVFViewModel: VFViewModel
    private var mContext:Context? = null
    private var list: ArrayList<Chat> = arrayListOf()

    private var lastNewChatResult: NewChatResult? = null
    private var lastSendChatResult: SendChatResult? = null
    private var lastGetBeniPrjResult: GetBeniPrjResult? = null

    private var chatSendCount = 0
    private var lastRecordChatMsg = ""
    private var lastChatTitle = ""
    private var lastSenderNumber = ""
    private var lastReceiverNumber = ""

    private var recording = false
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setFragmentResultListener(Const.FRAGMENT_REQUEST_WORK_END){ _, _ ->
//            val inOut = PreferenceUtil.getInOut()
//            if(inOut == Const.InOut.IN.name) callGetVisitLogs()
//            else callSetWorkEndOut()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        CommonUtils.commonLog("[RunFragment] onCreateView")
        mSipViewModel = ViewModelProvider(this)[SipViewModel::class.java]
        mSipViewModel.setEvent(this)
        initRecognizer()

        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        binding = FragmentRunBinding.inflate(inflater, container, false)
        val bundle = arguments
        if(bundle != null){
            val text = bundle.getString("text")
            CommonUtils.commonLog("text ===> $text")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUIEvent()
        initControl()

        initObserve()
    }

    private fun initObserve(){
        setNewChatObserve()
        setSendChatObserve()
        getBeniPrjObserve()
    }

    private fun setNewChatObserve(){
        mVFViewModel.getSetNewChat().observe(viewLifecycleOwner, EventObserver{
            binding.progress.visibility = View.GONE
            if(it.isSuccess()) {
                val newChatResult: NewChatResult? = it.getResponse()?.body()

                lastNewChatResult = newChatResult

                binding.textChatTitle.text = lastChatTitle
                binding.textReceiverNumber.text = lastReceiverNumber
                binding.textSenderNumber.text = lastSenderNumber

                startRecord()

                Toast.makeText(requireActivity(), "채팅이 시작되었습니다.", Toast.LENGTH_SHORT).show()

            } else {
                observeResponseError(it, Const.Api.SetNewChat)
            }
        })
    }

    private fun setSendChatObserve(){
        mVFViewModel.getSetSendChat().observe(viewLifecycleOwner, EventObserver{
            binding.progress.visibility = View.GONE
            if(it.isSuccess()) {

                val sendChatResult: SendChatResult? = it.getResponse()?.body()
                lastSendChatResult = sendChatResult

                val showingText = if (sendChatResult?.voicefYn != null && sendChatResult.voicefYn == 1) {
                    "보이스피싱일 가능성이 높습니다. 주의해주세요!"
                } else {
                    "정상 대화입니다."
                }
                mAdapter.addItem(
                    Chat(
                        type = "system",
                        contents = showingText
                    )
                )
                mAdapter.notifyDataSetChanged()
                binding.rv.scrollToPosition(mAdapter.itemCount - 1)

                // 채팅 3번마다 BeniPrj Checking
                chatSendCount++
                if (chatSendCount % 3 == 0) {
                    chatSendCount = 0
                    callGetBeniPrj()
                }
                //

            } else {
                //observeResponseError(it, Const.Api.SetNewChat)
            }
        })
    }

    private fun getBeniPrjObserve(){
        mVFViewModel.getGetBeniPrj().observe(viewLifecycleOwner, EventObserver{
            binding.progress.visibility = View.GONE
            if(it.isSuccess()) {
                val getBeniPrjResult: GetBeniPrjResult? = it.getResponse()?.body()

                lastGetBeniPrjResult = getBeniPrjResult

                val showingText = if (getBeniPrjResult?.voicefYn != null && getBeniPrjResult.voicefYn == 1) {
                    "보이스피싱일 가능성이 높습니다. 주의해주세요!"
                } else {
                    "정상 대화입니다."
                }
                mAdapter.addItem(
                    Chat(
                        type = "system",
                        contents = showingText
                    )
                )
                mAdapter.notifyDataSetChanged()
                binding.rv.scrollToPosition(mAdapter.itemCount - 1)

            } else {
                observeResponseError(it, Const.Api.SetNewChat)
            }
        })
    }

    private fun callSetNewChat() {
        binding.progress.visibility = View.VISIBLE

        mVFViewModel.callSetNewChat(
            Requests.SetNewChat(
                chatTitle = lastChatTitle,
                mobileReceiver = lastReceiverNumber,
                mobileSender = lastSenderNumber
            )
        )
    }

    private fun callSetSendChat() {
        try {
            mVFViewModel.callSetSendChat(
                Requests.SetSendChat(
                    chatMsg = lastRecordChatMsg,
                    talkUserType = 1,
                    fkChatId = lastNewChatResult!!.id!!
                )
            )
        }catch (e: Exception) {
            CommonUtils.commonErrLog("[callSetSendChat] exception : e")
        }
    }

    private fun callGetBeniPrj() {
        try {
            mVFViewModel.callGetBeniPrj(
                Requests.GetBeniPrj(
                    fkChatHistId = lastSendChatResult!!.id!!
                )
            )
        }catch (e: Exception) {
            CommonUtils.commonErrLog("[callGetBeniPrj] exception : e")
        }
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
                        Const.Api.SetNewChat -> {
                            callSetNewChat()
                        }
                        Const.Api.SetSendChat -> {
                            callSetSendChat()
                        }
                        Const.Api.GetBeniPrj -> {
                            callGetBeniPrj()
                        }
                        else -> {
                            Toast.makeText(requireActivity(), "호출 타입이 존재하지 않습니다.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    when (name) {
                        Const.Api.SetNewChat -> {

                        }
                        Const.Api.SetSendChat -> {

                        }
                        Const.Api.GetBeniPrj -> {

                        }
                        else -> {
                            //
                        }
                    }
                }
            }
        }).show()
    }

    fun initView(){
        CommonUtils.commonLog("[RunFragment] initView")
        binding.rv.layoutManager = LinearLayoutManager(context)
        mAdapter = ChatListAdapter(object : ChatListAdapter.OnItemClickListener{
            override fun onClick(data: Chat) {
                //
            }
        })
        binding.rv.adapter = mAdapter
    }

    private fun initControl() {
//        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireActivity().packageName)
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR")   //한국어
        //intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }


    private fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    private fun setUIEvent(){
        binding.textEnd.onThrottleClick {
            mSipViewModel.hangup()

            endProcess()

            Toast.makeText(requireActivity(),getString(R.string.toast_work_end), Toast.LENGTH_SHORT).show()
            requireActivity().stopService(Intent(requireActivity(), BackgroundService::class.java))
            requireActivity().finish()
            JoinActivity.startActivity(requireActivity())
        }

        binding.textCreateChat.onThrottleClick {

            if (recording) {
                Toast.makeText(requireActivity(), "채팅이 종료되었습니다.", Toast.LENGTH_SHORT).show()
                resetChat()

                endProcess()
                stopRecord()

                stopRecognizer()
                mSipViewModel.hangup()

            } else {
                CustomChatRegisterDialog(mContext!!, object : CustomChatRegisterDialog.OnChatRegisterListener{
                    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                    override fun onConfirmClick(title: String, senderNumber: String, receiverNumber: String, receiverAddress: String) {

                        lastChatTitle = title
                        lastSenderNumber = senderNumber
                        lastReceiverNumber = receiverNumber

                        callSetNewChat()
                        //startRecord()
                        startProcess()

                        mSipViewModel.dial(receiverAddress)
                    }
                }).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun startProcess() {
        val speechRecognizerIntent = createSpeechRecognizerIntent()
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    fun endProcess() {
        try {
            speechRecognizer.cancel()
        }catch (_: Exception) {}
        try {
            speechRecognizer.stopListening()
        }catch (_: Exception) {}
    }

    fun resetChat() {
        lastNewChatResult = null
        lastSendChatResult = null
        lastGetBeniPrjResult = null
        lastSenderNumber = ""
        lastReceiverNumber = ""
        lastChatTitle = ""
        lastRecordChatMsg = ""
        chatSendCount = 0

        binding.textChatTitle.text = "-"
        binding.textReceiverNumber.text = "-"
        binding.textSenderNumber.text = "-"

        mAdapter.lists = list
        mAdapter.notifyDataSetChanged()
    }

    //녹음 시작
    fun startRecord(isReset: Boolean = true) {
        recording = true

        binding.textCreateChat.setText("채팅종료")

//        if (isReset) {
//            binding.recordResult.text = ""
//        }

//        try {
//            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireActivity())
//            speechRecognizer.setRecognitionListener(listener)
//            speechRecognizer.startListening(intent)
//        }catch (e: Exception) {
//            CommonUtils.commonLog("[RunFragment] startRecord exception : $e")
//        }
    }

    //녹음 중지
    private fun stopRecord() {
        recording = false

        binding.textCreateChat.setText("채팅시작")

        speechRecognizer.stopListening()
    }

//    var listener: RecognitionListener = object : RecognitionListener {
//        override fun onReadyForSpeech(bundle: Bundle) {}
//        override fun onBeginningOfSpeech() {
//            //사용자가 말하기 시작
//        }
//
//        override fun onRmsChanged(v: Float) {}
//        override fun onBufferReceived(bytes: ByteArray) {
//            Log.d("PARK", "[onBufferReceived] BODY : " + String(bytes, StandardCharsets.UTF_8))
//        }
//        override fun onEndOfSpeech() {
//            //사용자가 말을 멈추면 호출
//            //인식 결과에 따라 onError나 onResults가 호출됨
//        }
//
//        override fun onError(error: Int) {    //토스트 메세지로 에러 출력
//            val message: String = when (error) {
//                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
//                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
//                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
//                SpeechRecognizer.ERROR_NO_MATCH -> {
//                    if (recording) startRecord(false)
//                    return
//                }
//
//                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER가 바쁨"
//                SpeechRecognizer.ERROR_SERVER -> "서버 에러"
//                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간 초과"
//                else -> "알 수 없는 오류"
//            }
////            Toast.makeText(requireActivity(), "에러가 발생하였습니다. : $message", Toast.LENGTH_SHORT)
////                .show()
//            //stopRecord()
//        }
//
//        //인식 결과가 준비되면 호출
//        override fun onResults(bundle: Bundle) {
//            val matches =
//                bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) //인식 결과를 담은 ArrayList
//
//            // 구
////            val originText: String = binding.recordResult.text.toString() //기존 text
////
////            //인식 결과
////            var newText = ""
////            for (i in matches!!.indices) {
////                newText += matches[i]
////            }
////            binding.recordResult.text = "$originText $newText" //기존의 text에 인식 결과를 이어붙임
//
//            // 신규
//            var newText = ""
//            for (i in matches!!.indices) {
//                newText += matches[i]
//            }
//
//            mAdapter.addItem(
//                Chat(
//                type = "user",
//                contents = newText
//                )
//            )
//
//            // TEST
////            if (mAdapter.itemCount == 3) {
////                mAdapter.addItem(
////                    Chat(
////                        type = "system",
////                        contents = "보이스피싱일 가능성이 높습니다. 주의해주세요!"
////                    )
////                )
////            }
//
//            mAdapter.notifyDataSetChanged()
//            binding.rv.scrollToPosition(mAdapter.itemCount - 1)
//
//            lastRecordChatMsg = newText
//            callSetSendChat()
//
//            speechRecognizer.startListening(intent) //녹음버튼을 누를 때까지 계속 녹음해야 하므로 녹음 재개
//        }
//
//        override fun onPartialResults(bundle: Bundle) {
//            //
//        }
//        override fun onEvent(i: Int, bundle: Bundle) {}
//    }

    /**
     * LIFE CYCLE START
     */
    override fun onBackPress(): Boolean {
        return false
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        if(visitListDialog != null) visitListDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
    /**
     * LIFE CYCLE END
     */

    private fun showCarryingMode(){
        //binding.inputCode.visibility    = View.GONE
    }


    lateinit var m_audioPipe: Array<ParcelFileDescriptor>
    var mOutputStream: OutputStream? = null

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private fun createSpeechRecognizerIntent(): Intent {

        m_audioPipe = ParcelFileDescriptor.createPipe()
        mOutputStream = ParcelFileDescriptor.AutoCloseOutputStream(m_audioPipe[1])

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
            2000
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
            5000
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
            2000
        )

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE, m_audioPipe[0])
        //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE, mOutputStream)

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_CHANNEL_COUNT, 1)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_AUDIO_SOURCE_ENCODING,
            AudioFormat.ENCODING_PCM_16BIT
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_AUDIO_SOURCE_SAMPLING_RATE, 8000)
        return speechRecognizerIntent
    }

    fun initRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {
                Log.i("VFAPPLOG", "onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                Log.i("VFAPPLOG", "onBeginningOfSpeech")
            }

            override fun onRmsChanged(v: Float) {
                //Log.i("VFAPPLOG", "v = $v")
            }

            override fun onBufferReceived(bytes: ByteArray) {
            }

            override fun onEndOfSpeech() {
                Log.i("VFAPPLOG", "onEndOfSpeech")
                //stopRecognizer()
            }

            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onError(i: Int) {
                Log.i("VFAPPLOG", "onError = $i")

                if (recording) {
                    startProcess()
                }
            }

            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onResults(bundle: Bundle) {
                Log.i("VFAPPLOG", "[onResults]")
                val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (data != null) {
                    var newText = ""
                    for (i in data.indices) {
                        newText += data[i]
                    }
                    Log.i("VFAPPLOG", "[onResults] : $newText")
                }

                if (data != null && data.size > 0) {
                    val resultData = data[0]
                    Log.i(
                        "VFAPPLOG",
                        "[onResults] resultData = " + resultData + ", data.get(0) = " + data[0]
                    )

                    if (resultData.isNotEmpty() && resultData.isNotBlank() && resultData.length > 1) {
                        mAdapter.addItem(
                            Chat(
                                type = "user",
                                contents = resultData
                            )
                        )

                        mAdapter.notifyDataSetChanged()
                        binding.rv.scrollToPosition(mAdapter.itemCount - 1)

                        lastRecordChatMsg = resultData
                        callSetSendChat()
                    }
                }

                if (recording) {
                    startProcess()
                }
            }

            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onPartialResults(bundle: Bundle) {
//                Log.i("VFAPPLOG", "onPartialResults")
                val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (data != null && data.size > 0) {
                    val resultData = data[0]
                    Log.i(
                        "VFAPPLOG",
                        "[onPartialResults] resultData = " + resultData + ", data.get(0) = " + data[0]
                    )

                    if (resultData.isNotEmpty() && resultData.isNotBlank() && resultData.length > 1) {
                        mAdapter.addItem(
                            Chat(
                                type = "user",
                                contents = resultData
                            )
                        )

                        mAdapter.notifyDataSetChanged()
                        binding.rv.scrollToPosition(mAdapter.itemCount - 1)

                        lastRecordChatMsg = resultData
                        callSetSendChat()
                    }
                }
            }

            override fun onEvent(i: Int, bundle: Bundle) {
                Log.i("VFAPPLOG", "onEvent")
            }
        })
    }

    fun stopRecognizer() {

        Handler(Looper.getMainLooper()).post(Runnable {
            if (speechRecognizer != null) {
                speechRecognizer.stopListening()
                try {
                    if (mOutputStream != null) {
                        mOutputStream?.close()
                        mOutputStream = null
                    }
                } catch (e: IOException) {
                }
            }
        })
    }

    var counter = 0
    override fun onCallbackAudioRawData(p2: ByteArray?, p3: Int) {
        //Log.i("VFAPPLOG", "onCallbackAudioRawData : $p3 / ${counter++}")

        // if (counter > 50000) counter = 0

        try {

            mOutputStream!!.write(p2)
            mOutputStream!!.flush()

            if (counter % 100 == 0) {
                Log.i("VFAPPLOG", "onCallbackAudioRawData : $p3 / ${counter++}")
            }

            //if (counter > 50000) counter = 0

        }catch (e: Exception) {
            Log.e("VFAPPLOG", "[onCallbackAudioRawData] exception : $e")
        }
    }
}