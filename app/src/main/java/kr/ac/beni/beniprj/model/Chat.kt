package kr.ac.beni.beniprj.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Chat(
    var type: String,
    var contents: String,
    var voicefYn: Int = 0
)

@Parcelize
data class NewChatResult(
    @SerializedName("active") var active: String?,
    @SerializedName("chat_title") var chatTitle: String?,
    @SerializedName("created") var created: String?,
    @SerializedName("id") var id: Int?,
    @SerializedName("mobile_receiver") var mobileReceiver: String?,
    @SerializedName("mobile_sender") var mobileSender: String?
): Parcelable

@Parcelize
data class SendChatResult(
    @SerializedName("chat_msg") var chatMsg: String?,
    @SerializedName("created") var created: String?,
    @SerializedName("fk_chat_id") var fkChatId: Int?,
    @SerializedName("fk_model_id") var fkModelId: Int?,
    @SerializedName("fk_user_id") var fkUserId: Int?,
    @SerializedName("id") var id: Int?,
    @SerializedName("talk_user_type") var talkUserType: Int?,
    @SerializedName("voicef_yn") var voicefYn: Int?
): Parcelable

@Parcelize
data class GetBeniPrjResult(
    @SerializedName("voicef_yn") var voicefYn: Int?
): Parcelable

@Parcelize
data class GetBeniPrjRoomResult(
    @SerializedName("voicef_yn") var voicefYn: Int?
): Parcelable