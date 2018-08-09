package it.eng.areas.ems.mobileagent;

import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import it.eng.areas.ems.mobileagent.artifacts.util.DimmerUtils;
import it.eng.areas.ems.mobileagent.http.AgentRestService_Factory;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import it.eng.areas.ems.mobileagent.module.AgentModule;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetAgentServerPortFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetAgentServerRootFolderFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetArtifactsHttpBaseUrlFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetGroupIdFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetHttpRestPathFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetInboundReceiveFreqFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttConnectAttemptMaxFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttKeepAliveFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttQosLevelFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttReconnectAttemptsMaxFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttReconnectDelayFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttReconnectDelayMaxFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetMqttUrlFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetOutboundConnectTimeoutFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetOutboundReadTimeoutFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetOutboundTimeoutInMillisFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetOutboundUrlFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetOutboundWriteTimeoutFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_GetStorePathFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideArtifactsManagerFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideArtifactsManagerServiceFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideArtifactsManagerStateServiceFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideCheckDownloadedArtifactsFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideDimmerUtilsFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideDocumentStoreFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideHttpClientFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideJsonMapperFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideJsonStoreServiceFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideLocalArtifactsServiceFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideLocalDbFactoryFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideMessageReceiverFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideMessageServiceFactory;
import it.eng.areas.ems.mobileagent.module.AgentModule_ProvideMultipleArtifactsJobFactory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerAgentApp_AgentComponent implements AgentApp.AgentComponent {
  private AgentModule agentModule;

  private AgentModule_GetStorePathFactory getStorePathProvider;

  private AgentModule_GetGroupIdFactory getGroupIdProvider;

  private Provider<JsonMapper> provideJsonMapperProvider;

  private Provider<JsonStoreService> provideJsonStoreServiceProvider;

  private Provider<LocalArtifactsService> provideLocalArtifactsServiceProvider;

  private AgentModule_GetOutboundUrlFactory getOutboundUrlProvider;

  private AgentModule_GetOutboundTimeoutInMillisFactory getOutboundTimeoutInMillisProvider;

  private AgentModule_GetOutboundConnectTimeoutFactory getOutboundConnectTimeoutProvider;

  private AgentModule_GetOutboundReadTimeoutFactory getOutboundReadTimeoutProvider;

  private AgentModule_GetOutboundWriteTimeoutFactory getOutboundWriteTimeoutProvider;

  private AgentModule_ProvideHttpClientFactory provideHttpClientProvider;

  private Provider<DocumentStore> provideDocumentStoreProvider;

  private AgentModule_ProvideMessageServiceFactory provideMessageServiceProvider;

  private AgentModule_GetMqttUrlFactory getMqttUrlProvider;

  private AgentModule_GetMqttQosLevelFactory getMqttQosLevelProvider;

  private AgentModule_GetMqttKeepAliveFactory getMqttKeepAliveProvider;

  private AgentModule_GetMqttConnectAttemptMaxFactory getMqttConnectAttemptMaxProvider;

  private AgentModule_GetMqttReconnectAttemptsMaxFactory getMqttReconnectAttemptsMaxProvider;

  private AgentModule_GetMqttReconnectDelayFactory getMqttReconnectDelayProvider;

  private AgentModule_GetMqttReconnectDelayMaxFactory getMqttReconnectDelayMaxProvider;

  private AgentModule_GetInboundReceiveFreqFactory getInboundReceiveFreqProvider;

  private AgentModule_ProvideMessageReceiverFactory provideMessageReceiverProvider;

  private Provider<ArtifactsManagerStateService> provideArtifactsManagerStateServiceProvider;

  private Provider<DimmerUtils> provideDimmerUtilsProvider;

  private AgentModule_ProvideLocalDbFactoryFactory provideLocalDbFactoryProvider;

  private AgentRestService_Factory agentRestServiceProvider;

  private AgentModule_ProvideCheckDownloadedArtifactsFactory
      provideCheckDownloadedArtifactsProvider;

  private AgentModule_GetArtifactsHttpBaseUrlFactory getArtifactsHttpBaseUrlProvider;

  private AgentModule_GetHttpRestPathFactory getHttpRestPathProvider;

  private Provider<ArtifactsManagerService> provideArtifactsManagerServiceProvider;

  private Provider<ArtifactsManager> provideArtifactsManagerProvider;

  private AgentModule_ProvideMultipleArtifactsJobFactory provideMultipleArtifactsJobProvider;

  private DaggerAgentApp_AgentComponent(Builder builder) {
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static AgentApp.AgentComponent create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {
    this.getStorePathProvider = AgentModule_GetStorePathFactory.create(builder.agentModule);
    this.getGroupIdProvider = AgentModule_GetGroupIdFactory.create(builder.agentModule);
    this.provideJsonMapperProvider =
        DoubleCheck.provider(AgentModule_ProvideJsonMapperFactory.create());
    this.provideJsonStoreServiceProvider =
        DoubleCheck.provider(
            AgentModule_ProvideJsonStoreServiceFactory.create(
                getStorePathProvider, getGroupIdProvider, provideJsonMapperProvider));
    this.provideLocalArtifactsServiceProvider =
        DoubleCheck.provider(
            AgentModule_ProvideLocalArtifactsServiceFactory.create(
                getStorePathProvider, provideJsonStoreServiceProvider, provideJsonMapperProvider));
    this.getOutboundUrlProvider = AgentModule_GetOutboundUrlFactory.create(builder.agentModule);
    this.getOutboundTimeoutInMillisProvider =
        AgentModule_GetOutboundTimeoutInMillisFactory.create(builder.agentModule);
    this.getOutboundConnectTimeoutProvider =
        AgentModule_GetOutboundConnectTimeoutFactory.create(builder.agentModule);
    this.getOutboundReadTimeoutProvider =
        AgentModule_GetOutboundReadTimeoutFactory.create(builder.agentModule);
    this.getOutboundWriteTimeoutProvider =
        AgentModule_GetOutboundWriteTimeoutFactory.create(builder.agentModule);
    this.provideHttpClientProvider =
        AgentModule_ProvideHttpClientFactory.create(
            builder.agentModule,
            getOutboundConnectTimeoutProvider,
            getOutboundReadTimeoutProvider,
            getOutboundWriteTimeoutProvider);
    this.provideDocumentStoreProvider =
        DoubleCheck.provider(AgentModule_ProvideDocumentStoreFactory.create(builder.agentModule));
    this.provideMessageServiceProvider =
        AgentModule_ProvideMessageServiceFactory.create(
            builder.agentModule,
            getOutboundUrlProvider,
            getOutboundTimeoutInMillisProvider,
            provideJsonMapperProvider,
            provideHttpClientProvider,
            provideDocumentStoreProvider);
    this.getMqttUrlProvider = AgentModule_GetMqttUrlFactory.create(builder.agentModule);
    this.getMqttQosLevelProvider = AgentModule_GetMqttQosLevelFactory.create(builder.agentModule);
    this.getMqttKeepAliveProvider = AgentModule_GetMqttKeepAliveFactory.create(builder.agentModule);
    this.getMqttConnectAttemptMaxProvider =
        AgentModule_GetMqttConnectAttemptMaxFactory.create(builder.agentModule);
    this.getMqttReconnectAttemptsMaxProvider =
        AgentModule_GetMqttReconnectAttemptsMaxFactory.create(builder.agentModule);
    this.getMqttReconnectDelayProvider =
        AgentModule_GetMqttReconnectDelayFactory.create(builder.agentModule);
    this.getMqttReconnectDelayMaxProvider =
        AgentModule_GetMqttReconnectDelayMaxFactory.create(builder.agentModule);
    this.getInboundReceiveFreqProvider =
        AgentModule_GetInboundReceiveFreqFactory.create(builder.agentModule);
    this.provideMessageReceiverProvider =
        AgentModule_ProvideMessageReceiverFactory.create(
            builder.agentModule,
            getMqttUrlProvider,
            getMqttQosLevelProvider,
            getMqttKeepAliveProvider,
            getMqttConnectAttemptMaxProvider,
            getMqttReconnectAttemptsMaxProvider,
            getMqttReconnectDelayProvider,
            getMqttReconnectDelayMaxProvider,
            getInboundReceiveFreqProvider);
    this.provideArtifactsManagerStateServiceProvider =
        DoubleCheck.provider(AgentModule_ProvideArtifactsManagerStateServiceFactory.create());
    this.provideDimmerUtilsProvider =
        DoubleCheck.provider(AgentModule_ProvideDimmerUtilsFactory.create());
    this.provideLocalDbFactoryProvider =
        AgentModule_ProvideLocalDbFactoryFactory.create(builder.agentModule);
    this.agentRestServiceProvider =
        AgentRestService_Factory.create(
            provideLocalArtifactsServiceProvider,
            getGroupIdProvider,
            getStorePathProvider,
            provideMessageServiceProvider,
            provideMessageReceiverProvider,
            provideArtifactsManagerStateServiceProvider,
            provideDimmerUtilsProvider,
            provideJsonMapperProvider,
            provideDocumentStoreProvider,
            provideLocalDbFactoryProvider);
    this.agentModule = builder.agentModule;
    this.provideCheckDownloadedArtifactsProvider =
        AgentModule_ProvideCheckDownloadedArtifactsFactory.create(
            builder.agentModule,
            getGroupIdProvider,
            getStorePathProvider,
            provideLocalArtifactsServiceProvider,
            provideJsonMapperProvider);
    this.getArtifactsHttpBaseUrlProvider =
        AgentModule_GetArtifactsHttpBaseUrlFactory.create(builder.agentModule);
    this.getHttpRestPathProvider = AgentModule_GetHttpRestPathFactory.create(builder.agentModule);
    this.provideArtifactsManagerServiceProvider =
        DoubleCheck.provider(
            AgentModule_ProvideArtifactsManagerServiceFactory.create(
                getArtifactsHttpBaseUrlProvider, getHttpRestPathProvider));
    this.provideArtifactsManagerProvider =
        DoubleCheck.provider(
            AgentModule_ProvideArtifactsManagerFactory.create(
                provideArtifactsManagerServiceProvider, provideJsonStoreServiceProvider));
    this.provideMultipleArtifactsJobProvider =
        AgentModule_ProvideMultipleArtifactsJobFactory.create(
            builder.agentModule,
            getGroupIdProvider,
            getStorePathProvider,
            provideArtifactsManagerProvider,
            provideJsonStoreServiceProvider,
            provideArtifactsManagerStateServiceProvider);
  }

  @Override
  public Agent agent() {
    return new Agent(
        DoubleCheck.lazy(agentRestServiceProvider),
        AgentModule_GetAgentServerRootFolderFactory.proxyGetAgentServerRootFolder(agentModule),
        AgentModule_GetAgentServerPortFactory.proxyGetAgentServerPort(agentModule),
        provideArtifactsManagerStateServiceProvider.get(),
        DoubleCheck.lazy(provideCheckDownloadedArtifactsProvider),
        DoubleCheck.lazy(provideMultipleArtifactsJobProvider),
        provideJsonStoreServiceProvider.get(),
        provideArtifactsManagerProvider.get());
  }

  public static final class Builder {
    private AgentModule agentModule;

    private Builder() {}

    public AgentApp.AgentComponent build() {
      if (agentModule == null) {
        this.agentModule = new AgentModule();
      }
      return new DaggerAgentApp_AgentComponent(this);
    }

    public Builder agentModule(AgentModule agentModule) {
      this.agentModule = Preconditions.checkNotNull(agentModule);
      return this;
    }
  }
}
