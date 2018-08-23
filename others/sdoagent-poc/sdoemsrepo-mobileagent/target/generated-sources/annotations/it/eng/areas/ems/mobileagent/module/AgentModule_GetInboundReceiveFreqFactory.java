package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetInboundReceiveFreqFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetInboundReceiveFreqFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getInboundReceiveFreq(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetInboundReceiveFreqFactory create(AgentModule module) {
    return new AgentModule_GetInboundReceiveFreqFactory(module);
  }

  public static String proxyGetInboundReceiveFreq(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getInboundReceiveFreq(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
