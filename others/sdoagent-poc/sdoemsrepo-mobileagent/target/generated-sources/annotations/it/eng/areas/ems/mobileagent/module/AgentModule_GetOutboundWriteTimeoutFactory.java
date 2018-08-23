package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetOutboundWriteTimeoutFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetOutboundWriteTimeoutFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getOutboundWriteTimeout(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetOutboundWriteTimeoutFactory create(AgentModule module) {
    return new AgentModule_GetOutboundWriteTimeoutFactory(module);
  }

  public static String proxyGetOutboundWriteTimeout(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getOutboundWriteTimeout(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
