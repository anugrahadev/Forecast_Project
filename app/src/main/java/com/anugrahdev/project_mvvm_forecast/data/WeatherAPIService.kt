package com.anugrahdev.project_mvvm_forecast.data

import com.anugrahdev.project_mvvm_forecast.data.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "976f9b626ce8237521ac8ab4e225a65d"
//http://api.weatherstack.com/current?access_key=976f9b626ce8237521ac8ab4e225a65d&query=london
interface WeatherAPIService {

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location:String

    ):Deferred<CurrentWeatherResponse>

    companion object {
        operator fun invoke() : WeatherAPIService{
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)


            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherAPIService::class.java)


        }
    }
}