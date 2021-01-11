package com.devlab.rickandmortyapp.di

import android.util.Log
import com.devlab.rickandmortyapp.BuildConfig
import com.devlab.rickandmortyapp.repository.base.AppConfig
import com.devlab.rickandmortyapp.repository.services.IApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.e("OkHttp", it)
        })

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        builder.addInterceptor { chain ->
            val originalRequest = chain.request()

            val newRequestBuilder = originalRequest.newBuilder()

            newRequestBuilder
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("ClientVersion", BuildConfig.VERSION_NAME)
                .cacheControl(CacheControl.FORCE_NETWORK)

            return@addInterceptor chain.proceed(newRequestBuilder.build())
        }

        builder.addInterceptor(interceptor)
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.writeTimeout(30, TimeUnit.SECONDS)

        builder.hostnameVerifier { _, _ -> true }

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideApiService(appConfig: AppConfig, okHttpClient: OkHttpClient, gson: Gson): IApiService {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(appConfig.serviceUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        return retrofit.create(IApiService::class.java)
    }
}