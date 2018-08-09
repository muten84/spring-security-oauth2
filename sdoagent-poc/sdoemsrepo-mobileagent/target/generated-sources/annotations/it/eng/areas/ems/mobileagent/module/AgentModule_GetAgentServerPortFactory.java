package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetAgentServerPortFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetAgentServerPortFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getAgentServerPort(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetAgentServerPortFactory create(AgentModule module) {
    return new AgentModule_GetAgentServerPortFactory(module);
  }

  public static String proxyGetAgentServerPort(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getAgentServerPort(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
