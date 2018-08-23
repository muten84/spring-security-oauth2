package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideJsonStoreServiceFactory implements Factory<JsonStoreService> {
  private final Provider<String> storePathProvider;

  private final Provider<String> groupIdProvider;

  private final Provider<JsonMapper> mapperProvider;

  public AgentModule_ProvideJsonStoreServiceFactory(
      Provider<String> storePathProvider,
      Provider<String> groupIdProvider,
      Provider<JsonMapper> mapperProvider) {
    this.storePathProvider = storePathProvider;
    this.groupIdProvider = groupIdProvider;
    this.mapperProvider = mapperProvider;
  }

  @Override
  public JsonStoreService get() {
    return Preconditions.checkNotNull(
        AgentModule.provideJsonStoreService(
            storePathProvider.get(), groupIdProvider.get(), mapperProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideJsonStoreServiceFactory create(
      Provider<String> storePathProvider,
      Provider<String> groupIdProvider,
      Provider<JsonMapper> mapperProvider) {
    return new AgentModule_ProvideJsonStoreServiceFactory(
        storePathProvider, groupIdProvider, mapperProvider);
  }

  public static JsonStoreService proxyProvideJsonStoreService(
      String storePath, String groupId, JsonMapper mapper) {
    return Preconditions.checkNotNull(
        AgentModule.provideJsonStoreService(storePath, groupId, mapper),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
