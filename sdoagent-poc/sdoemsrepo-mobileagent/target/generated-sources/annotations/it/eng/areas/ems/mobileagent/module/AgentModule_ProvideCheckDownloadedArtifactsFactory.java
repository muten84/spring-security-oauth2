package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.job.CheckDownloadedArtifacts;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideCheckDownloadedArtifactsFactory
    implements Factory<CheckDownloadedArtifacts> {
  private final AgentModule module;

  private final Provider<String> groupIdProvider;

  private final Provider<String> localStorePathProvider;

  private final Provider<LocalArtifactsService> localArtifactsServiceProvider;

  private final Provider<JsonMapper> mapperProvider;

  public AgentModule_ProvideCheckDownloadedArtifactsFactory(
      AgentModule module,
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<LocalArtifactsService> localArtifactsServiceProvider,
      Provider<JsonMapper> mapperProvider) {
    this.module = module;
    this.groupIdProvider = groupIdProvider;
    this.localStorePathProvider = localStorePathProvider;
    this.localArtifactsServiceProvider = localArtifactsServiceProvider;
    this.mapperProvider = mapperProvider;
  }

  @Override
  public CheckDownloadedArtifacts get() {
    return Preconditions.checkNotNull(
        module.provideCheckDownloadedArtifacts(
            groupIdProvider.get(),
            localStorePathProvider.get(),
            localArtifactsServiceProvider.get(),
            mapperProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideCheckDownloadedArtifactsFactory create(
      AgentModule module,
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<LocalArtifactsService> localArtifactsServiceProvider,
      Provider<JsonMapper> mapperProvider) {
    return new AgentModule_ProvideCheckDownloadedArtifactsFactory(
        module,
        groupIdProvider,
        localStorePathProvider,
        localArtifactsServiceProvider,
        mapperProvider);
  }

  public static CheckDownloadedArtifacts proxyProvideCheckDownloadedArtifacts(
      AgentModule instance,
      String groupId,
      String localStorePath,
      LocalArtifactsService localArtifactsService,
      JsonMapper mapper) {
    return Preconditions.checkNotNull(
        instance.provideCheckDownloadedArtifacts(
            groupId, localStorePath, localArtifactsService, mapper),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
