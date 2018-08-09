package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetGroupIdFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetGroupIdFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getGroupId(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetGroupIdFactory create(AgentModule module) {
    return new AgentModule_GetGroupIdFactory(module);
  }

  public static String proxyGetGroupId(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getGroupId(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
