package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideArtifactsManagerServiceFactory
    implements Factory<ArtifactsManagerService> {
  private final Provider<String> baseUrlProvider;

  private final Provider<String> restPathProvider;

  public AgentModule_ProvideArtifactsManagerServiceFactory(
      Provider<String> baseUrlProvider, Provider<String> restPathProvider) {
    this.baseUrlProvider = baseUrlProvider;
    this.restPathProvider = restPathProvider;
  }

  @Override
  public ArtifactsManagerService get() {
    return Preconditions.checkNotNull(
        AgentModule.provideArtifactsManagerService(baseUrlProvider.get(), restPathProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideArtifactsManagerServiceFactory create(
      Provider<String> baseUrlProvider, Provider<String> restPathProvider) {
    return new AgentModule_ProvideArtifactsManagerServiceFactory(baseUrlProvider, restPathProvider);
  }

  public static ArtifactsManagerService proxyProvideArtifactsManagerService(
      String baseUrl, String restPath) {
    return Preconditions.checkNotNull(
        AgentModule.provideArtifactsManagerService(baseUrl, restPath),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
