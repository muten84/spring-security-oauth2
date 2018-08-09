package it.eng.areas.ems.mobileagent.http;

import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import it.eng.areas.ems.mobileagent.artifacts.util.DimmerUtils;
import it.eng.areas.ems.mobileagent.db.PerstLookupFactory;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;
import it.eng.areas.ems.mobileagent.message.MessageService;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentRestService_Factory implements Factory<AgentRestService> {
  private final Provider<LocalArtifactsService> localArtifactsServiceProvider;

  private final Provider<String> groupIdProvider;

  private final Provider<String> localStorePathProvider;

  private final Provider<MessageService> messageServiceProvider;

  private final Provider<MessageReceiver> messageReceiverFactoryProvider;

  private final Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider;

  private final Provider<DimmerUtils> dimmerUtilsProvider;

  private final Provider<JsonMapper> mapperProvider;

  private final Provider<DocumentStore> documentStoreProvider;

  private final Provider<PerstLookupFactory> dbServiceProvider;

  public AgentRestService_Factory(
      Provider<LocalArtifactsService> localArtifactsServiceProvider,
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<MessageService> messageServiceProvider,
      Provider<MessageReceiver> messageReceiverFactoryProvider,
      Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider,
      Provider<DimmerUtils> dimmerUtilsProvider,
      Provider<JsonMapper> mapperProvider,
      Provider<DocumentStore> documentStoreProvider,
      Provider<PerstLookupFactory> dbServiceProvider) {
    this.localArtifactsServiceProvider = localArtifactsServiceProvider;
    this.groupIdProvider = groupIdProvider;
    this.localStorePathProvider = localStorePathProvider;
    this.messageServiceProvider = messageServiceProvider;
    this.messageReceiverFactoryProvider = messageReceiverFactoryProvider;
    this.artifactsManagerStateServiceProvider = artifactsManagerStateServiceProvider;
    this.dimmerUtilsProvider = dimmerUtilsProvider;
    this.mapperProvider = mapperProvider;
    this.documentStoreProvider = documentStoreProvider;
    this.dbServiceProvider = dbServiceProvider;
  }

  @Override
  public AgentRestService get() {
    return new AgentRestService(
        localArtifactsServiceProvider.get(),
        groupIdProvider.get(),
        localStorePathProvider.get(),
        DoubleCheck.lazy(messageServiceProvider),
        DoubleCheck.lazy(messageReceiverFactoryProvider),
        artifactsManagerStateServiceProvider.get(),
        dimmerUtilsProvider.get(),
        mapperProvider.get(),
        DoubleCheck.lazy(documentStoreProvider),
        DoubleCheck.lazy(dbServiceProvider));
  }

  public static AgentRestService_Factory create(
      Provider<LocalArtifactsService> localArtifactsServiceProvider,
      Provider<String> groupIdProvider,
      Provider<String> localStorePathProvider,
      Provider<MessageService> messageServiceProvider,
      Provider<MessageReceiver> messageReceiverFactoryProvider,
      Provider<ArtifactsManagerStateService> artifactsManagerStateServiceProvider,
      Provider<DimmerUtils> dimmerUtilsProvider,
      Provider<JsonMapper> mapperProvider,
      Provider<DocumentStore> documentStoreProvider,
      Provider<PerstLookupFactory> dbServiceProvider) {
    return new AgentRestService_Factory(
        localArtifactsServiceProvider,
        groupIdProvider,
        localStorePathProvider,
        messageServiceProvider,
        messageReceiverFactoryProvider,
        artifactsManagerStateServiceProvider,
        dimmerUtilsProvider,
        mapperProvider,
        documentStoreProvider,
        dbServiceProvider);
  }
}
