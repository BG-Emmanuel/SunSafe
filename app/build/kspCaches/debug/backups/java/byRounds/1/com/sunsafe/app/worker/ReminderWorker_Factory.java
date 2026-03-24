package com.sunsafe.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.sunsafe.app.domain.repository.SettingsRepository;
import com.sunsafe.app.domain.repository.UserRepository;
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
public final class ReminderWorker_Factory {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<SunSafeNotificationManager> notificationManagerProvider;

  public ReminderWorker_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public ReminderWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, settingsRepositoryProvider.get(), userRepositoryProvider.get(), notificationManagerProvider.get());
  }

  public static ReminderWorker_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    return new ReminderWorker_Factory(settingsRepositoryProvider, userRepositoryProvider, notificationManagerProvider);
  }

  public static ReminderWorker newInstance(Context context, WorkerParameters params,
      SettingsRepository settingsRepository, UserRepository userRepository,
      SunSafeNotificationManager notificationManager) {
    return new ReminderWorker(context, params, settingsRepository, userRepository, notificationManager);
  }
}
