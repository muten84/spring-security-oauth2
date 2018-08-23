package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetArtifactsHttpBaseUrlFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetArtifactsHttpBaseUrlFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getArtifactsHttpBaseUrl(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetArtifactsHttpBaseUrlFactory create(AgentModule module) {
    return new AgentModule_GetArtifactsHttpBaseUrlFactory(module);
  }

  public static String proxyGetArtifactsHttpBaseUrl(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getArtifactsHttpBaseUrl(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
