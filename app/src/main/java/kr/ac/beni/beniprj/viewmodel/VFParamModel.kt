package kr.ac.beni.beniprj.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VFParamModel : ViewModel() {
    private var _useModeType: MutableLiveData<CharSequence> = MutableLiveData()

    fun getUseModeType(): MutableLiveData<CharSequence> = _useModeType

    fun updateUseModeType(input: CharSequence) {
        _useModeType.value = input
    }
}