package kr.ac.beni.beniprj

class Const {
    companion object{
        const val COMMON_TAG = "VFAPPLOG"
        const val PORTSIP_LICENSE_KEY = "4ANDcx5FOTRCRkU2OENFNEZDRTcyMDcyRTIyRUJBQzk5NEI4MUA5QUNFNEIzQjc4Qjg2Mzc4RDZDMURDRDFBQkYxOTAyOUBEMjgwRjY4Qjk5NDE3OUE0RDExRkNGNjVBQkNGMzZENEA1QzlBRkVBQjREQzJFMUM4NjE1NUJCRDQ3MjVBMzdFOQ"

        const val TEST_MODE                     = true
        const val DEBUG_MODE                    = false

        //const val BASE_URL                      = "https://abrain.hoseo.ac.kr"
        const val BASE_URL                      = "http://localhost:5000"

        const val PRIVACY_URL                   = "http://www.hoseo.ac.kr/Home/Contents.mbz?action=MAPP_1708310223"

        const val FOREGROUND_ID                 = 4857
    }

    enum class Api {
        SetNewChat,
        SetSendChat,
        GetBeniPrj,
        SetNewUserInfo,
    }
}

data class UserInfo(var name: String, var age: Int, var sex:Int, var diseaseNum:Int, var diseaseLevel:Int)