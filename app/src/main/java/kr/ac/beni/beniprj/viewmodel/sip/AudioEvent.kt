package kr.ac.beni.beniprj.viewmodel.sip

interface AudioEvent {
    fun onCallbackAudioRawData(p2: ByteArray?, p3: Int)
}