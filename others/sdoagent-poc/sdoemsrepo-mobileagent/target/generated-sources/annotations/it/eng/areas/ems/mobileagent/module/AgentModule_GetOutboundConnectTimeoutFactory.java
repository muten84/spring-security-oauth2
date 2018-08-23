package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetOutboundConnectTimeoutFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetOutboundConnectTimeoutFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getOutboundConnectTimeout(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetOutboundConnectTimeoutFactory create(AgentModule module) {
    return new AgentModule_GetOutboundConnectTimeoutFactory(module);
  }

  public static String proxyGetOutboundConnectTimeout(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getOutboundConnectTimeout(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
