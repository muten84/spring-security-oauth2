package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_GetStorePathFactory implements Factory<String> {
  private final AgentModule module;

  public AgentModule_GetStorePathFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.getStorePath(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_GetStorePathFactory create(AgentModule module) {
    return new AgentModule_GetStorePathFactory(module);
  }

  public static String proxyGetStorePath(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.getStorePath(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
