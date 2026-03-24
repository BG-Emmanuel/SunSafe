package com.sunsafe.app.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculateSafeExposureUseCase_Factory implements Factory<CalculateSafeExposureUseCase> {
  @Override
  public CalculateSafeExposureUseCase get() {
    return newInstance();
  }

  public static CalculateSafeExposureUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateSafeExposureUseCase newInstance() {
    return new CalculateSafeExposureUseCase();
  }

  private static final class InstanceHolder {
    private static final CalculateSafeExposureUseCase_Factory INSTANCE = new CalculateSafeExposureUseCase_Factory();
  }
}
