package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttKeepAliveFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttKeepAliveFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttKeepAlive(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttKeepAliveFactory create(AgentModule module) {
    return new AgentModule_GetMqttKeepAliveFactory(module);
  }

  public static String proxyGetMqttKeepAlive(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttKeepAlive(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
