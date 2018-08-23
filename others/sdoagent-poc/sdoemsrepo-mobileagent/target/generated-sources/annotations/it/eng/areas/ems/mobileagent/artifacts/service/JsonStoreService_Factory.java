package it.eng.areas.ems.mobileagent.artifacts.service;

import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class JsonStoreService_Factory implements Factory<JsonStoreService> {
  private final Provider<String> storePathProvider;

  private final Provider<String> groupIdProvider;

  private final Provider<JsonMapper> jsonMapperProvider;

  public JsonStoreService_Factory(
      Provider<String> storePathProvider,
      Provider<String> groupIdProvider,
      Provider<JsonMapper> jsonMapperProvider) {
    this.storePathProvider = storePathProvider;
    this.groupIdProvider = groupIdProvider;
    this.jsonMapperProvider = jsonMapperProvider;
  }

  @Override
  public JsonStoreService get() {
    return new JsonStoreService(
        storePathProvider.get(), groupIdProvider.get(), jsonMapperProvider.get());
  }

  public static JsonStoreService_Factory create(
      Provider<String> storePathProvider,
      Provider<String> groupIdProvider,
      Provider<JsonMapper> jsonMapperProvider) {
    return new JsonStoreService_Factory(storePathProvider, groupIdProvider, jsonMapperProvider);
  }
}
