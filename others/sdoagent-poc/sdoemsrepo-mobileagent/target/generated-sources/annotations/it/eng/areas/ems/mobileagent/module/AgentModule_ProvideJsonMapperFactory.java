package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideJsonMapperFactory implements Factory<JsonMapper> {
  private static final AgentModule_ProvideJsonMapperFactory INSTANCE =
      new AgentModule_ProvideJsonMapperFactory();

  @Override
  public JsonMapper get() {
    return Preconditions.checkNotNull(
        AgentModule.provideJsonMapper(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideJsonMapperFactory create() {
    return INSTANCE;
  }

  public static JsonMapper proxyProvideJsonMapper() {
    return Preconditions.checkNotNull(
        AgentModule.provideJsonMapper(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
