package com.sunsafe.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class UvDataRefreshWorker_AssistedFactory_Impl implements UvDataRefreshWorker_AssistedFactory {
  private final UvDataRefreshWorker_Factory delegateFactory;

  UvDataRefreshWorker_AssistedFactory_Impl(UvDataRefreshWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public UvDataRefreshWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<UvDataRefreshWorker_AssistedFactory> create(
      UvDataRefreshWorker_Factory delegateFactory) {
    return InstanceFactory.create(new UvDataRefreshWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<UvDataRefreshWorker_AssistedFactory> createFactoryProvider(
      UvDataRefreshWorker_Factory delegateFactory) {
    return InstanceFactory.create(new UvDataRefreshWorker_AssistedFactory_Impl(delegateFactory));
  }
}
