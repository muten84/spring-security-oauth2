package it.eng.areas.ems.mobileagent.artifacts.util;

import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DimmerUtils_Factory implements Factory<DimmerUtils> {
  private final Provider<JsonMapper> mapperProvider;

  public DimmerUtils_Factory(Provider<JsonMapper> mapperProvider) {
    this.mapperProvider = mapperProvider;
  }

  @Override
  public DimmerUtils get() {
    return new DimmerUtils(mapperProvider.get());
  }

  public static DimmerUtils_Factory create(Provider<JsonMapper> mapperProvider) {
    return new DimmerUtils_Factory(mapperProvider);
  }
}
