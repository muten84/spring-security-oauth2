package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetAgentServerRootFolderFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetAgentServerRootFolderFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getAgentServerRootFolder(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetAgentServerRootFolderFactory create(AgentModule module) {
    return new AgentModule_GetAgentServerRootFolderFactory(module);
  }

  public static String proxyGetAgentServerRootFolder(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getAgentServerRootFolder(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
