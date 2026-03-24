package com.sunsafe.app.worker;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = ExposureTrackingWorker.class
)
public interface ExposureTrackingWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.sunsafe.app.worker.ExposureTrackingWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      ExposureTrackingWorker_AssistedFactory factory);
}
