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
public final class ExposureTrackingWorker_AssistedFactory_Impl implements ExposureTrackingWorker_AssistedFactory {
  private final ExposureTrackingWorker_Factory delegateFactory;

  ExposureTrackingWorker_AssistedFactory_Impl(ExposureTrackingWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public ExposureTrackingWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<ExposureTrackingWorker_AssistedFactory> create(
      ExposureTrackingWorker_Factory delegateFactory) {
    return InstanceFactory.create(new ExposureTrackingWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<ExposureTrackingWorker_AssistedFactory> createFactoryProvider(
      ExposureTrackingWorker_Factory delegateFactory) {
    return InstanceFactory.create(new ExposureTrackingWorker_AssistedFactory_Impl(delegateFactory));
  }
}
