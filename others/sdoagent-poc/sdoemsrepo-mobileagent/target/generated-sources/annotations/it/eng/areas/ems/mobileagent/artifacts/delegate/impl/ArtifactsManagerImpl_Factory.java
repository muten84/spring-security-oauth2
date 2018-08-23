package it.eng.areas.ems.mobileagent.artifacts.delegate.impl;

import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class ArtifactsManagerImpl_Factory implements Factory<ArtifactsManagerImpl> {
  private final Provider<ArtifactsManagerService> artifactsServiceProvider;

  private final Provider<JsonStoreService> storeServiceProvider;

  public ArtifactsManagerImpl_Factory(
      Provider<ArtifactsManagerService> artifactsServiceProvider,
      Provider<JsonStoreService> storeServiceProvider) {
    this.artifactsServiceProvider = artifactsServiceProvider;
    this.storeServiceProvider = storeServiceProvider;
  }

  @Override
  public ArtifactsManagerImpl get() {
    return new ArtifactsManagerImpl(artifactsServiceProvider.get(), storeServiceProvider.get());
  }

  public static ArtifactsManagerImpl_Factory create(
      Provider<ArtifactsManagerService> artifactsServiceProvider,
      Provider<JsonStoreService> storeServiceProvider) {
    return new ArtifactsManagerImpl_Factory(artifactsServiceProvider, storeServiceProvider);
  }
}
