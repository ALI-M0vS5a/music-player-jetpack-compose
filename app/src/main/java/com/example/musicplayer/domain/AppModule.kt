package com.example.musicplayer.domain

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.R
import com.example.musicplayer.data.remote.LyricsApi
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)

    @Provides
    @Singleton
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_baseline_error_24)
            .error(R.drawable.ic_baseline_error_24)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Provides
    @Singleton
    fun providesAuthClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        .addInterceptor(
            object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val response = chain.proceed(request = chain.request())
                    val body = response.peekBody(2048).string()
                    try {
                        if (response.isSuccessful) {
                            if (body.contains("status")) {
                                val jsonObject = JSONObject(body)
                                val status = jsonObject.optInt("status")
                                Timber.d("Status = $status")
                                if (status == 0) {
                                    jsonObject.getJSONObject("data").optString("error_code")
                                    return chain.proceed(chain.request())
                                }
                            } else {
                                Timber.d("Body is not containing status, might be not valid GSON")
                            }
                        }
                        Timber.d("End")

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Timber.d("Error")
                    }
                    return response
                }
            }
        )
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun providesLyricsApi(retrofit: Retrofit): LyricsApi = retrofit.create(LyricsApi::class.java)

}