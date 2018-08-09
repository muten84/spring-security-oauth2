package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideArtifactsManagerStateServiceFactory
    implements Factory<ArtifactsManagerStateService> {
  private static final AgentModule_ProvideArtifactsManagerStateServiceFactory INSTANCE =
      new AgentModule_ProvideArtifactsManagerStateServiceFactory();

  @Override
  public ArtifactsManagerStateService get() {
    return Preconditions.checkNotNull(
        AgentModule.provideArtifactsManagerStateService(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideArtifactsManagerStateServiceFactory create() {
    return INSTANCE;
  }

  public static ArtifactsManagerStateService proxyProvideArtifactsManagerStateService() {
    return Preconditions.checkNotNull(
        AgentModule.provideArtifactsManagerStateService(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
