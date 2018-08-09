package it.eng.areas.ems.mobileagent.artifacts.service;

import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class LocalArtifactsService_Factory implements Factory<LocalArtifactsService> {
  private final Provider<String> localStorePathProvider;

  private final Provider<JsonStoreService> storeServiceProvider;

  private final Provider<JsonMapper> mapperProvider;

  public LocalArtifactsService_Factory(
      Provider<String> localStorePathProvider,
      Provider<JsonStoreService> storeServiceProvider,
      Provider<JsonMapper> mapperProvider) {
    this.localStorePathProvider = localStorePathProvider;
    this.storeServiceProvider = storeServiceProvider;
    this.mapperProvider = mapperProvider;
  }

  @Override
  public LocalArtifactsService get() {
    return new LocalArtifactsService(
        localStorePathProvider.get(), storeServiceProvider.get(), mapperProvider.get());
  }

  public static LocalArtifactsService_Factory create(
      Provider<String> localStorePathProvider,
      Provider<JsonStoreService> storeServiceProvider,
      Provider<JsonMapper> mapperProvider) {
    return new LocalArtifactsService_Factory(
        localStorePathProvider, storeServiceProvider, mapperProvider);
  }
}
