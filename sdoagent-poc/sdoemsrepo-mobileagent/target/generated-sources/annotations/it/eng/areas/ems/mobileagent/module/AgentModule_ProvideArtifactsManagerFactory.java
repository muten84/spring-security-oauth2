package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideArtifactsManagerFactory implements Factory<ArtifactsManager> {
  private final Provider<ArtifactsManagerService> serviceProvider;

  private final Provider<JsonStoreService> jsonStoreServiceProvider;

  public AgentModule_ProvideArtifactsManagerFactory(
      Provider<ArtifactsManagerService> serviceProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider) {
    this.serviceProvider = serviceProvider;
    this.jsonStoreServiceProvider = jsonStoreServiceProvider;
  }

  @Override
  public ArtifactsManager get() {
    return Preconditions.checkNotNull(
        AgentModule.provideArtifactsManager(serviceProvider.get(), jsonStoreServiceProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideArtifactsManagerFactory create(
      Provider<ArtifactsManagerService> serviceProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider) {
    return new AgentModule_ProvideArtifactsManagerFactory(
        serviceProvider, jsonStoreServiceProvider);
  }

  public static ArtifactsManager proxyProvideArtifactsManager(
      ArtifactsManagerService service, JsonStoreService jsonStoreService) {
    return Preconditions.checkNotNull(
        AgentModule.provideArtifactsManager(service, jsonStoreService),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
