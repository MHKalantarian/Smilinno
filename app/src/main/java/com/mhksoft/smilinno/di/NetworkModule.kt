package com.mhksoft.smilinno.di

import android.content.Context
import coil.ImageLoader
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mhksoft.smilinno.BuildConfig
import com.mhksoft.smilinno.data.remote.BlogService
import com.mhksoft.smilinno.data.remote.SliderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient { okHttpClient }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        objectMapper: ObjectMapper
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://94.101.179.76:4005/api/v1/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideMapper(): ObjectMapper {
        val mapper = ObjectMapper()

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(1024)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )

        return mapper
    }

    // Services
    @Provides
    @Singleton
    fun provideSliderService(retrofit: Retrofit): SliderService {
        return retrofit.create(SliderService::class.java)
    }

    @Provides
    @Singleton
    fun provideBlogService(retrofit: Retrofit): BlogService {
        return retrofit.create(BlogService::class.java)
    }
}
