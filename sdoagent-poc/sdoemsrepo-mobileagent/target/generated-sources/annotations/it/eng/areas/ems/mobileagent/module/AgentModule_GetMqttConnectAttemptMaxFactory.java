package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttConnectAttemptMaxFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttConnectAttemptMaxFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttConnectAttemptMax(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttConnectAttemptMaxFactory create(AgentModule module) {
    return new AgentModule_GetMqttConnectAttemptMaxFactory(module);
  }

  public static String proxyGetMqttConnectAttemptMax(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttConnectAttemptMax(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
