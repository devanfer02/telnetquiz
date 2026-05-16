package com.example.telnetquiz.di

import android.content.Context
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "telnetquiz_prefs")
private const val TIMEOUT: Long = 7;

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
    @Named("apiKey")
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("x-api-key", BuildConfig.API_KEY)
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(request)
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
        @Named("apiKey") apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
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
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
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
