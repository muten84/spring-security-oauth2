package it.eng.areas.ems.mobileagent.artifacts.job;

import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class MultipleArtifactsJob_Factory implements Factory<MultipleArtifactsJob> {
  private final Provider<String> groupIdProvider;

  private final Provider<String> localStorePathProvider;

  private final Provider<ArtifactsManager> artifactsManagerProvider;

  private final Provider<JsonStoreService> jsonStoreServiceProvider;

  private final Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider;

  public MultipleArtifactsJob_Factory(
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<ArtifactsManager> artifactsManagerProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider,
      Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider) {
    this.groupIdProvider = groupIdProvider;
    this.localStorePathProvider = localStorePathProvider;
    this.artifactsManagerProvider = artifactsManagerProvider;
    this.jsonStoreServiceProvider = jsonStoreServiceProvider;
    this.artifactsManagerStateServiceProvider = artifactsManagerStateServiceProvider;
  }

  @Override
  public MultipleArtifactsJob get() {
    return new MultipleArtifactsJob(
        groupIdProvider.get(),
        localStorePathProvider.get(),
        artifactsManagerProvider.get(),
        jsonStoreServiceProvider.get(),
        artifactsManagerStateServiceProvider.get());
  }

  public static MultipleArtifactsJob_Factory create(
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<ArtifactsManager> artifactsManagerProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider,
      Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider) {
    return new MultipleArtifactsJob_Factory(
        groupIdProvider,
        localStorePathProvider,
        artifactsManagerProvider,
        jsonStoreServiceProvider,
        artifactsManagerStateServiceProvider);
  }
}
