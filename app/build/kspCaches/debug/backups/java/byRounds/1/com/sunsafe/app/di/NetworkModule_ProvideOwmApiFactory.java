package com.sunsafe.app.di;

import com.sunsafe.app.data.remote.api.OpenWeatherApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class NetworkModule_ProvideOwmApiFactory implements Factory<OpenWeatherApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideOwmApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OpenWeatherApi get() {
    return provideOwmApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideOwmApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideOwmApiFactory(retrofitProvider);
  }

  public static OpenWeatherApi provideOwmApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOwmApi(retrofit));
  }
}
