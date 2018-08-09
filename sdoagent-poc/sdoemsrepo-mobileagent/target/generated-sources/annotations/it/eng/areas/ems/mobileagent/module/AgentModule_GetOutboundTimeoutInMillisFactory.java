package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetOutboundTimeoutInMillisFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetOutboundTimeoutInMillisFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getOutboundTimeoutInMillis(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetOutboundTimeoutInMillisFactory create(AgentModule module) {
    return new AgentModule_GetOutboundTimeoutInMillisFactory(module);
  }

  public static String proxyGetOutboundTimeoutInMillis(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getOutboundTimeoutInMillis(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
