package com.sunsafe.app.service;

import com.sunsafe.app.domain.repository.ExposureRepository;
import com.sunsafe.app.domain.repository.LocationRepository;
import com.sunsafe.app.domain.repository.SettingsRepository;
import com.sunsafe.app.domain.repository.UserRepository;
import com.sunsafe.app.domain.repository.UvRepository;
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase;
import com.sunsafe.app.notification.SunSafeNotificationManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ExposureTrackingService_MembersInjector implements MembersInjector<ExposureTrackingService> {
  private final Provider<ExposureRepository> exposureRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<LocationRepository> locationRepositoryProvider;

  private final Provider<UvRepository> uvRepositoryProvider;

  private final Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider;

  private final Provider<SunSafeNotificationManager> notificationManagerProvider;

  public ExposureTrackingService_MembersInjector(
      Provider<ExposureRepository> exposureRepositoryProvider,
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

  public static MembersInjector<ExposureTrackingService> create(
      Provider<ExposureRepository> exposureRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<UvRepository> uvRepositoryProvider,
      Provider<CalculateRemainingExposureUseCase> calculateRemainingExposureProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    return new ExposureTrackingService_MembersInjector(exposureRepositoryProvider, settingsRepositoryProvider, userRepositoryProvider, locationRepositoryProvider, uvRepositoryProvider, calculateRemainingExposureProvider, notificationManagerProvider);
  }

  @Override
  public void injectMembers(ExposureTrackingService instance) {
    injectExposureRepository(instance, exposureRepositoryProvider.get());
    injectSettingsRepository(instance, settingsRepositoryProvider.get());
    injectUserRepository(instance, userRepositoryProvider.get());
    injectLocationRepository(instance, locationRepositoryProvider.get());
    injectUvRepository(instance, uvRepositoryProvider.get());
    injectCalculateRemainingExposure(instance, calculateRemainingExposureProvider.get());
    injectNotificationManager(instance, notificationManagerProvider.get());
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.exposureRepository")
  public static void injectExposureRepository(ExposureTrackingService instance,
      ExposureRepository exposureRepository) {
    instance.exposureRepository = exposureRepository;
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.settingsRepository")
  public static void injectSettingsRepository(ExposureTrackingService instance,
      SettingsRepository settingsRepository) {
    instance.settingsRepository = settingsRepository;
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.userRepository")
  public static void injectUserRepository(ExposureTrackingService instance,
      UserRepository userRepository) {
    instance.userRepository = userRepository;
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.locationRepository")
  public static void injectLocationRepository(ExposureTrackingService instance,
      LocationRepository locationRepository) {
    instance.locationRepository = locationRepository;
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.uvRepository")
  public static void injectUvRepository(ExposureTrackingService instance,
      UvRepository uvRepository) {
    instance.uvRepository = uvRepository;
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.calculateRemainingExposure")
  public static void injectCalculateRemainingExposure(ExposureTrackingService instance,
      CalculateRemainingExposureUseCase calculateRemainingExposure) {
    instance.calculateRemainingExposure = calculateRemainingExposure;
  }

  @InjectedFieldSignature("com.sunsafe.app.service.ExposureTrackingService.notificationManager")
  public static void injectNotificationManager(ExposureTrackingService instance,
      SunSafeNotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }
}
