package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetOutboundReadTimeoutFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetOutboundReadTimeoutFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getOutboundReadTimeout(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetOutboundReadTimeoutFactory create(AgentModule module) {
    return new AgentModule_GetOutboundReadTimeoutFactory(module);
  }

  public static String proxyGetOutboundReadTimeout(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getOutboundReadTimeout(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
