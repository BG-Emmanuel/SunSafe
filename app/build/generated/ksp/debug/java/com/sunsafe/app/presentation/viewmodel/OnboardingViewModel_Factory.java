package com.sunsafe.app.presentation.viewmodel;

import com.sunsafe.app.domain.repository.SettingsRepository;
import com.sunsafe.app.domain.repository.UserRepository;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public OnboardingViewModel_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(userRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static OnboardingViewModel_Factory create(Provider<UserRepository> userRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new OnboardingViewModel_Factory(userRepositoryProvider, settingsRepositoryProvider);
  }

  public static OnboardingViewModel newInstance(UserRepository userRepository,
      SettingsRepository settingsRepository) {
    return new OnboardingViewModel(userRepository, settingsRepository);
  }
}
