package kr.ac.beni.beniprj.model

import android.os.Parcelable
import android.webkit.JsResult
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.google.gson.Gson
import kotlinx.parcelize.RawValue

@Parcelize
data class NewUserInfoResult(
    @SerializedName("name") var name: String?,
    @SerializedName("age") var age: Int?,
    @SerializedName("sex") var sex: Int?,
    @SerializedName("disease_num") var diseaseNum: Int?,
    @SerializedName("disease_level") var diseaseLevel: Int?
): Parcelable



data class GetMedicalHistInfo(
    @SerializedName("id") val id: String,
    @SerializedName("medicalMainName") val medicalMainName: String,
    @SerializedName("medicalMainCode") val medicalMainCode: String,
    @SerializedName("medicalSubName") val medicalSubName: String,
    @SerializedName("medicalSubCode") val medicalSubCode: String,
    @SerializedName("operationDuration") val operationDuration: String,
    @SerializedName("operationHz") val operationHz: String,
    @SerializedName("operationRate") val operationRate: String,
    @SerializedName("medicalDate") val medicalDate: String,
    @SerializedName("fk_user_id") val fkUserId: Int,
)


@Parcelize
data class GetMedicalHistResult(
    @SerializedName("medical_hist_list")  val medicalHistList: List<MedicalHistItem>?,
    @SerializedName("result") val result: String
): Parcelable

@Parcelize
data class MedicalHistItem(
    @SerializedName("id") val id: Int,
    @SerializedName("medicalMainName") val medicalMainName: String,
    @SerializedName("medicalMainCode") val medicalMainCode: String,
    @SerializedName("medicalSubName") val medicalSubName: String,
    @SerializedName("medicalSubCode") val medicalSubCode: String,
    @SerializedName("operationDuration") val operationDuration: String,
    @SerializedName("operationHz") val operationHz: String,
    @SerializedName("operationRate") val operationRate: String,
    @SerializedName("medicalDate") val medicalDate: String,
    @SerializedName("fk_user_id") val fkUserId: Int,
): Parcelable

@Parcelize
data class SetMedicalHistResult(
    @SerializedName("id") val id: Int,
): Parcelable

data class MedicalHistInfo(
    var id: Int,
    var medicalMainName: String,
    var medicalMainCode: String,
    var medicalSubName: String,
    var medicalSubCode: String,
    var medicalDate: String,
    var operationDuration:String,
    var operationHz:String,
    var operationRate:String
)