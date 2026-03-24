package com.sunsafe.app.data.repository;

import com.sunsafe.app.data.local.dao.ExposureDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ExposureRepositoryImpl_Factory implements Factory<ExposureRepositoryImpl> {
  private final Provider<ExposureDao> exposureDaoProvider;

  public ExposureRepositoryImpl_Factory(Provider<ExposureDao> exposureDaoProvider) {
    this.exposureDaoProvider = exposureDaoProvider;
  }

  @Override
  public ExposureRepositoryImpl get() {
    return newInstance(exposureDaoProvider.get());
  }

  public static ExposureRepositoryImpl_Factory create(Provider<ExposureDao> exposureDaoProvider) {
    return new ExposureRepositoryImpl_Factory(exposureDaoProvider);
  }

  public static ExposureRepositoryImpl newInstance(ExposureDao exposureDao) {
    return new ExposureRepositoryImpl(exposureDao);
  }
}
