package com.sunsafe.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.squareup.moshi.Moshi;
import com.sunsafe.app.data.local.SunSafeDatabase;
import com.sunsafe.app.data.local.dao.ExposureDao;
import com.sunsafe.app.data.local.dao.UserDao;
import com.sunsafe.app.data.remote.api.OpenUvApi;
import com.sunsafe.app.data.remote.api.OpenWeatherApi;
import com.sunsafe.app.data.repository.ExposureRepositoryImpl;
import com.sunsafe.app.data.repository.LocationRepositoryImpl;
import com.sunsafe.app.data.repository.SettingsRepositoryImpl;
import com.sunsafe.app.data.repository.UserRepositoryImpl;
import com.sunsafe.app.data.repository.UvRepositoryImpl;
import com.sunsafe.app.di.DatabaseModule_ProvideDataStoreFactory;
import com.sunsafe.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.sunsafe.app.di.DatabaseModule_ProvideExposureDaoFactory;
import com.sunsafe.app.di.DatabaseModule_ProvideUserDaoFactory;
import com.sunsafe.app.di.NetworkModule_ProvideMoshiFactory;
import com.sunsafe.app.di.NetworkModule_ProvideOkHttpClientFactory;
import com.sunsafe.app.di.NetworkModule_ProvideOpenUvApiFactory;
import com.sunsafe.app.di.NetworkModule_ProvideOpenUvRetrofitFactory;
import com.sunsafe.app.di.NetworkModule_ProvideOwmApiFactory;
import com.sunsafe.app.di.NetworkModule_ProvideOwmRetrofitFactory;
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase;
import com.sunsafe.app.domain.usecase.CalculateSafeExposureUseCase;
import com.sunsafe.app.notification.SunSafeNotificationManager;
import com.sunsafe.app.presentation.viewmodel.DashboardViewModel;
import com.sunsafe.app.presentation.viewmodel.DashboardViewModel_HiltModules;
import com.sunsafe.app.presentation.viewmodel.ForecastViewModel;
import com.sunsafe.app.presentation.viewmodel.ForecastViewModel_HiltModules;
import com.sunsafe.app.presentation.viewmodel.HistoryViewModel;
import com.sunsafe.app.presentation.viewmodel.HistoryViewModel_HiltModules;
import com.sunsafe.app.presentation.viewmodel.MainViewModel;
import com.sunsafe.app.presentation.viewmodel.MainViewModel_HiltModules;
import com.sunsafe.app.presentation.viewmodel.OnboardingViewModel;
import com.sunsafe.app.presentation.viewmodel.OnboardingViewModel_HiltModules;
import com.sunsafe.app.presentation.viewmodel.SettingsViewModel;
import com.sunsafe.app.presentation.viewmodel.SettingsViewModel_HiltModules;
import com.sunsafe.app.service.ExposureTrackingService;
import com.sunsafe.app.service.ExposureTrackingService_MembersInjector;
import com.sunsafe.app.worker.ExposureTrackingWorker;
import com.sunsafe.app.worker.ExposureTrackingWorker_AssistedFactory;
import com.sunsafe.app.worker.ReminderWorker;
import com.sunsafe.app.worker.ReminderWorker_AssistedFactory;
import com.sunsafe.app.worker.UvDataRefreshWorker;
import com.sunsafe.app.worker.UvDataRefreshWorker_AssistedFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerSunSafeApplication_HiltComponents_SingletonC {
  private DaggerSunSafeApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public SunSafeApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements SunSafeApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements SunSafeApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements SunSafeApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements SunSafeApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements SunSafeApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements SunSafeApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements SunSafeApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public SunSafeApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends SunSafeApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends SunSafeApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends SunSafeApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends SunSafeApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_ForecastViewModel, ForecastViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_HistoryViewModel, HistoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_MainViewModel, MainViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectNotificationManager(instance, singletonCImpl.sunSafeNotificationManagerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_sunsafe_app_presentation_viewmodel_ForecastViewModel = "com.sunsafe.app.presentation.viewmodel.ForecastViewModel";

      static String com_sunsafe_app_presentation_viewmodel_DashboardViewModel = "com.sunsafe.app.presentation.viewmodel.DashboardViewModel";

      static String com_sunsafe_app_presentation_viewmodel_MainViewModel = "com.sunsafe.app.presentation.viewmodel.MainViewModel";

      static String com_sunsafe_app_presentation_viewmodel_OnboardingViewModel = "com.sunsafe.app.presentation.viewmodel.OnboardingViewModel";

      static String com_sunsafe_app_presentation_viewmodel_SettingsViewModel = "com.sunsafe.app.presentation.viewmodel.SettingsViewModel";

      static String com_sunsafe_app_presentation_viewmodel_HistoryViewModel = "com.sunsafe.app.presentation.viewmodel.HistoryViewModel";

      @KeepFieldType
      ForecastViewModel com_sunsafe_app_presentation_viewmodel_ForecastViewModel2;

      @KeepFieldType
      DashboardViewModel com_sunsafe_app_presentation_viewmodel_DashboardViewModel2;

      @KeepFieldType
      MainViewModel com_sunsafe_app_presentation_viewmodel_MainViewModel2;

      @KeepFieldType
      OnboardingViewModel com_sunsafe_app_presentation_viewmodel_OnboardingViewModel2;

      @KeepFieldType
      SettingsViewModel com_sunsafe_app_presentation_viewmodel_SettingsViewModel2;

      @KeepFieldType
      HistoryViewModel com_sunsafe_app_presentation_viewmodel_HistoryViewModel2;
    }
  }

  private static final class ViewModelCImpl extends SunSafeApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<ForecastViewModel> forecastViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.forecastViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_ForecastViewModel, ((Provider) forecastViewModelProvider)).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_HistoryViewModel, ((Provider) historyViewModelProvider)).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_MainViewModel, ((Provider) mainViewModelProvider)).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_sunsafe_app_presentation_viewmodel_SettingsViewModel, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_sunsafe_app_presentation_viewmodel_OnboardingViewModel = "com.sunsafe.app.presentation.viewmodel.OnboardingViewModel";

      static String com_sunsafe_app_presentation_viewmodel_MainViewModel = "com.sunsafe.app.presentation.viewmodel.MainViewModel";

      static String com_sunsafe_app_presentation_viewmodel_DashboardViewModel = "com.sunsafe.app.presentation.viewmodel.DashboardViewModel";

      static String com_sunsafe_app_presentation_viewmodel_SettingsViewModel = "com.sunsafe.app.presentation.viewmodel.SettingsViewModel";

      static String com_sunsafe_app_presentation_viewmodel_ForecastViewModel = "com.sunsafe.app.presentation.viewmodel.ForecastViewModel";

      static String com_sunsafe_app_presentation_viewmodel_HistoryViewModel = "com.sunsafe.app.presentation.viewmodel.HistoryViewModel";

      @KeepFieldType
      OnboardingViewModel com_sunsafe_app_presentation_viewmodel_OnboardingViewModel2;

      @KeepFieldType
      MainViewModel com_sunsafe_app_presentation_viewmodel_MainViewModel2;

      @KeepFieldType
      DashboardViewModel com_sunsafe_app_presentation_viewmodel_DashboardViewModel2;

      @KeepFieldType
      SettingsViewModel com_sunsafe_app_presentation_viewmodel_SettingsViewModel2;

      @KeepFieldType
      ForecastViewModel com_sunsafe_app_presentation_viewmodel_ForecastViewModel2;

      @KeepFieldType
      HistoryViewModel com_sunsafe_app_presentation_viewmodel_HistoryViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.sunsafe.app.presentation.viewmodel.DashboardViewModel 
          return (T) new DashboardViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.userRepositoryImplProvider.get(), singletonCImpl.uvRepositoryImplProvider.get(), singletonCImpl.exposureRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.locationRepositoryImplProvider.get(), singletonCImpl.calculateRemainingExposureUseCase());

          case 1: // com.sunsafe.app.presentation.viewmodel.ForecastViewModel 
          return (T) new ForecastViewModel(singletonCImpl.uvRepositoryImplProvider.get(), singletonCImpl.locationRepositoryImplProvider.get());

          case 2: // com.sunsafe.app.presentation.viewmodel.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.exposureRepositoryImplProvider.get());

          case 3: // com.sunsafe.app.presentation.viewmodel.MainViewModel 
          return (T) new MainViewModel(singletonCImpl.settingsRepositoryImplProvider.get());

          case 4: // com.sunsafe.app.presentation.viewmodel.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.userRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 5: // com.sunsafe.app.presentation.viewmodel.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.userRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends SunSafeApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends SunSafeApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectExposureTrackingService(ExposureTrackingService exposureTrackingService) {
      injectExposureTrackingService2(exposureTrackingService);
    }

    private ExposureTrackingService injectExposureTrackingService2(
        ExposureTrackingService instance) {
      ExposureTrackingService_MembersInjector.injectExposureRepository(instance, singletonCImpl.exposureRepositoryImplProvider.get());
      ExposureTrackingService_MembersInjector.injectSettingsRepository(instance, singletonCImpl.settingsRepositoryImplProvider.get());
      ExposureTrackingService_MembersInjector.injectUserRepository(instance, singletonCImpl.userRepositoryImplProvider.get());
      ExposureTrackingService_MembersInjector.injectLocationRepository(instance, singletonCImpl.locationRepositoryImplProvider.get());
      ExposureTrackingService_MembersInjector.injectUvRepository(instance, singletonCImpl.uvRepositoryImplProvider.get());
      ExposureTrackingService_MembersInjector.injectCalculateRemainingExposure(instance, singletonCImpl.calculateRemainingExposureUseCase());
      ExposureTrackingService_MembersInjector.injectNotificationManager(instance, singletonCImpl.sunSafeNotificationManagerProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends SunSafeApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SunSafeDatabase> provideDatabaseProvider;

    private Provider<ExposureRepositoryImpl> exposureRepositoryImplProvider;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<UserRepositoryImpl> userRepositoryImplProvider;

    private Provider<LocationRepositoryImpl> locationRepositoryImplProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Moshi> provideMoshiProvider;

    private Provider<Retrofit> provideOpenUvRetrofitProvider;

    private Provider<OpenUvApi> provideOpenUvApiProvider;

    private Provider<Retrofit> provideOwmRetrofitProvider;

    private Provider<OpenWeatherApi> provideOwmApiProvider;

    private Provider<UvRepositoryImpl> uvRepositoryImplProvider;

    private Provider<SunSafeNotificationManager> sunSafeNotificationManagerProvider;

    private Provider<ExposureTrackingWorker_AssistedFactory> exposureTrackingWorker_AssistedFactoryProvider;

    private Provider<ReminderWorker_AssistedFactory> reminderWorker_AssistedFactoryProvider;

    private Provider<UvDataRefreshWorker_AssistedFactory> uvDataRefreshWorker_AssistedFactoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ExposureDao exposureDao() {
      return DatabaseModule_ProvideExposureDaoFactory.provideExposureDao(provideDatabaseProvider.get());
    }

    private UserDao userDao() {
      return DatabaseModule_ProvideUserDaoFactory.provideUserDao(provideDatabaseProvider.get());
    }

    private CalculateRemainingExposureUseCase calculateRemainingExposureUseCase() {
      return new CalculateRemainingExposureUseCase(new CalculateSafeExposureUseCase());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return MapBuilder.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>newMapBuilder(3).put("com.sunsafe.app.worker.ExposureTrackingWorker", ((Provider) exposureTrackingWorker_AssistedFactoryProvider)).put("com.sunsafe.app.worker.ReminderWorker", ((Provider) reminderWorker_AssistedFactoryProvider)).put("com.sunsafe.app.worker.UvDataRefreshWorker", ((Provider) uvDataRefreshWorker_AssistedFactoryProvider)).build();
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<SunSafeDatabase>(singletonCImpl, 2));
      this.exposureRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ExposureRepositoryImpl>(singletonCImpl, 1));
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 4));
      this.settingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepositoryImpl>(singletonCImpl, 3));
      this.userRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserRepositoryImpl>(singletonCImpl, 5));
      this.locationRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<LocationRepositoryImpl>(singletonCImpl, 6));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 10));
      this.provideMoshiProvider = DoubleCheck.provider(new SwitchingProvider<Moshi>(singletonCImpl, 11));
      this.provideOpenUvRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 9));
      this.provideOpenUvApiProvider = DoubleCheck.provider(new SwitchingProvider<OpenUvApi>(singletonCImpl, 8));
      this.provideOwmRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 13));
      this.provideOwmApiProvider = DoubleCheck.provider(new SwitchingProvider<OpenWeatherApi>(singletonCImpl, 12));
      this.uvRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UvRepositoryImpl>(singletonCImpl, 7));
      this.sunSafeNotificationManagerProvider = DoubleCheck.provider(new SwitchingProvider<SunSafeNotificationManager>(singletonCImpl, 14));
      this.exposureTrackingWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<ExposureTrackingWorker_AssistedFactory>(singletonCImpl, 0));
      this.reminderWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<ReminderWorker_AssistedFactory>(singletonCImpl, 15));
      this.uvDataRefreshWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<UvDataRefreshWorker_AssistedFactory>(singletonCImpl, 16));
    }

    @Override
    public void injectSunSafeApplication(SunSafeApplication sunSafeApplication) {
      injectSunSafeApplication2(sunSafeApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private SunSafeApplication injectSunSafeApplication2(SunSafeApplication instance) {
      SunSafeApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.sunsafe.app.worker.ExposureTrackingWorker_AssistedFactory 
          return (T) new ExposureTrackingWorker_AssistedFactory() {
            @Override
            public ExposureTrackingWorker create(Context context, WorkerParameters params) {
              return new ExposureTrackingWorker(context, params, singletonCImpl.exposureRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.userRepositoryImplProvider.get(), singletonCImpl.locationRepositoryImplProvider.get(), singletonCImpl.uvRepositoryImplProvider.get(), singletonCImpl.calculateRemainingExposureUseCase(), singletonCImpl.sunSafeNotificationManagerProvider.get());
            }
          };

          case 1: // com.sunsafe.app.data.repository.ExposureRepositoryImpl 
          return (T) new ExposureRepositoryImpl(singletonCImpl.exposureDao());

          case 2: // com.sunsafe.app.data.local.SunSafeDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.sunsafe.app.data.repository.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl(singletonCImpl.provideDataStoreProvider.get());

          case 4: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) DatabaseModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.sunsafe.app.data.repository.UserRepositoryImpl 
          return (T) new UserRepositoryImpl(singletonCImpl.userDao());

          case 6: // com.sunsafe.app.data.repository.LocationRepositoryImpl 
          return (T) new LocationRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.sunsafe.app.data.repository.UvRepositoryImpl 
          return (T) new UvRepositoryImpl(singletonCImpl.provideOpenUvApiProvider.get(), singletonCImpl.provideOwmApiProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get());

          case 8: // com.sunsafe.app.data.remote.api.OpenUvApi 
          return (T) NetworkModule_ProvideOpenUvApiFactory.provideOpenUvApi(singletonCImpl.provideOpenUvRetrofitProvider.get());

          case 9: // @javax.inject.Named("openuv") retrofit2.Retrofit 
          return (T) NetworkModule_ProvideOpenUvRetrofitFactory.provideOpenUvRetrofit(singletonCImpl.provideOkHttpClientProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 10: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 11: // com.squareup.moshi.Moshi 
          return (T) NetworkModule_ProvideMoshiFactory.provideMoshi();

          case 12: // com.sunsafe.app.data.remote.api.OpenWeatherApi 
          return (T) NetworkModule_ProvideOwmApiFactory.provideOwmApi(singletonCImpl.provideOwmRetrofitProvider.get());

          case 13: // @javax.inject.Named("owm") retrofit2.Retrofit 
          return (T) NetworkModule_ProvideOwmRetrofitFactory.provideOwmRetrofit(singletonCImpl.provideOkHttpClientProvider.get(), singletonCImpl.provideMoshiProvider.get());

          case 14: // com.sunsafe.app.notification.SunSafeNotificationManager 
          return (T) new SunSafeNotificationManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 15: // com.sunsafe.app.worker.ReminderWorker_AssistedFactory 
          return (T) new ReminderWorker_AssistedFactory() {
            @Override
            public ReminderWorker create(Context context2, WorkerParameters params2) {
              return new ReminderWorker(context2, params2, singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.userRepositoryImplProvider.get(), singletonCImpl.sunSafeNotificationManagerProvider.get());
            }
          };

          case 16: // com.sunsafe.app.worker.UvDataRefreshWorker_AssistedFactory 
          return (T) new UvDataRefreshWorker_AssistedFactory() {
            @Override
            public UvDataRefreshWorker create(Context context3, WorkerParameters params3) {
              return new UvDataRefreshWorker(context3, params3, singletonCImpl.uvRepositoryImplProvider.get(), singletonCImpl.locationRepositoryImplProvider.get(), singletonCImpl.settingsRepositoryImplProvider.get(), singletonCImpl.sunSafeNotificationManagerProvider.get());
            }
          };

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
