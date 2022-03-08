package com.decorator1889.instruments.Network

import android.annotation.SuppressLint
import androidx.preference.PreferenceManager
import com.decorator1889.instruments.App
import com.decorator1889.instruments.BuildConfig
import com.decorator1889.instruments.util.dataservices.AuthenticationService
import com.decorator1889.instruments.util.dataservices.PreferencesService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface ApiNetwork {

    companion object {
        const val RESPONSE_COOKIES_HEADER = "Set-cookie"
        const val REQUEST_COOKIES_HEADER = "cookie"
        const val PHPSESSID_HEADER = "PHPSESSID"
        const val baseUrl = ""

        private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val API: ApiNetwork by lazy {
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.callTimeout(60, TimeUnit.SECONDS)
            okHttpClient.connectTimeout(20, TimeUnit.SECONDS)
            okHttpClient.readTimeout(20, TimeUnit.SECONDS)
            okHttpClient.writeTimeout(20, TimeUnit.SECONDS)
            okHttpClient.addInterceptor(CookiesInterceptor())
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                    okHttpClient.addInterceptor(this)
                }
            }
            val retrofit = Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
            retrofit.create(ApiNetwork::class.java)
        }

        @SuppressLint("ApplySharedPref")
        fun resetCookies() {
            PreferencesService.cookies.remove()
        }
    }

    class CookiesInterceptor : Interceptor {
        @SuppressLint("ApplySharedPref")
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest: Request
            if (request.headers.size == 0 && AuthenticationService.isAuthorize().not()) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance())
                val cookies = PreferencesService.cookies()
                if (cookies.isEmpty()) {
                    chain.proceed(request).run {
                        var responseCookies = ""
                        headers(RESPONSE_COOKIES_HEADER).firstOrNull {
                            it.contains(PHPSESSID_HEADER)
                        }?.split(';')?.forEach {
                            if (it.contains(PHPSESSID_HEADER)) responseCookies = it
                        }
                        newRequest = request.newBuilder()
                            .addHeader(REQUEST_COOKIES_HEADER, responseCookies)
                            .build()
                        close()
                        PreferencesService.cookies insert responseCookies
                    }
                } else {
                    newRequest = request.newBuilder()
                        .addHeader(REQUEST_COOKIES_HEADER, cookies)
                        .build()
                }
                return chain.proceed(newRequest)
            }
            return chain.proceed(request)
        }

    }

    class HTTPAuthenticator : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request {
            return response.request.newBuilder()
                .header(
                    "Authorization",
                    Credentials.basic("", "")
                )
                .build()
        }
    }
}
