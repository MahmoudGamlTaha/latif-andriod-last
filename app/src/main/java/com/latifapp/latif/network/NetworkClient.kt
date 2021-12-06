package com.example.postsapplication.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.utiles.Utiles
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkClient {
    val BASE_URL = "http://194.163.180.99:8070/"
    // val BASE_URL = "http://latifapp.herokuapp.com/"
     private val TIMEOUT_MIN = 2



    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
        return retrofit
    }

    @Provides
    @Singleton
    fun getNetworkService(retrofit: Retrofit): NetworkApis {
        return retrofit.create(NetworkApis::class.java)
    }

    @Provides
    @Singleton
    fun getUnsafeOkHttpClientAuth(appPrefsStorage: AppPrefsStorage): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            // Install the all_en-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all_en-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val interceptor1 = HttpLoggingInterceptor()
            interceptor1.setLevel(HttpLoggingInterceptor.Level.BODY)
            val interceptor =
                Interceptor { chain: Interceptor.Chain ->
                    var request: Request = chain.request()
                    val httpUrl: HttpUrl = request.url()
                    val url = httpUrl.newBuilder().build()
                    var token_:String?= ""
                    if(!AppPrefsStorage.token.isNullOrEmpty())
                        token_ ="Bearer ${AppPrefsStorage.token}"

                    request = request.newBuilder().addHeader("Authorization", token_).build()
                    chain.proceed(request)
                }
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(
                TIMEOUT_MIN.toLong(),
                TimeUnit.MINUTES
            )
            builder.readTimeout(
                TIMEOUT_MIN.toLong(),
                TimeUnit.MINUTES
            )
            builder.addInterceptor(interceptor).build()
            // for logged msgs
            builder.addInterceptor(interceptor1).build()
            builder.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            builder.build()
        } catch (e: Exception) {
            Utiles.log_D("NetworkClient", e.toString())
            throw RuntimeException(e)
        }
    }

}


