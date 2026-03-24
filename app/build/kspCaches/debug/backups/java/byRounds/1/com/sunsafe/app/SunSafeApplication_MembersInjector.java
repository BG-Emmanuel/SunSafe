package com.sunsafe.app;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class SunSafeApplication_MembersInjector implements MembersInjector<SunSafeApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public SunSafeApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<SunSafeApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new SunSafeApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(SunSafeApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.sunsafe.app.SunSafeApplication.workerFactory")
  public static void injectWorkerFactory(SunSafeApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
