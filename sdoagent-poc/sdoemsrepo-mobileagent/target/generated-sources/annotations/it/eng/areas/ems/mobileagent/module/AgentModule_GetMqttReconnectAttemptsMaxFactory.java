package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttReconnectAttemptsMaxFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttReconnectAttemptsMaxFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttReconnectAttemptsMax(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttReconnectAttemptsMaxFactory create(AgentModule module) {
    return new AgentModule_GetMqttReconnectAttemptsMaxFactory(module);
  }

  public static String proxyGetMqttReconnectAttemptsMax(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttReconnectAttemptsMax(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
