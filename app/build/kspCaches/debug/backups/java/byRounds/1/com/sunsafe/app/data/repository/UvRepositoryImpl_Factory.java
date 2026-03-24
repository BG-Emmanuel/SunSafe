package com.sunsafe.app.data.repository;

import com.sunsafe.app.data.remote.api.OpenUvApi;
import com.sunsafe.app.data.remote.api.OpenWeatherApi;
import com.sunsafe.app.domain.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UvRepositoryImpl_Factory implements Factory<UvRepositoryImpl> {
  private final Provider<OpenUvApi> openUvApiProvider;

  private final Provider<OpenWeatherApi> owmApiProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public UvRepositoryImpl_Factory(Provider<OpenUvApi> openUvApiProvider,
      Provider<OpenWeatherApi> owmApiProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.openUvApiProvider = openUvApiProvider;
    this.owmApiProvider = owmApiProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public UvRepositoryImpl get() {
    return newInstance(openUvApiProvider.get(), owmApiProvider.get(), settingsRepositoryProvider.get());
  }

  public static UvRepositoryImpl_Factory create(Provider<OpenUvApi> openUvApiProvider,
      Provider<OpenWeatherApi> owmApiProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new UvRepositoryImpl_Factory(openUvApiProvider, owmApiProvider, settingsRepositoryProvider);
  }

  public static UvRepositoryImpl newInstance(OpenUvApi openUvApi, OpenWeatherApi owmApi,
      SettingsRepository settingsRepository) {
    return new UvRepositoryImpl(openUvApi, owmApi, settingsRepository);
  }
}
