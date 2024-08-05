package kr.ac.beni.beniprj.retrofit

import com.google.gson.annotations.SerializedName
import kr.ac.beni.beniprj.util.CommonUtils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ApiResponse<T> {
    var exceptionMessage: String = ""
    private var mResponse : Response<T>? = null
    var hasException : Boolean = false
    var code: Int = 0

    constructor(error: Throwable){
        hasException = true
        exceptionMessage = error.localizedMessage?.toString() ?: error.message.toString()
    }

    constructor(response: Response<T>?){
        mResponse = response
        hasException = false
        isSuccess()
    }

    fun isSuccess():Boolean{
        return if(mResponse != null){
            val c = mResponse?.code()
            if(c != null) code = c
            mResponse?.isSuccessful == true && mResponse?.body() != null
        }else{
            false
        }
    }

    fun getErrorBody(): ErrorBody?{
        if(mResponse == null){
            return null
        }else{
            val errorBody:String? = mResponse?.errorBody()?.string()
            CommonUtils.commonErrLog("errorBody => $errorBody" + " " + mResponse?.code())
            if(errorBody!=null){
                return try {
                    val jsonObject = JSONObject(errorBody)
                    val type:String? = jsonObject.getString("type")
                    val message:String? = jsonObject.getString("message")
                    ErrorBody(type,message)
                }catch (e: JSONException){
                    hasException = true
                    CommonUtils.commonErrLog("errorBody JSONException ==> " + e.localizedMessage)
                    null
                }
            }
            return null
        }
    }

    fun getResponse(): Response<T>?{
        return mResponse
    }

    data class ErrorBody(
        @SerializedName("type") var type: String?,
        @SerializedName("message") var message: String?
    )
}