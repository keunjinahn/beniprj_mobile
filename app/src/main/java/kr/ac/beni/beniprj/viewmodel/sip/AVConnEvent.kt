package kr.ac.beni.beniprj.viewmodel.sip

import kr.ac.beni.beniprj.viewmodel.sip.AVConnector.TwoChannel

interface AVConnEvent {
    fun onLogin(statusCode: Int, twoChannel: TwoChannel?)
    fun onLoginFail(statusCode: Int, statusText: String, twoChannel: TwoChannel?)
    fun onConnect(statusCode: Int, twoChannel: TwoChannel?)
    fun onDisconnect(statusCode: Int, twoChannel: TwoChannel?)
    fun onDisconnectByPeer(statusCode: Int, twoChannel: TwoChannel?)
    fun onConnectFail(statusCode: Int, statusText: String, twoChannel: TwoChannel?)
    fun onInviteIncoming(sessionId: Long, caller: String)
    fun onRecvSignaling(sessionId: Long, msg: String?)
    fun onRecvVideoRawData()
    fun onRecvAudioRawData(p2: ByteArray?, p3: Int)
}