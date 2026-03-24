package com.sunsafe.app.presentation.viewmodel;

import android.content.Context;
import com.sunsafe.app.domain.repository.ExposureRepository;
import com.sunsafe.app.domain.repository.LocationRepository;
import com.sunsafe.app.domain.repository.SettingsRepository;
import com.sunsafe.app.domain.repository.UserRepository;
import com.sunsafe.app.domain.repository.UvRepository;
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<UvRepository> uvRepositoryProvider;

  private final Provider<ExposureRepository> exposureRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<LocationRepository> locationRepositoryProvider;

  private final Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider;

  public DashboardViewModel_Factory(Provider<Context> contextProvider,
      Provider<UserRepository> userRepositoryProvider, Provider<UvRepository> uvRepositoryProvider,
      Provider<ExposureRepository> exposureRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider) {
    this.contextProvider = contextProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.uvRepositoryProvider = uvRepositoryProvider;
    this.exposureRepositoryProvider = exposureRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.locationRepositoryProvider = locationRepositoryProvider;
    this.calculateRemainingExposureProvider = calculateRemainingExposureProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(contextProvider.get(), userRepositoryProvider.get(), uvRepositoryProvider.get(), exposureRepositoryProvider.get(), settingsRepositoryProvider.get(), locationRepositoryProvider.get(), calculateRemainingExposureProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Context> contextProvider,
      Provider<UserRepository> userRepositoryProvider, Provider<UvRepository> uvRepositoryProvider,
      Provider<ExposureRepository> exposureRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider) {
    return new DashboardViewModel_Factory(contextProvider, userRepositoryProvider, uvRepositoryProvider, exposureRepositoryProvider, settingsRepositoryProvider, locationRepositoryProvider, calculateRemainingExposureProvider);
  }

  public static DashboardViewModel newInstance(Context context, UserRepository userRepository,
      UvRepository uvRepository, ExposureRepository exposureRepository,
      SettingsRepository settingsRepository, LocationRepository locationRepository,
      CalculateRemainingExposureUseCase calculateRemainingExposure) {
    return new DashboardViewModel(context, userRepository, uvRepository, exposureRepository, settingsRepository, locationRepository, calculateRemainingExposure);
  }
}
