package com.sunsafe.app;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<SunSafeNotificationManager> notificationManagerProvider;

  public MainActivity_MembersInjector(
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    this.notificationManagerProvider = notificationManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<SunSafeNotificationManager> notificationManagerProvider) {
    return new MainActivity_MembersInjector(notificationManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectNotificationManager(instance, notificationManagerProvider.get());
  }

  @InjectedFieldSignature("com.sunsafe.app.MainActivity.notificationManager")
  public static void injectNotificationManager(MainActivity instance,
      SunSafeNotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }
}
