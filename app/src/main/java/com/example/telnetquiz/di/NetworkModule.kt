package com.example.telnetquiz.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.telnetquiz.BuildConfig
import com.example.telnetquiz.data.local.TokenManager
import com.example.telnetquiz.data.remote.api.TelNetQuizApi
import com.example.telnetquiz.data.remote.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "telnetquiz_prefs")
private const val TIMEOUT: Long = 7;
private const val HTTP_TAG = "TelNetQuizHttp"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> Log.d(HTTP_TAG, message) }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    @Named("apiKey")
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("x-api-key", BuildConfig.API_KEY)
                .header("Content-Type", "application/json")
                .build()
            Log.d(HTTP_TAG, "--> ${request.method} ${request.url}")
            try {
                val response = chain.proceed(request)
                Log.d(HTTP_TAG, "<-- ${response.code} ${request.method} ${request.url}")
                response
            } catch (e: Exception) {
                Log.e(HTTP_TAG, "xx ${request.method} ${request.url} failed: ${e.javaClass.simpleName}: ${e.message}")
                throw e
            }
        }
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
        return Interceptor { chain ->
            val token = runBlocking { tokenManager.authToken.first() }
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            if (!token.isNullOrEmpty()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    @Named("refreshClient")
    fun provideRefreshOkHttpClient(
        @Named("apiKey") apiKeyInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("refreshApi")
    fun provideRefreshApi(@Named("refreshClient") okHttpClient: OkHttpClient): TelNetQuizApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TelNetQuizApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("apiKey") apiKeyInterceptor: Interceptor,
        @Named("auth") authInterceptor: Interceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        Log.i(HTTP_TAG, "Retrofit baseUrl=${BuildConfig.BASE_URL} apiKey=${if (BuildConfig.API_KEY.isBlank()) "MISSING" else "set(${BuildConfig.API_KEY.length} chars)"} timeoutSec=$TIMEOUT")
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTelNetQuizApi(retrofit: Retrofit): TelNetQuizApi {
        return retrofit.create(TelNetQuizApi::class.java)
    }
}
