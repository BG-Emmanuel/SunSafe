package com.sunsafe.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.sunsafe.app.domain.repository.LocationRepository;
import com.sunsafe.app.domain.repository.SettingsRepository;
import com.sunsafe.app.domain.repository.UvRepository;
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
public final class UvDataRefreshWorker_Factory {
  private final Provider<UvRepository> uvRepositoryProvider;

  private final Provider<LocationRepository> locationRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<SunSafeNotificationManager> notificationManagerProvider;

  public UvDataRefreshWorker_Factory(Provider<UvRepository> uvRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    this.uvRepositoryProvider = uvRepositoryProvider;
    this.locationRepositoryProvider = locationRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public UvDataRefreshWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, uvRepositoryProvider.get(), locationRepositoryProvider.get(), settingsRepositoryProvider.get(), notificationManagerProvider.get());
  }

  public static UvDataRefreshWorker_Factory create(Provider<UvRepository> uvRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    return new UvDataRefreshWorker_Factory(uvRepositoryProvider, locationRepositoryProvider, settingsRepositoryProvider, notificationManagerProvider);
  }

  public static UvDataRefreshWorker newInstance(Context context, WorkerParameters params,
      UvRepository uvRepository, LocationRepository locationRepository,
      SettingsRepository settingsRepository, SunSafeNotificationManager notificationManager) {
    return new UvDataRefreshWorker(context, params, uvRepository, locationRepository, settingsRepository, notificationManager);
  }
}
