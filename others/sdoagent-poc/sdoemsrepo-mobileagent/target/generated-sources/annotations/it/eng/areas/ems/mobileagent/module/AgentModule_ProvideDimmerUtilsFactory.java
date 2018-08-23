package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.util.DimmerUtils;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideDimmerUtilsFactory implements Factory<DimmerUtils> {
  private static final AgentModule_ProvideDimmerUtilsFactory INSTANCE =
      new AgentModule_ProvideDimmerUtilsFactory();

  @Override
  public DimmerUtils get() {
    return Preconditions.checkNotNull(
        AgentModule.provideDimmerUtils(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideDimmerUtilsFactory create() {
    return INSTANCE;
  }

  public static DimmerUtils proxyProvideDimmerUtils() {
    return Preconditions.checkNotNull(
        AgentModule.provideDimmerUtils(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
