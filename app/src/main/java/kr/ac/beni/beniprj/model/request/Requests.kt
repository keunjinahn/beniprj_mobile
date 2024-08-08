package kr.ac.beni.beniprj.model.request

import com.google.gson.annotations.SerializedName

class Requests {
    data class SetNewChat(
        @SerializedName("chat_title") val chatTitle: String,
        @SerializedName("mobile_sender") val mobileSender: String,
        @SerializedName("mobile_receiver") val mobileReceiver: String
    )

    data class SetSendChat(
        @SerializedName("chat_msg") val chatMsg: String,
        @SerializedName("talk_user_type") val talkUserType: Int,
        @SerializedName("fk_chat_id") val fkChatId: Int
    )

    data class GetBeniPrj(
        @SerializedName("fk_chat_hist_id") val fkChatHistId: Int
    )

    data class GetBeniPrjRoom(
        @SerializedName("fk_chat_id") val fkChatId: Int
    )

    ///////////////////////////////////////////////////////////
    data class SetNewUserInfo(
        @SerializedName("name") val name: String,
        @SerializedName("age") val age: Int,
        @SerializedName("sex") val sex: Int,
        @SerializedName("disease_num") val diseaseNum: Int,
        @SerializedName("disease_level") val diseaseLevel: Int
    )

    ///////////////////////////////////////////////////////////
    data class GetMedicalHist(
        @SerializedName("fk_user_id") val fkUserId: Int
    )


    data class SetMedicalHist(
        @SerializedName("medicalMainName") val medicalMainName: String,
        @SerializedName("medicalMainCode") val medicalMainCode: String,
        @SerializedName("medicalSubName") val medicalSubName: String,
        @SerializedName("medicalSubCode") val medicalSubCode: String,
        @SerializedName("operationDuration") val operationDuration: String,
        @SerializedName("operationHz") val operationHz: String,
        @SerializedName("operationRate") val operationRate: String,
        @SerializedName("medicalDate") val medicalDate: String,
        @SerializedName("fk_user_id") val fkUserId: Any?,
    )

}