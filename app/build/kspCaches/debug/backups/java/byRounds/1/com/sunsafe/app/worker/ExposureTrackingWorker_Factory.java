package com.sunsafe.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.sunsafe.app.domain.repository.ExposureRepository;
import com.sunsafe.app.domain.repository.LocationRepository;
import com.sunsafe.app.domain.repository.SettingsRepository;
import com.sunsafe.app.domain.repository.UserRepository;
import com.sunsafe.app.domain.repository.UvRepository;
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase;
import com.sunsafe.app.notification.SunSafeNotificationManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ExposureTrackingWorker_Factory {
  private final Provider<ExposureRepository> exposureRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<LocationRepository> locationRepositoryProvider;

  private final Provider<UvRepository> uvRepositoryProvider;

  private final Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider;

  private final Provider<SunSafeNotificationManager> notificationManagerProvider;

  public ExposureTrackingWorker_Factory(Provider<ExposureRepository> exposureRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<UvRepository> uvRepositoryProvider,
      Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    this.exposureRepositoryProvider = exposureRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.locationRepositoryProvider = locationRepositoryProvider;
    this.uvRepositoryProvider = uvRepositoryProvider;
    this.calculateRemainingExposureProvider = calculateRemainingExposureProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public ExposureTrackingWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, exposureRepositoryProvider.get(), settingsRepositoryProvider.get(), userRepositoryProvider.get(), locationRepositoryProvider.get(), uvRepositoryProvider.get(), calculateRemainingExposureProvider.get(), notificationManagerProvider.get());
  }

  public static ExposureTrackingWorker_Factory create(
      Provider<ExposureRepository> exposureRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<UvRepository> uvRepositoryProvider,
      Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    return new ExposureTrackingWorker_Factory(exposureRepositoryProvider, settingsRepositoryProvider, userRepositoryProvider, locationRepositoryProvider, uvRepositoryProvider, calculateRemainingExposureProvider, notificationManagerProvider);
  }

  public static ExposureTrackingWorker newInstance(Context context, WorkerParameters params,
      ExposureRepository exposureRepository, SettingsRepository settingsRepository,
      UserRepository userRepository, LocationRepository locationRepository,
      UvRepository uvRepository, CalculateRemainingExposureUseCase calculateRemainingExposure,
      SunSafeNotificationManager notificationManager) {
    return new ExposureTrackingWorker(context, params, exposureRepository, settingsRepository, userRepository, locationRepository, uvRepository, calculateRemainingExposure, notificationManager);
  }
}
