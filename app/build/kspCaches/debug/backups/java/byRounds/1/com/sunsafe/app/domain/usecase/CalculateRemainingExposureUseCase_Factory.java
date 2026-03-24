package com.sunsafe.app.domain.usecase;

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
public final class CalculateRemainingExposureUseCase_Factory implements Factory<CalculateRemainingExposureUseCase> {
  private final Provider<CalculateSafeExposureUseCase> calculateSafeExposureProvider;

  public CalculateRemainingExposureUseCase_Factory(
      Provider<CalculateSafeExposureUseCase> calculateSafeExposureProvider) {
    this.calculateSafeExposureProvider = calculateSafeExposureProvider;
  }

  @Override
  public CalculateRemainingExposureUseCase get() {
    return newInstance(calculateSafeExposureProvider.get());
  }

  public static CalculateRemainingExposureUseCase_Factory create(
      Provider<CalculateSafeExposureUseCase> calculateSafeExposureProvider) {
    return new CalculateRemainingExposureUseCase_Factory(calculateSafeExposureProvider);
  }

  public static CalculateRemainingExposureUseCase newInstance(
      CalculateSafeExposureUseCase calculateSafeExposure) {
    return new CalculateRemainingExposureUseCase(calculateSafeExposure);
  }
}
