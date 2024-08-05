package kr.ac.beni.beniprj.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kr.ac.beni.beniprj.model.GetBeniPrjResult
import kr.ac.beni.beniprj.model.GetBeniPrjRoomResult
import kr.ac.beni.beniprj.model.NewChatResult
import kr.ac.beni.beniprj.model.NewUserInfoResult
import kr.ac.beni.beniprj.model.SendChatResult
import kr.ac.beni.beniprj.model.request.Requests
import kr.ac.beni.beniprj.repository.VFRepository
import kr.ac.beni.beniprj.retrofit.ApiResponse
import kr.ac.beni.beniprj.util.Event

class VFViewModel constructor(application: Application)
    : AndroidViewModel(application)
{
    private var setNewChat: MutableLiveData<Event<Requests.SetNewChat>> = MutableLiveData()
    private var setSendChat: MutableLiveData<Event<Requests.SetSendChat>> = MutableLiveData()
    private var getBeniPrj: MutableLiveData<Event<Requests.GetBeniPrj>> = MutableLiveData()
    private var getBeniPrjRoom: MutableLiveData<Event<Requests.GetBeniPrjRoom>> = MutableLiveData()

    //
    private var setNewChatLiveData: LiveData<Event<ApiResponse<NewChatResult>>> = Transformations.switchMap(setNewChat) { VFRepository.setNewChat(it.peekContent()) }
    fun callSetNewChat(request: Requests.SetNewChat){ setNewChat.value = Event(request) }
    fun getSetNewChat(): LiveData<Event<ApiResponse<NewChatResult>>>{ return setNewChatLiveData }

    private var setSendChatLiveData: LiveData<Event<ApiResponse<SendChatResult>>> = Transformations.switchMap(setSendChat) { VFRepository.setSendChat(it.peekContent()) }
    fun callSetSendChat(request: Requests.SetSendChat){ setSendChat.value = Event(request) }
    fun getSetSendChat(): LiveData<Event<ApiResponse<SendChatResult>>>{ return setSendChatLiveData }

    private var getBeniPrjLiveData: LiveData<Event<ApiResponse<GetBeniPrjResult>>> = Transformations.switchMap(getBeniPrj) { VFRepository.getBeniPrj(it.peekContent()) }
    fun callGetBeniPrj(request: Requests.GetBeniPrj){ getBeniPrj.value = Event(request) }
    fun getGetBeniPrj(): LiveData<Event<ApiResponse<GetBeniPrjResult>>>{ return getBeniPrjLiveData }

    private var getBeniPrjRoomLiveData: LiveData<Event<ApiResponse<GetBeniPrjRoomResult>>> = Transformations.switchMap(getBeniPrjRoom) { VFRepository.getBeniPrjRoom(it.peekContent()) }
    fun callGetBeniPrjRoom(request: Requests.GetBeniPrjRoom){ getBeniPrjRoom.value = Event(request) }
    fun getGetBeniPrjRoom(): LiveData<Event<ApiResponse<GetBeniPrjRoomResult>>>{ return getBeniPrjRoomLiveData }


    /////////////////////////////////////////////////
    private var setNewUserInfo: MutableLiveData<Event<Requests.SetNewUserInfo>> = MutableLiveData()
    private var setNewUserInfoLiveData: LiveData<Event<ApiResponse<NewUserInfoResult>>> = Transformations.switchMap(setNewUserInfo) { VFRepository.setNewUserInfo(it.peekContent()) }
    fun callSetNewUserInfo(request: Requests.SetNewUserInfo){ setNewUserInfo.value = Event(request) }
    fun getSetNewUserInfo(): LiveData<Event<ApiResponse<NewUserInfoResult>>>{ return setNewUserInfoLiveData}
}