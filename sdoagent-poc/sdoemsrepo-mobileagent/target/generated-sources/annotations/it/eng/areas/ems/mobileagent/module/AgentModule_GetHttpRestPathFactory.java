package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetHttpRestPathFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetHttpRestPathFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getHttpRestPath(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetHttpRestPathFactory create(AgentModule module) {
    return new AgentModule_GetHttpRestPathFactory(module);
  }

  public static String proxyGetHttpRestPath(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getHttpRestPath(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
