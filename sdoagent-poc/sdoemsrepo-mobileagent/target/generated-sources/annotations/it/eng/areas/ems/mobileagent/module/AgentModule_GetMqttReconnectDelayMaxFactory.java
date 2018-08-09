package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttReconnectDelayMaxFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttReconnectDelayMaxFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttReconnectDelayMax(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttReconnectDelayMaxFactory create(AgentModule module) {
    return new AgentModule_GetMqttReconnectDelayMaxFactory(module);
  }

  public static String proxyGetMqttReconnectDelayMax(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttReconnectDelayMax(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
