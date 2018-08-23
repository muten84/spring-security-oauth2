package it.eng.areas.ems.mobileagent;

import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.job.CheckDownloadedArtifacts;
import it.eng.areas.ems.mobileagent.artifacts.job.MultipleArtifactsJob;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.http.AgentRestService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class Agent_Factory implements Factory<Agent> {
  private final Provider<AgentRestService> agentRestServiceProvider;

  private final Provider<String> externalStaticFileLocationProvider;

  private final Provider<String> portProvider;

  private final Provider<ArtifactsManagerStateService> stateServiceProvider;

  private final Provider<CheckDownloadedArtifacts> checkArtifactsProvider;

  private final Provider<MultipleArtifactsJob> checkMultiArtifactsProvider;

  private final Provider<JsonStoreService> jsonStoreServiceProvider;

  private final Provider<ArtifactsManager> artifactsManagerProvider;

  public Agent_Factory(
      Provider<AgentRestService> agentRestServiceProvider,
      Provider<String> externalStaticFileLocationProvider,
      Provider<String> portProvider,
      Provider<ArtifactsManagerStateService> stateServiceProvider,
      Provider<CheckDownloadedArtifacts> checkArtifactsProvider,
      Provider<MultipleArtifactsJob> checkMultiArtifactsProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider,
      Provider<ArtifactsManager> artifactsManagerProvider) {
    this.agentRestServiceProvider = agentRestServiceProvider;
    this.externalStaticFileLocationProvider = externalStaticFileLocationProvider;
    this.portProvider = portProvider;
    this.stateServiceProvider = stateServiceProvider;
    this.checkArtifactsProvider = checkArtifactsProvider;
    this.checkMultiArtifactsProvider = checkMultiArtifactsProvider;
    this.jsonStoreServiceProvider = jsonStoreServiceProvider;
    this.artifactsManagerProvider = artifactsManagerProvider;
  }

  @Override
  public Agent get() {
    return new Agent(
        DoubleCheck.lazy(agentRestServiceProvider),
        externalStaticFileLocationProvider.get(),
        portProvider.get(),
        stateServiceProvider.get(),
        DoubleCheck.lazy(checkArtifactsProvider),
        DoubleCheck.lazy(checkMultiArtifactsProvider),
        jsonStoreServiceProvider.get(),
        artifactsManagerProvider.get());
  }

  public static Agent_Factory create(
      Provider<AgentRestService> agentRestServiceProvider,
      Provider<String> externalStaticFileLocationProvider,
      Provider<String> portProvider,
      Provider<ArtifactsManagerStateService> stateServiceProvider,
      Provider<CheckDownloadedArtifacts> checkArtifactsProvider,
      Provider<MultipleArtifactsJob> checkMultiArtifactsProvider,
      Provider<JsonStoreService> jsonStoreServiceProvider,
      Provider<ArtifactsManager> artifactsManagerProvider) {
    return new Agent_Factory(
        agentRestServiceProvider,
        externalStaticFileLocationProvider,
        portProvider,
        stateServiceProvider,
        checkArtifactsProvider,
        checkMultiArtifactsProvider,
        jsonStoreServiceProvider,
        artifactsManagerProvider);
  }

  public static Agent newAgent(
      Lazy<AgentRestService> agentRestService,
      String externalStaticFileLocation,
      String port,
      ArtifactsManagerStateService stateService,
      Lazy<CheckDownloadedArtifacts> checkArtifacts,
      Lazy<MultipleArtifactsJob> checkMultiArtifacts,
      JsonStoreService jsonStoreService,
      ArtifactsManager artifactsManager) {
    return new Agent(
        agentRestService,
        externalStaticFileLocation,
        port,
        stateService,
        checkArtifacts,
        checkMultiArtifacts,
        jsonStoreService,
        artifactsManager);
  }
}
