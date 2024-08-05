package kr.ac.beni.beniprj.viewmodel.sip

import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.portsip.OnPortSIPEvent
import com.portsip.PortSIPVideoRenderer
import com.portsip.PortSipEnumDefine
import com.portsip.PortSipErrorcode
import com.portsip.PortSipSdk
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.VFApplication

class AVConnector constructor(
    private val context: Context
) : OnPortSIPEvent {

    private val TAG: String = Const.COMMON_TAG

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    enum class TwoChannel {
        Audio, Video, All
    }

    private var portSipSdk: PortSipSdk = PortSipSdk()

    private var remoteVideoView: PortSIPVideoRenderer? = null
    private var localVideoView: PortSIPVideoRenderer? = null

    private val scalingType =
        PortSIPVideoRenderer.ScalingType.SCALE_ASPECT_BALANCED // SCALE_ASPECT_FIT or SCALE_ASPECT_FILL;


    private lateinit var avConnEvent: AVConnEvent

    private val twoChannel: TwoChannel = TwoChannel.All

    private var localPort = 5060

    private var sessionId : Long = 0

    private var bitrate = 2000
    private var framerate = 30
    private var resolution = 2

    private var applicaton: VFApplication = context.applicationContext as VFApplication

    fun setEvent(avConnEvent: AVConnEvent) {
        this.avConnEvent = avConnEvent
    }

    fun setSessionId(id: Long) {
        sessionId = id
    }

    fun setLocalPort(port: Int) {
        localPort = port
    }

    fun setRemoteVideoView(remoteVideoView: PortSIPVideoRenderer?) {
        this.remoteVideoView = remoteVideoView!!
        remoteVideoView.setScalingType(scalingType)
    }

    fun setLocalVideoView(localVideoView: PortSIPVideoRenderer?) {
        this.localVideoView = localVideoView!!
        localVideoView.setScalingType(scalingType)
    }

    fun updateVideo() {
        if (remoteVideoView != null && localVideoView != null) {
            portSipSdk.setRemoteVideoWindow(sessionId, remoteVideoView)

            // display Local video, mirror front camera
            portSipSdk.setLocalVideoWindow(localVideoView)
            portSipSdk.displayLocalVideo(true, true)
            portSipSdk.sendVideo(sessionId, false)

            Log.d(TAG, "[AVConnector] updateVideo()!! $sessionId")
        }

        portSipSdk.sendVideo(sessionId, false)
    }

    fun acceptCall() {
        Log.d(TAG, "[AVConnector] acceptCall() $sessionId")
        portSipSdk.answerCall(sessionId, true)

        //portSipSdk.muteSession(sessionId, isSoundMute, isSoundMute, false, false)
    }

    fun rejectCall() {
        Log.d(TAG, "[AVConnector] rejectCall() $sessionId")
        portSipSdk.rejectCall(sessionId, 486) // Busy
    }

    fun answerAudioCall() {
        portSipSdk.muteSession(sessionId, false, false, false, false)
    }

    fun sendVideo(isSend: Boolean) {
        portSipSdk.sendVideo(sessionId, isSend)
    }

    fun hangupCall() {
        Log.d(TAG, "[AVConnector] hangupCall() $sessionId")
        portSipSdk.hangUp(sessionId)
    }

    fun dial(targetAddress: String) : String {

        portSipSdk.enableAudioStreamCallback(-1, true, PortSipEnumDefine.ENUM_AUDIOSTREAM_BOTH_PER_CHANNEL)

        sessionId = portSipSdk.call(targetAddress, true, false)

        if (sessionId <= 0) {
            // showTips("Call failure")
            Log.d(TAG, "[AVConnector] Dial failure.")
            return "Call failure"
        }

        //default send video
        //portSipSdk.sendVideo(sessionId, true)
        Log.d(TAG, "[AVConnector] Dial success. : $targetAddress")
        return "Call success"
    }

    fun registerToServer () {
        var result = 0
        portSipSdk.setOnPortSIPEvent(this)
        portSipSdk.DeleteCallManager()
        portSipSdk.CreateCallManager(applicaton)

        //val localPort = Constants.PORTSIP_LOCAL_PORT.toInt()
        val dataPath: String = context.getExternalFilesDir(null)!!.absolutePath
        val certRoot = "$dataPath/certs"

        result = portSipSdk.initialize(
            PortSipEnumDefine.ENUM_TRANSPORT_UDP, "0.0.0.0", localPort,
            PortSipEnumDefine.ENUM_LOG_LEVEL_DEBUG, dataPath,
            8, "Thingscare SDK for Android", 0, 0, certRoot, "", false, null
        )

        Log.w(TAG, "[registerToServer] localPort : $localPort")

        var registerTips = "initialize failed"
        if (result == PortSipErrorcode.ECoreErrorNone) {

            //init failed
            registerTips = "ECoreWrongLicenseKey"

            result = portSipSdk.setLicenseKey(Const.PORTSIP_LICENSE_KEY)
            if (result != PortSipErrorcode.ECoreWrongLicenseKey) {

                if (twoChannel === TwoChannel.Audio || twoChannel === TwoChannel.All) {

//                    mainThreadHandler.post {
//                        portSipSdk.setAudioDevice(PortSipEnumDefine.AudioDevice.SPEAKER_PHONE)
//                    }

                    portSipSdk.clearAudioCodec()
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMA)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMU)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_DTMF)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_OPUS)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G722)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G729)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_GSM)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEX)
                    portSipSdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_AMR)
                    portSipSdk.enableAEC(true)
                    portSipSdk.enableAGC(true)
                    portSipSdk.enableANS(true)
                    portSipSdk.enableCNG(false)
                    portSipSdk.enableVAD(false)
                }

                if (twoChannel === TwoChannel.Video || twoChannel === TwoChannel.All) {
                    portSipSdk.clearVideoCodec()
                    portSipSdk.setSrtpPolicy(PortSipEnumDefine.ENUM_SRTPPOLICY_NONE)
                    portSipSdk.setVideoMTU(1100)
                    portSipSdk.setVideoNackStatus(true)
                    portSipSdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_H264)
                    portSipSdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_VP8)
                    portSipSdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_VP9)
                    portSipSdk.setVideoBitrate(-1, bitrate)
                    portSipSdk.setVideoFrameRate(-1, framerate)
                    val resolutionSize: Point = getResolutionSize(resolution)
                    portSipSdk.setVideoResolution(resolutionSize.x, resolutionSize.y)
                    portSipSdk.setVideoDeviceId(1)

                    portSipSdk.setVideoCropAndScale(true)
                }

                portSipSdk.enableReliableProvisional(false)
                portSipSdk.enable3GppTags(false)

                portSipSdk.enableCallbackSignaling(false, true)
                portSipSdk.enableVideoHardwareCodec(true, true)

                // TEST
                //portSipSdk.setRtpCallback(true)
                portSipSdk.enableAudioStreamCallback(-1, true, PortSipEnumDefine.ENUM_AUDIOSTREAM_BOTH_PER_CHANNEL)
                //

                registerTips = "setUser failed"
                result = -1

                result = portSipSdk.setUser("1234", "", "", "1234",
                    null, null, 0, null, 0, null, 0)

                if (result == PortSipErrorcode.ECoreErrorNone) {

                    onRegisterSuccess("online", result, "")
                }
            }

        }

        if (result != PortSipErrorcode.ECoreErrorNone) {
            onRegisterFailure(registerTips, result, "")
        }
    }

    fun Destroy() {
        portSipSdk.unRegisterServer()
        portSipSdk.DeleteCallManager()
    }

    // portsip event end
    private fun getResolutionSize(resolution: Int): Point {
        val resolutionSpec = Point(0, 0)
        when (resolution) {
            0 -> {
                resolutionSpec.x = 176
                resolutionSpec.y = 144
            }

            1 -> {
                resolutionSpec.x = 176
                resolutionSpec.y = 144
            }

            2 -> {
                resolutionSpec.x = 352
                resolutionSpec.y = 288
            }

            3 -> {
                resolutionSpec.x = 640
                resolutionSpec.y = 480
            }

            4 -> {
                resolutionSpec.x = 800
                resolutionSpec.y = 600
            }

            5 -> {
                resolutionSpec.x = 1024
                resolutionSpec.y = 768
            }

            6 -> {
                resolutionSpec.x = 1280
                resolutionSpec.y = 720
            }

            7 -> {
                resolutionSpec.x = 320
                resolutionSpec.y = 240
            }

            else -> {
                resolutionSpec.x = 176
                resolutionSpec.y = 144
            }
        }
        return resolutionSpec
    }

    override fun onRegisterSuccess(statusText: String?, statusCode: Int, newArg1: String?) {
        Log.d(TAG, "[AVConnector] onRegisterSuccess $sessionId")
        avConnEvent.onLogin(statusCode, twoChannel)
    }

    override fun onRegisterFailure(statusText: String?, statusCode: Int, newArg1: String?) {
        if (statusText != null) {
            Log.d(TAG, "[AVConnector] onRegisterFailure $sessionId")
            avConnEvent.onLoginFail(statusCode, statusText, twoChannel)
        }
    }

    override fun onInviteIncoming(
        sessionId: Long,
        callerDisplayName: String?,
        caller: String?,
        calleeDisplayName: String?,
        callee: String?,
        audioCodecNames: String?,
        videoCodecNames: String?,
        existsAudio: Boolean,
        existsVideo: Boolean,
        sipMessage: String?
    ) {
        Log.d(TAG, "[AVConnector] onInviteIncoming $sessionId $callerDisplayName $caller")

        //portSipSdk.enableVideoStreamCallback(sessionId, 2)

//        if (caller != null)
//            avConnEvent.onInviteIncoming(sessionId, caller)
//        else
//            avConnEvent.onInviteIncoming(sessionId, "")

        this.sessionId = sessionId
        portSipSdk.answerCall(sessionId, true)

        portSipSdk.sendVideo(sessionId, true)
    }

    override fun onInviteTrying(p0: Long) {
        Log.d(TAG, "[AVConnector] onInviteTrying $sessionId")
    }

    override fun onInviteSessionProgress(
        p0: Long,
        p1: String?,
        p2: String?,
        p3: Boolean,
        p4: Boolean,
        p5: Boolean,
        p6: String?
    ) {
        Log.d(TAG, "[AVConnector] onInviteSessionProgress $sessionId")
    }

    override fun onInviteRinging(sessionId: Long, p1: String?, p2: Int, p3: String?) {
        Log.d(TAG, "[AVConnector] onInviteRinging $sessionId")

        updateVideo()
    }

    override fun onInviteAnswered(
        sessionId: Long,
        callerDisplayName: String?,
        caller: String?,
        calleeDisplayName: String?,
        callee: String?,
        audioCodecs: String?,
        videoCodecs: String?,
        existsAudio: Boolean,
        existsVideo: Boolean,
        newArg1: String?
    ) {
        Log.d(TAG, "[AVConnector] onInviteAnswered $sessionId")
    }

    override fun onInviteFailure(sessionId: Long, reason: String, statusCode: Int, newArg1: String?) {
        Log.d(TAG, "[AVConnector] onInviteFailure $sessionId")
        avConnEvent.onConnectFail(statusCode, reason, twoChannel)
    }

    override fun onInviteUpdated(
        sessionId: Long,
        p1: String?,
        p2: String?,
        p3: Boolean,
        p4: Boolean,
        p5: String?
    ) {
        Log.d(TAG, "[AVConnector] onInviteUpdated $sessionId")
    }

    override fun onInviteConnected(sessionId: Long) {

        Log.d(TAG, "[AVConnector] onInviteConnected $sessionId")
        updateVideo()
        portSipSdk.enableAudioStreamCallback(sessionId, true, PortSipEnumDefine.ENUM_AUDIOSTREAM_BOTH_PER_CHANNEL)

        avConnEvent.onConnect(0, twoChannel)
    }

    override fun onInviteBeginingForward(p0: String?) {
        Log.d(TAG, "[AVConnector] onInviteBeginingForward $sessionId")
    }

    override fun onInviteClosed(sessionId: Long) {
        Log.d(TAG, "[AVConnector] onInviteClosed $sessionId")
        avConnEvent.onDisconnectByPeer(0, twoChannel)
    }

    override fun onDialogStateUpdated(p0: String?, p1: String?, p2: String?, p3: String?) {
        Log.d(TAG, "[AVConnector] onDialogStateUpdated $sessionId")
    }

    override fun onRemoteHold(p0: Long) {
        Log.d(TAG, "[AVConnector] onRemoteHold $sessionId")
    }

    override fun onRemoteUnHold(p0: Long, p1: String?, p2: String?, p3: Boolean, p4: Boolean) {
        Log.d(TAG, "[AVConnector] onRemoteUnHold $sessionId")
    }

    override fun onReceivedRefer(p0: Long, p1: Long, p2: String?, p3: String?, p4: String?) {
        Log.d(TAG, "[AVConnector] onReceivedRefer $sessionId")
    }

    override fun onReferAccepted(p0: Long) {
        Log.d(TAG, "[AVConnector] onReferAccepted $sessionId")
    }

    override fun onReferRejected(p0: Long, p1: String?, p2: Int) {
        Log.d(TAG, "[AVConnector] onReferRejected $sessionId")
    }

    override fun onTransferTrying(p0: Long) {
        Log.d(TAG, "[AVConnector] onTransferTrying $sessionId")
    }

    override fun onTransferRinging(p0: Long) {
        Log.d(TAG, "[AVConnector] onTransferRinging $sessionId")
    }

    override fun onACTVTransferSuccess(p0: Long) {
        Log.d(TAG, "[AVConnector] onACTVTransferSuccess $sessionId")
    }

    override fun onACTVTransferFailure(p0: Long, p1: String?, p2: Int) {
        Log.d(TAG, "[AVConnector] onACTVTransferFailure $sessionId")
    }

    override fun onReceivedSignaling(p0: Long, p1: String?) {
        //Log.d(TAG, "[AVConnector] onReceivedSignaling $sessionId $p1")
        avConnEvent.onRecvSignaling(p0, p1)
    }

    override fun onSendingSignaling(p0: Long, p1: String?) {
//        Log.d(TAG, "[AVConnector] onSendingSignaling $sessionId")
    }

    override fun onWaitingVoiceMessage(p0: String?, p1: Int, p2: Int, p3: Int, p4: Int) {
        Log.d(TAG, "[AVConnector] onWaitingVoiceMessage $sessionId")
    }

    override fun onWaitingFaxMessage(p0: String?, p1: Int, p2: Int, p3: Int, p4: Int) {
        Log.d(TAG, "[AVConnector] onWaitingFaxMessage $sessionId")
    }

    override fun onRecvDtmfTone(p0: Long, p1: Int) {
        Log.d(TAG, "[AVConnector] onRecvDtmfTone $sessionId")
    }

    override fun onRecvOptions(p0: String?) {
        Log.d(TAG, "[AVConnector] onRecvOptions $sessionId")
    }

    override fun onRecvInfo(p0: String?) {
        Log.d(TAG, "[AVConnector] onRecvInfo $sessionId")
    }

    override fun onRecvNotifyOfSubscription(p0: Long, p1: String?, p2: ByteArray?, p3: Int) {
        Log.d(TAG, "[AVConnector] onRecvNotifyOfSubscription $sessionId")
    }

    override fun onPresenceRecvSubscribe(p0: Long, p1: String?, p2: String?, p3: String?) {
        Log.d(TAG, "[AVConnector] onPresenceRecvSubscribe $sessionId")
    }

    override fun onPresenceOnline(p0: String?, p1: String?, p2: String?) {
        Log.d(TAG, "[AVConnector] onPresenceOnline $sessionId")
    }

    override fun onPresenceOffline(p0: String?, p1: String?) {
        Log.d(TAG, "[AVConnector] onPresenceOffline $sessionId")
    }

    override fun onRecvMessage(p0: Long, p1: String?, p2: String?, p3: ByteArray?, p4: Int) {
        Log.d(TAG, "[AVConnector] onRecvMessage $sessionId")
    }

    override fun onRecvOutOfDialogMessage(
        p0: String?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: String?,
        p6: ByteArray?,
        p7: Int,
        p8: String?
    ) {
        Log.d(TAG, "[AVConnector] onRecvOutOfDialogMessage $sessionId")
    }

    override fun onSendMessageSuccess(p0: Long, p1: Long) {
        Log.d(TAG, "[AVConnector] onSendMessageSuccess $sessionId")
    }

    override fun onSendMessageFailure(p0: Long, p1: Long, p2: String?, p3: Int) {
        Log.d(TAG, "[AVConnector] onSendMessageFailure $sessionId")
    }

    override fun onSendOutOfDialogMessageSuccess(
        p0: Long,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?
    ) {
        Log.d(TAG, "[AVConnector] onSendOutOfDialogMessageSuccess $sessionId")
    }

    override fun onSendOutOfDialogMessageFailure(
        p0: Long,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: String?,
        p6: Int
    ) {
        Log.d(TAG, "[AVConnector] onSendOutOfDialogMessageFailure $sessionId")
    }

    override fun onSubscriptionFailure(p0: Long, p1: Int) {
        Log.d(TAG, "[AVConnector] onSubscriptionFailure $sessionId")
    }

    override fun onSubscriptionTerminated(p0: Long) {
        Log.d(TAG, "[AVConnector] onSubscriptionTerminated $sessionId")
    }

    override fun onPlayAudioFileFinished(p0: Long, p1: String?) {
        Log.d(TAG, "[AVConnector] onPlayAudioFileFinished $sessionId")
    }

    override fun onPlayVideoFileFinished(p0: Long) {
        Log.d(TAG, "[AVConnector] onPlayVideoFileFinished $sessionId")
    }

    override fun onReceivedRTPPacket(p0: Long, isAudio: Boolean, p2: ByteArray?, packetSize: Int) {
        if (isAudio) {
            Log.d(TAG, "[AVConnector] onReceivedRTPPacket $sessionId / $packetSize")
        }
    }

    override fun onSendingRTPPacket(p0: Long, p1: Boolean, p2: ByteArray?, p3: Int) {
        //Log.d(TAG, "[AVConnector] onSendingRTPPacket $sessionId")
    }

    override fun onAudioRawCallback(p0: Long, p1: Int, p2: ByteArray?, p3: Int, p4: Int) {
        //Log.d(TAG, "[AVConnector] onAudioRawCallback $p0 / $p1 / ${p2?.size} / $p3 / $p4")
        avConnEvent.onRecvAudioRawData(p2, p3)
    }

    override fun onVideoRawCallback(sessionId: Long, enum_videoCallbackMode: Int, width: Int, height: Int, data: ByteArray?, dataLength: Int) {
        Log.d(TAG, "[AVConnector] onVideoRawCallback $enum_videoCallbackMode / $width / $height")
        avConnEvent.onRecvVideoRawData()
    }
}