package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetMqttUrlFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetMqttUrlFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getMqttUrl(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetMqttUrlFactory create(AgentModule module) {
    return new AgentModule_GetMqttUrlFactory(module);
  }

  public static String proxyGetMqttUrl(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getMqttUrl(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
