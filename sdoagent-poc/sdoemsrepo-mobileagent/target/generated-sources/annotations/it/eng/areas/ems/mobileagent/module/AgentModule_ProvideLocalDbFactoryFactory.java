package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.db.PerstLookupFactory;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideLocalDbFactoryFactory implements Factory<PerstLookupFactory> {
  private final AgentModule module;

  public AgentModule_ProvideLocalDbFactoryFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public PerstLookupFactory get() {
    return Preconditions.checkNotNull(
        module.provideLocalDbFactory(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideLocalDbFactoryFactory create(AgentModule module) {
    return new AgentModule_ProvideLocalDbFactoryFactory(module);
  }

  public static PerstLookupFactory proxyProvideLocalDbFactory(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.provideLocalDbFactory(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
