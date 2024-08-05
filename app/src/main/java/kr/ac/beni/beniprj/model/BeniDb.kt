package kr.ac.beni.beniprj.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewUserInfoResult(
    @SerializedName("name") var name: String?,
    @SerializedName("age") var age: Int?,
    @SerializedName("sex") var sex: Int?,
    @SerializedName("disease_num") var diseaseNum: Int?,
    @SerializedName("disease_level") var diseaseLevel: Int?
): Parcelable
