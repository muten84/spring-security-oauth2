package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideDocumentStoreFactory implements Factory<DocumentStore> {
  private final AgentModule module;

  public AgentModule_ProvideDocumentStoreFactory(AgentModule module) {
    this.module = module;
  }

  @Override
  public DocumentStore get() {
    return Preconditions.checkNotNull(
        module.provideDocumentStore(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideDocumentStoreFactory create(AgentModule module) {
    return new AgentModule_ProvideDocumentStoreFactory(module);
  }

  public static DocumentStore proxyProvideDocumentStore(AgentModule instance) {
    return Preconditions.checkNotNull(
        instance.provideDocumentStore(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
