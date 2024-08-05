package kr.ac.beni.beniprj.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.model.*
import kr.ac.beni.beniprj.model.request.Requests
import kr.ac.beni.beniprj.retrofit.ApiInterface
import kr.ac.beni.beniprj.retrofit.ApiResponse
import kr.ac.beni.beniprj.retrofit.RetrofitClient
import kr.ac.beni.beniprj.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VFRepository {
    private val call = RetrofitClient.createLiveDataRetrofitUnsafe(Const.BASE_URL)?.create(ApiInterface::class.java)

    private val setNewUserInfo= MutableLiveData<Event<ApiResponse<NewUserInfoResult>>>()
    fun setNewUserInfo(request: Requests.SetNewUserInfo): LiveData<Event<ApiResponse<NewUserInfoResult>>> {
        call?.setNewUserInfo(request)?.enqueue(object : Callback<NewUserInfoResult>{
            override fun onResponse(call: Call<NewUserInfoResult>, response: Response<NewUserInfoResult>) {
                val apiResponse = ApiResponse(response)
                setNewUserInfo.postValue(Event(apiResponse))
            }

            override fun onFailure(call: Call<NewUserInfoResult>, t: Throwable) {
                val apiResponse = ApiResponse<NewUserInfoResult>(t)
                setNewUserInfo.postValue(Event(apiResponse))
            }

        })
        return setNewUserInfo
    }

    private val setNewChat = MutableLiveData<Event<ApiResponse<NewChatResult>>>()
    fun setNewChat(request: Requests.SetNewChat): LiveData<Event<ApiResponse<NewChatResult>>> {
        call?.setNewChat(request)?.enqueue(object : Callback<NewChatResult>{
            override fun onResponse(call: Call<NewChatResult>, response: Response<NewChatResult>) {
                val apiResponse = ApiResponse(response)
                setNewChat.postValue(Event(apiResponse))
            }

            override fun onFailure(call: Call<NewChatResult>, t: Throwable) {
                val apiResponse = ApiResponse<NewChatResult>(t)
                setNewChat.postValue(Event(apiResponse))
            }

        })
        return setNewChat
    }

    private val setSendChat = MutableLiveData<Event<ApiResponse<SendChatResult>>>()
    fun setSendChat(request: Requests.SetSendChat): LiveData<Event<ApiResponse<SendChatResult>>> {
        call?.setSendChat(request)?.enqueue(object : Callback<SendChatResult>{
            override fun onResponse(call: Call<SendChatResult>, response: Response<SendChatResult>) {
                val apiResponse = ApiResponse(response)
                setSendChat.postValue(Event(apiResponse))
            }

            override fun onFailure(call: Call<SendChatResult>, t: Throwable) {
                val apiResponse = ApiResponse<SendChatResult>(t)
                setSendChat.postValue(Event(apiResponse))
            }

        })
        return setSendChat
    }

    private val getBeniPrj = MutableLiveData<Event<ApiResponse<GetBeniPrjResult>>>()
    fun getBeniPrj(request: Requests.GetBeniPrj): LiveData<Event<ApiResponse<GetBeniPrjResult>>> {
        call?.getBeniPrj(request)?.enqueue(object : Callback<GetBeniPrjResult>{
            override fun onResponse(call: Call<GetBeniPrjResult>, response: Response<GetBeniPrjResult>) {
                val apiResponse = ApiResponse(response)
                getBeniPrj.postValue(Event(apiResponse))
            }

            override fun onFailure(call: Call<GetBeniPrjResult>, t: Throwable) {
                val apiResponse = ApiResponse<GetBeniPrjResult>(t)
                getBeniPrj.postValue(Event(apiResponse))
            }

        })
        return getBeniPrj
    }

    private val getBeniPrjRoom = MutableLiveData<Event<ApiResponse<GetBeniPrjRoomResult>>>()
    fun getBeniPrjRoom(request: Requests.GetBeniPrjRoom): LiveData<Event<ApiResponse<GetBeniPrjRoomResult>>> {
        call?.getBeniPrjRoom(request)?.enqueue(object : Callback<GetBeniPrjRoomResult>{
            override fun onResponse(call: Call<GetBeniPrjRoomResult>, response: Response<GetBeniPrjRoomResult>) {
                val apiResponse = ApiResponse(response)
                getBeniPrjRoom.postValue(Event(apiResponse))
            }

            override fun onFailure(call: Call<GetBeniPrjRoomResult>, t: Throwable) {
                val apiResponse = ApiResponse<GetBeniPrjRoomResult>(t)
                getBeniPrjRoom.postValue(Event(apiResponse))
            }

        })
        return getBeniPrjRoom
    }
}