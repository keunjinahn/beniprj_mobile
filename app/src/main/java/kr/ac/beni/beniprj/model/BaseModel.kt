package kr.ac.beni.beniprj.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseModel(
    @SerializedName("type") var type: String,
    @SerializedName("message") var message: String,
    @SerializedName("args") var args: String
    ): Parcelable