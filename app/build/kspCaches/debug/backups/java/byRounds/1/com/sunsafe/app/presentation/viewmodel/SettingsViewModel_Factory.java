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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public SettingsViewModel_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(userRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<UserRepository> userRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new SettingsViewModel_Factory(userRepositoryProvider, settingsRepositoryProvider);
  }

  public static SettingsViewModel newInstance(UserRepository userRepository,
      SettingsRepository settingsRepository) {
    return new SettingsViewModel(userRepository, settingsRepository);
  }
}
