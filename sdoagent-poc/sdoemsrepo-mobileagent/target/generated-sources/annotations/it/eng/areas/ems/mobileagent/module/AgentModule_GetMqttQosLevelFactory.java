package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttQosLevelFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttQosLevelFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttQosLevel(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttQosLevelFactory create(AgentModule module) {
    return new AgentModule_GetMqttQosLevelFactory(module);
  }

  public static String proxyGetMqttQosLevel(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttQosLevel(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
