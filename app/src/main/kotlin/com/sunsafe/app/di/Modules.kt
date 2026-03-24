package com.sunsafe.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sunsafe.app.BuildConfig
import com.sunsafe.app.data.local.SunSafeDatabase
import com.sunsafe.app.data.local.dao.*
import com.sunsafe.app.data.remote.api.*
import com.sunsafe.app.data.repository.*
import com.sunsafe.app.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

// DataStore extension
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sunsafe_settings")

// ─────────────────────────────────────────────────────────────────────────────
// Database Module
// ─────────────────────────────────────────────────────────────────────────────
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SunSafeDatabase =
        Room.databaseBuilder(context, SunSafeDatabase::class.java, SunSafeDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(db: SunSafeDatabase): UserDao = db.userDao()

    @Provides
    fun provideExposureDao(db: SunSafeDatabase): ExposureDao = db.exposureDao()

    @Provides
    fun provideSkinPhotoDao(db: SunSafeDatabase): SkinPhotoDao = db.skinPhotoDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}

// ─────────────────────────────────────────────────────────────────────────────
// Network Module
// ─────────────────────────────────────────────────────────────────────────────
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("openuv")
    fun provideOpenUvRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.OPENUV_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    @Named("owm")
    fun provideOwmRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.OWM_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideOpenUvApi(@Named("openuv") retrofit: Retrofit): OpenUvApi =
        retrofit.create(OpenUvApi::class.java)

    @Provides
    @Singleton
    fun provideOwmApi(@Named("owm") retrofit: Retrofit): OpenWeatherApi =
        retrofit.create(OpenWeatherApi::class.java)
}

// ─────────────────────────────────────────────────────────────────────────────
// Repository Module
// ─────────────────────────────────────────────────────────────────────────────
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindUvRepository(impl: UvRepositoryImpl): UvRepository

    @Binds
    @Singleton
    abstract fun bindExposureRepository(impl: ExposureRepositoryImpl): ExposureRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository
}
