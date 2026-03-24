package com.sunsafe.app.presentation.viewmodel;

import com.sunsafe.app.domain.repository.ExposureRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<ExposureRepository> exposureRepositoryProvider;

  public HistoryViewModel_Factory(Provider<ExposureRepository> exposureRepositoryProvider) {
    this.exposureRepositoryProvider = exposureRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(exposureRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<ExposureRepository> exposureRepositoryProvider) {
    return new HistoryViewModel_Factory(exposureRepositoryProvider);
  }

  public static HistoryViewModel newInstance(ExposureRepository exposureRepository) {
    return new HistoryViewModel(exposureRepository);
  }
}
