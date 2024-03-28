import android.util.Log
import com.fityatra.BuildConfig
import com.fityatra.utils.ApiServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = BuildConfig.API_URL

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()


    val apiService: ApiServices = retrofit.create(ApiServices::class.java)

    fun call() {
        Log.d("retrofit", retrofit.toString())
    }
}
