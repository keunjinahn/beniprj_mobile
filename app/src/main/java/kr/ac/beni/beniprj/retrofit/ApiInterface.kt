package kr.ac.beni.beniprj.retrofit

import kr.ac.beni.beniprj.model.*
import kr.ac.beni.beniprj.model.request.Requests
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // 대화 생성
    @POST("/api/label/v1/cedr_chat")
    fun setNewChat(@Body body: Requests.SetNewChat) : Call<NewChatResult>

    // 채팅 전송
    @POST("/api/label/v1/cedr_chat_hist")
    fun setSendChat(@Body body: Requests.SetSendChat) : Call<SendChatResult>

    // 피싱 체크
    @POST("/api/label/v1/check_voicef")
    fun getBeniPrj(@Body body: Requests.GetBeniPrj) : Call<GetBeniPrjResult>

    // 피싱 체크 Room
    @POST("/api/label/v1/check_voicef_room")
    fun getBeniPrjRoom(@Body body: Requests.GetBeniPrjRoom) : Call<GetBeniPrjRoomResult>

    /////////////////////////////////////////////////////////////
    @POST("/api/v1/add_user")
    fun setNewUserInfo(@Body body: Requests.SetNewUserInfo) : Call<NewUserInfoResult>

    @POST("/api/v1/add_medical_hist")
    fun setMedicalHist(@Body body: Requests.SetMedicalHist) : Call<SetMedicalHistResult>

    @POST("/api/v1/medical_hist_list")
    fun getMedicalHist(@Body body: Requests.GetMedicalHist) : Call<GetMedicalHistResult>

}