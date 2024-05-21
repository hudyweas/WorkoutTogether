

package com.hudyweas.workouttogether.model.service.module

import com.hudyweas.workouttogether.model.service.AccountService
import com.hudyweas.workouttogether.model.service.ConfigurationService
import com.hudyweas.workouttogether.model.service.LogService
import com.hudyweas.workouttogether.model.service.StorageService
import com.hudyweas.workouttogether.model.service.impl.AccountServiceImpl
import com.hudyweas.workouttogether.model.service.impl.ConfigurationServiceImpl
import com.hudyweas.workouttogether.model.service.impl.LogServiceImpl
import com.hudyweas.workouttogether.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

  @Provides
  fun provideHttpClient(): HttpClient {
    return HttpClient(Android) {
      install(JsonFeature) {
        serializer = KotlinxSerializer(Json {
          ignoreUnknownKeys = true
        })
      }
    }
  }


}