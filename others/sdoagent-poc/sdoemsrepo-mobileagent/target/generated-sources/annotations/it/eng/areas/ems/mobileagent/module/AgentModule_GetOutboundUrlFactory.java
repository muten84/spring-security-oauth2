package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetOutboundUrlFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetOutboundUrlFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getOutboundUrl(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetOutboundUrlFactory create(AgentModule module) {
    return new AgentModule_GetOutboundUrlFactory(module);
  }

  public static String proxyGetOutboundUrl(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getOutboundUrl(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
