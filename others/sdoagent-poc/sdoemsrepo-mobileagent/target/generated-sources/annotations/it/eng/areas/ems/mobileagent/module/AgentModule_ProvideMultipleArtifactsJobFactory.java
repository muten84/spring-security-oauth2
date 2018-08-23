package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.job.MultipleArtifactsJob;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideMultipleArtifactsJobFactory
    implements Factory<MultipleArtifactsJob> {
  private final AgentModule module;

  private final Provider<String> groupIdProvider;

  private final Provider<String> localStorePathProvider;

  private final Provider<ArtifactsManager> artifactsManagerProvider;

  private final Provider<JsonStoreService> jsonStoreServiceProvider;

  private final Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider;

  public AgentModule_ProvideMultipleArtifactsJobFactory(
      AgentModule module,
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<ArtifactsManager> artifactsManagerProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider,
      Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider) {
    this.module = module;
    this.groupIdProvider = groupIdProvider;
    this.localStorePathProvider = localStorePathProvider;
    this.artifactsManagerProvider = artifactsManagerProvider;
    this.jsonStoreServiceProvider = jsonStoreServiceProvider;
    this.artifactsManagerStateServiceProvider = artifactsManagerStateServiceProvider;
  }

  @Override
  public MultipleArtifactsJob get() {
    return Preconditions.checkNotNull(
        module.provideMultipleArtifactsJob(
            groupIdProvider.get(),
            localStorePathProvider.get(),
            artifactsManagerProvider.get(),
            jsonStoreServiceProvider.get(),
            artifactsManagerStateServiceProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideMultipleArtifactsJobFactory create(
      AgentModule module,
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<ArtifactsManager> artifactsManagerProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider,
      Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider) {
    return new AgentModule_ProvideMultipleArtifactsJobFactory(
        module,
        groupIdProvider,
        localStorePathProvider,
        artifactsManagerProvider,
        jsonStoreServiceProvider,
        artifactsManagerStateServiceProvider);
  }

  public static MultipleArtifactsJob proxyProvideMultipleArtifactsJob(
      AgentModule instance,
      String groupId,
      String localStorePath,
      ArtifactsManager artifactsManager,
      JsonStoreService jsonStoreService,
      ArtifactsManagerStateService artifactsManagerStateService) {
    return Preconditions.checkNotNull(
        instance.provideMultipleArtifactsJob(
            groupId,
            localStorePath,
            artifactsManager,
            jsonStoreService,
            artifactsManagerStateService),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
