package kr.ac.beni.beniprj.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.ac.beni.beniprj.Const

class VFParamModel : ViewModel() {
    /*
    private val _useModeType = LiveData<String>()
    val useModeType: LiveData<String> get() = _useModeType
    private var _runModeType: MutableLiveData<Const.RunModeType> = MutableLiveData()

    fun updateUseModeType(input: Const.UserModeType) {
        _useModeType.value = input
    }

    fun getRunModeType(): MutableLiveData<Const.RunModeType> = _runModeType
    fun updateRunModeType(input: Const.RunModeType) {
        _runModeType.value = input
    }
    */

    private val _useModeType = MutableLiveData<Const.UserModeType>()
    val useModeType: LiveData<Const.UserModeType> get() = _useModeType

    fun updateUseModeType(newData: Const.UserModeType) {
        _useModeType.value = newData
    }

    private val _runModeType = MutableLiveData<Const.RunModeType>()
    val runModeType: LiveData<Const.RunModeType> get() = _runModeType

    fun updateRunModeType(newData: Const.RunModeType) {
        _runModeType.value = newData
    }
}