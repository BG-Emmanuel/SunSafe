package com.sunsafe.app.di;

import com.sunsafe.app.data.local.SunSafeDatabase;
import com.sunsafe.app.data.local.dao.ExposureDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideExposureDaoFactory implements Factory<ExposureDao> {
  private final Provider<SunSafeDatabase> dbProvider;

  public DatabaseModule_ProvideExposureDaoFactory(Provider<SunSafeDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ExposureDao get() {
    return provideExposureDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideExposureDaoFactory create(
      Provider<SunSafeDatabase> dbProvider) {
    return new DatabaseModule_ProvideExposureDaoFactory(dbProvider);
  }

  public static ExposureDao provideExposureDao(SunSafeDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExposureDao(db));
  }
}
