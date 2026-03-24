package com.sunsafe.app.di;

import com.sunsafe.app.data.local.SunSafeDatabase;
import com.sunsafe.app.data.local.dao.SkinPhotoDao;
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
public final class DatabaseModule_ProvideSkinPhotoDaoFactory implements Factory<SkinPhotoDao> {
  private final Provider<SunSafeDatabase> dbProvider;

  public DatabaseModule_ProvideSkinPhotoDaoFactory(Provider<SunSafeDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SkinPhotoDao get() {
    return provideSkinPhotoDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideSkinPhotoDaoFactory create(
      Provider<SunSafeDatabase> dbProvider) {
    return new DatabaseModule_ProvideSkinPhotoDaoFactory(dbProvider);
  }

  public static SkinPhotoDao provideSkinPhotoDao(SunSafeDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSkinPhotoDao(db));
  }
}
