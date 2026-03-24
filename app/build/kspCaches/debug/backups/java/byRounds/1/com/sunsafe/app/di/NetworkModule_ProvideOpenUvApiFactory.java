package com.sunsafe.app.di;

import com.sunsafe.app.data.remote.api.OpenUvApi;
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
public final class NetworkModule_ProvideOpenUvApiFactory implements Factory<OpenUvApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideOpenUvApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OpenUvApi get() {
    return provideOpenUvApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideOpenUvApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideOpenUvApiFactory(retrofitProvider);
  }

  public static OpenUvApi provideOpenUvApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOpenUvApi(retrofit));
  }
}
