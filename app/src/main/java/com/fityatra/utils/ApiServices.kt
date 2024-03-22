package com.fityatra.utils

import com.fityatra.models.ApiResponse
import com.fityatra.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)
data class SendOtpRequest(val email: String)
data class VerifyOtpRequest(val email: String, val otp: String)
data class CreateUserRequest(val name: String, val email: String, val password: String, val image: String)
data class ImageUploadResponse(val imagePath: String)
data class GetUserRequest(val authToken: String)

interface ApiServices {

    @Headers("Content-Type: application/json")
    @POST("/api/getuser")
    suspend fun getUser(@Body request: GetUserRequest): Response<User>

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

}
