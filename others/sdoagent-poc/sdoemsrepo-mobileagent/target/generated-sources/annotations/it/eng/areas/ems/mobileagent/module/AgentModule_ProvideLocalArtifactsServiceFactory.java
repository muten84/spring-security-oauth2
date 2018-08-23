package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideLocalArtifactsServiceFactory
    implements Factory<LocalArtifactsService> {
  private final Provider<String> localStorePathProvider;

  private final Provider<JsonStoreService> storeServiceProvider;

  private final Provider<JsonMapper> mapperProvider;

  public AgentModule_ProvideLocalArtifactsServiceFactory(
      Provider<String> localStorePathProvider,
      Provider<JsonStoreService> storeServiceProvider,
      Provider<JsonMapper> mapperProvider) {
    this.localStorePathProvider = localStorePathProvider;
    this.storeServiceProvider = storeServiceProvider;
    this.mapperProvider = mapperProvider;
  }

  @Override
  public LocalArtifactsService get() {
    return Preconditions.checkNotNull(
        AgentModule.provideLocalArtifactsService(
            localStorePathProvider.get(), storeServiceProvider.get(), mapperProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideLocalArtifactsServiceFactory create(
      Provider<String> localStorePathProvider,
      Provider<JsonStoreService> storeServiceProvider,
      Provider<JsonMapper> mapperProvider) {
    return new AgentModule_ProvideLocalArtifactsServiceFactory(
        localStorePathProvider, storeServiceProvider, mapperProvider);
  }

  public static LocalArtifactsService proxyProvideLocalArtifactsService(
      String localStorePath, JsonStoreService storeService, JsonMapper mapper) {
    return Preconditions.checkNotNull(
        AgentModule.provideLocalArtifactsService(localStorePath, storeService, mapper),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
