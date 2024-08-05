package kr.ac.beni.beniprj.retrofit

import kr.ac.beni.beniprj.Const
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    fun createLiveDataRetrofitUnsafe(url: String): Retrofit? {

//        val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
//            .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
//            .cipherSuites(
//                TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
//                TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
//                TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
//            )
//            .build()


        //val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        val httpClient: OkHttpClient.Builder = TrustOkHttpClientUtil.getUnsafeOkHttpClient()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
//            .connectionSpecs(listOf(spec))
//            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
//            val original = chain.request()
//            val request = original.newBuilder()
//                .header("Content-Type", "application/json")
//                .addHeader("Connection", "close")
//                .method(original.method, original.body)
//                .build()
//            chain.proceed(request)
//        })
        if (Const.DEBUG_MODE || Const.TEST_MODE) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
        }
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(LiveDataCallAdapterFactory())
//            .client(getUnsafeOkHttpClient().build())
            .client(httpClient.build())
            .build()
    }
}