package com.fityatra.utils

import com.fityatra.models.ApiResponse
import com.fityatra.models.Exercise
import com.fityatra.models.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)
data class SendOtpRequest(val email: String)
data class VerifyOtpRequest(val email: String, val otp: String)
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val image: String
)


data class ProfileData(
    val age: Number,
    val height: Number,
    val weight: Number,
    val gender: String,
    val postureProblems: String
)
data class UserInfo(
    val user: User,
    val age: String,
    val height: String,
    val weight: String,
    val gender: String,
    val postureProblems: String
)


data class ImageUploadResponse(val imagePath: String)

interface ApiServices {

    @Headers("Content-Type: application/json")
    @POST("/api/getuser")
    suspend fun getUser(@Header("Authorization") authToken: String): Response<User>

    @Headers("Content-Type: application/json")
    @POST("/api/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/api/createuser")
    suspend fun createUser(@Body request: CreateUserRequest): ApiResponse<String>

    @Multipart
    @POST("/api/upload/imageUpload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUploadResponse>


    @Headers("Content-Type: application/json")
    @POST("/api/addinfo/userinfo/")
    suspend fun saveProfile(@Header("Authorization") authToken: String, @Body profileData: ProfileData): Response<Unit>
    @Headers("Content-Type: application/json")
    @GET("/api/addinfo/userinfo/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): Response<UserInfo>

    @Headers("Content-Type: application/json")
    @GET("/api/exercises")
    suspend fun getExercises(): Response<List<Exercise>>

}
