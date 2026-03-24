package com.sunsafe.app.data.repository;

import com.sunsafe.app.data.local.dao.SkinPhotoDao;
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
public final class PhotoRepositoryImpl_Factory implements Factory<PhotoRepositoryImpl> {
  private final Provider<SkinPhotoDao> photoDaoProvider;

  public PhotoRepositoryImpl_Factory(Provider<SkinPhotoDao> photoDaoProvider) {
    this.photoDaoProvider = photoDaoProvider;
  }

  @Override
  public PhotoRepositoryImpl get() {
    return newInstance(photoDaoProvider.get());
  }

  public static PhotoRepositoryImpl_Factory create(Provider<SkinPhotoDao> photoDaoProvider) {
    return new PhotoRepositoryImpl_Factory(photoDaoProvider);
  }

  public static PhotoRepositoryImpl newInstance(SkinPhotoDao photoDao) {
    return new PhotoRepositoryImpl(photoDao);
  }
}
