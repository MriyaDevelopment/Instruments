package com.decorator1889.instruments.Network

import com.decorator1889.instruments.Network.response.*
import com.decorator1889.instruments.Network.response.RemoveLikeResponse
import com.decorator1889.instruments.util.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiNetwork {

    @POST(getCategories)
    fun getCategoriesAsync(): Deferred<CategoriesResponse>

    @POST(getSubCategories)
    fun getSubCategoriesAsync(): Deferred<SubCategoriesResponse>

    @FormUrlEncoded
    @POST(getInstrumentsByType)
    fun getInstrumentsByTypeAsync(
        @Field("user_token") user_token: String?,
        @Field("type") type: String?,
    ): Deferred<InstrumentsResponse>

    @FormUrlEncoded
    @POST(getSurgeryInstrumentsByType)
    fun getSurgeryInstrumentsByTypeAsync(
        @Field("user_token") user_token: String?,
        @Field("type") type: String?,
    ): Deferred<InstrumentsResponse>

    @FormUrlEncoded
    @POST(register)
    fun registerAsync(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") password_confirmation: String?,
    ): Deferred<RegisterResponse>

    @FormUrlEncoded
    @POST(login)
    fun loginAsync(
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): Deferred<LoginResponse>

    @FormUrlEncoded
    @POST(getProfileData)
    fun getProfileDataAsync(
        @Field("user_token") user_token: String?,
    ): Deferred<ProfileResponse>

    @FormUrlEncoded
    @POST(setLike)
    fun setLikeAsync(
        @Field("user_token") user_token: String?,
        @Field("instrument_id") instrument_id: Long?,
        @Field("is_surgery") is_surgery: Boolean?,
    ): Deferred<LikeResponse>

    @FormUrlEncoded
    @POST(removeLike)
    fun removeLikeAsync(
        @Field("user_token") user_token: String?,
        @Field("instrument_id") instrument_id: Long?,
        @Field("is_surgery") is_surgery: Boolean?,
    ): Deferred<RemoveLikeResponse>

    @POST(getLevels)
    fun getLevelsAsync(): Deferred<LevelsResponse>

    @FormUrlEncoded
    @POST(getFavourites)
    fun getFavoritesAsync(
        @Field("user_token") user_token: String?
    ): Deferred<FavoritesResponse>

    @FormUrlEncoded
    @POST(getResult)
    fun getResultAsync(
        @Field("user_token") user_token: String?
    ): Deferred<ResultResponse>

    @POST(getTypes)
    fun getTypesAsync(): Deferred<TypesResponse>

    @FormUrlEncoded
    @POST(getQuestionByTypeAndLevel)
    fun getQuestionByTypeAndLevelAsync(
        @Field("type") type: String?,
        @Field("level") level: Long?
    ): Deferred<QuestionResponse>

    @FormUrlEncoded
    @POST(setResult)
    fun setResultAsync(
        @Field("user_token") user_token: String?,
        @Field("level") level: Long?,
        @Field("categories") categories: String?,
        @Field("number_of_correct_answers") number_of_correct_answers: Long?,
        @Field("number_of_questions") number_of_questions: Long?,
        @Field("questions") questions: String
    ): Deferred<SentResultResponse>

    @FormUrlEncoded
    @POST(getLastTest)
    fun getLastTestAsync(
        @Field("user_token") user_token: String?
    ): Deferred<QuestionResponse>

    @GET(send)
    fun sendMailAsync(
        @Query("user_token") user_token: String?,
        @Query("email") email: String?,
    ): Deferred<SendMainResponse>

    companion object {
        const val baseUrl = "http://ovz2.j04713753.pqr7m.vps.myjino.ru/"
        const val getCategories = "public/api/getCategories"
        const val getSubCategories = "public/api/getSubCategories"
        const val getInstrumentsByType = "public/api/getInstrumentsByType"
        const val getSurgeryInstrumentsByType = "public/api/getSurgeryInstrumentsByType"
        const val register = "public/api/register"
        const val login = "public/api/login"
        const val getProfileData = "public/api/getProfileData"
        const val setLike = "public/api/setLike"
        const val removeLike = "public/api/removeLike"
        const val getLevels = "public/api/getLevels"
        const val getFavourites = "public/api/getFavourites"
        const val getResult = "public/api/getResult"
        const val getTypes = "public/api/getTypes"
        const val getQuestionByTypeAndLevel = "public/api/getQuestionByTypeAndLevel"
        const val setResult = "public/api/setResult"
        const val getLastTest = "public/api/getLastTest"
        const val send = "public/mail/send"

        private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val API: ApiNetwork by lazy {
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.callTimeout(60, TimeUnit.SECONDS)
            okHttpClient.connectTimeout(20, TimeUnit.SECONDS)
            okHttpClient.readTimeout(20, TimeUnit.SECONDS)
            okHttpClient.writeTimeout(20, TimeUnit.SECONDS)
            val retrofit = Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
            retrofit.create(ApiNetwork::class.java)
        }
    }
}
