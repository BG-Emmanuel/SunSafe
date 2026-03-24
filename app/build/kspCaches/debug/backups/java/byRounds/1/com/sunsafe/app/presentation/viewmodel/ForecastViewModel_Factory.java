package com.sunsafe.app.presentation.viewmodel;

import com.sunsafe.app.domain.repository.LocationRepository;
import com.sunsafe.app.domain.repository.UvRepository;
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
public final class ForecastViewModel_Factory implements Factory<ForecastViewModel> {
  private final Provider<UvRepository> uvRepositoryProvider;

  private final Provider<LocationRepository> locationRepositoryProvider;

  public ForecastViewModel_Factory(Provider<UvRepository> uvRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider) {
    this.uvRepositoryProvider = uvRepositoryProvider;
    this.locationRepositoryProvider = locationRepositoryProvider;
  }

  @Override
  public ForecastViewModel get() {
    return newInstance(uvRepositoryProvider.get(), locationRepositoryProvider.get());
  }

  public static ForecastViewModel_Factory create(Provider<UvRepository> uvRepositoryProvider,
      Provider<LocationRepository> locationRepositoryProvider) {
    return new ForecastViewModel_Factory(uvRepositoryProvider, locationRepositoryProvider);
  }

  public static ForecastViewModel newInstance(UvRepository uvRepository,
      LocationRepository locationRepository) {
    return new ForecastViewModel(uvRepository, locationRepository);
  }
}
