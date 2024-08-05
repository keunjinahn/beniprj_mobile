package kr.ac.beni.beniprj.viewmodel.sip

import android.app.Application
import android.content.Context
import android.provider.MediaStore.Audio
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.beni.beniprj.Const.Companion.COMMON_TAG

class SipViewModel constructor(application: Application)
    : AndroidViewModel(application), AVConnEvent
{
    private val context: Context
        get() = getApplication<Application>().applicationContext

    private lateinit var audioEvent: AudioEvent

    private var avConnector: AVConnector = AVConnector(context)
    init {
        Log.d(COMMON_TAG, "[SipViewModel] init start.")

        avConnector.setEvent(this)

        viewModelScope.launch(Dispatchers.IO) {
            avConnector.setLocalPort(5060)
            avConnector.registerToServer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            avConnector.Destroy()
        } catch (_: Exception) {}
    }

    fun setEvent(event: AudioEvent) {
        audioEvent = event
    }

    fun dial(address: String) {
        avConnector.dial(address)
    }

    fun hangup() {
        avConnector.hangupCall()
    }

    override fun onLogin(statusCode: Int, twoChannel: AVConnector.TwoChannel?) {
        Log.d(COMMON_TAG, "[SipViewModel] onLogin : $statusCode")
    }

    override fun onLoginFail(
        statusCode: Int,
        statusText: String,
        twoChannel: AVConnector.TwoChannel?
    ) {
        Log.d(COMMON_TAG, "[SipViewModel] onLoginFail : $statusCode / $statusText")
    }

    override fun onConnect(statusCode: Int, twoChannel: AVConnector.TwoChannel?) {
        Log.d(COMMON_TAG, "[SipViewModel] onConnect : $statusCode")
    }

    override fun onDisconnect(statusCode: Int, twoChannel: AVConnector.TwoChannel?) {
        Log.d(COMMON_TAG, "[SipViewModel] onDisconnect : $statusCode")
    }

    override fun onDisconnectByPeer(statusCode: Int, twoChannel: AVConnector.TwoChannel?) {
        Log.d(COMMON_TAG, "[SipViewModel] statusCode : $statusCode")
    }

    override fun onConnectFail(
        statusCode: Int,
        statusText: String,
        twoChannel: AVConnector.TwoChannel?
    ) {
        Log.d(COMMON_TAG, "[SipViewModel] onConnectFail : $statusCode / $statusText")
    }

    override fun onInviteIncoming(sessionId: Long, caller: String) {
        Log.d(COMMON_TAG, "[SipViewModel] onInviteIncoming : $sessionId / $caller")
//        avConnector.acceptCall()
    }

    override fun onRecvSignaling(sessionId: Long, msg: String?) {
        Log.d(COMMON_TAG, "[SipViewModel] onRecvSignaling : $sessionId / $msg:")
    }

    override fun onRecvVideoRawData() {
        Log.d(COMMON_TAG, "[SipViewModel] onRecvVideoRawData")
    }

    override fun onRecvAudioRawData(p2: ByteArray?, p3: Int) {

        audioEvent.onCallbackAudioRawData(p2, p3)
    }
    //


}