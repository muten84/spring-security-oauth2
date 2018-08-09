package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttReconnectDelayFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttReconnectDelayFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttReconnectDelay(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttReconnectDelayFactory create(AgentModule module) {
    return new AgentModule_GetMqttReconnectDelayFactory(module);
  }

  public static String proxyGetMqttReconnectDelay(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttReconnectDelay(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
