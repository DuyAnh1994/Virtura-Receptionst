package ai.ftech.config.retrofit

import ai.ftech.vituralreceptionist.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getClient(baseURL: String): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        else interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.NONE }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}
