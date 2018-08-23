package it.eng.areas.ems.mobileagent.module;

import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.message.MessageService;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideMessageServiceFactory implements Factory<MessageService> {
  private final AgentModule module;

  private final Provider<String> messageServiceUrlProvider;

  private final Provider<String> defaultMessageTimeoutINMillisProvider;

  private final Provider<JsonMapper> jsonMapperProvider;

  private final Provider<OkHttpClient> clientFactoryProvider;

  private final Provider<DocumentStore> documentStoreProvider;

  public AgentModule_ProvideMessageServiceFactory(
      AgentModule module,
      Provider<String> messageServiceUrlProvider,
      Provider<String> defaultMessageTimeoutINMillisProvider,
      Provider<JsonMapper> jsonMapperProvider,
      Provider<OkHttpClient> clientFactoryProvider,
      Provider<DocumentStore> documentStoreProvider) {
    this.module = module;
    this.messageServiceUrlProvider = messageServiceUrlProvider;
    this.defaultMessageTimeoutINMillisProvider = defaultMessageTimeoutINMillisProvider;
    this.jsonMapperProvider = jsonMapperProvider;
    this.clientFactoryProvider = clientFactoryProvider;
    this.documentStoreProvider = documentStoreProvider;
  }

  @Override
  public MessageService get() {
    return Preconditions.checkNotNull(
        module.provideMessageService(
            messageServiceUrlProvider.get(),
            defaultMessageTimeoutINMillisProvider.get(),
            jsonMapperProvider.get(),
            DoubleCheck.lazy(clientFactoryProvider),
            documentStoreProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideMessageServiceFactory create(
      AgentModule module,
      Provider<String> messageServiceUrlProvider,
      Provider<String> defaultMessageTimeoutINMillisProvider,
      Provider<JsonMapper> jsonMapperProvider,
      Provider<OkHttpClient> clientFactoryProvider,
      Provider<DocumentStore> documentStoreProvider) {
    return new AgentModule_ProvideMessageServiceFactory(
        module,
        messageServiceUrlProvider,
        defaultMessageTimeoutINMillisProvider,
        jsonMapperProvider,
        clientFactoryProvider,
        documentStoreProvider);
  }

  public static MessageService proxyProvideMessageService(
      AgentModule instance,
      String messageServiceUrl,
      String defaultMessageTimeoutINMillis,
      JsonMapper jsonMapper,
      Lazy<OkHttpClient> clientFactory,
      DocumentStore documentStore) {
    return Preconditions.checkNotNull(
        instance.provideMessageService(
            messageServiceUrl,
            defaultMessageTimeoutINMillis,
            jsonMapper,
            clientFactory,
            documentStore),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
