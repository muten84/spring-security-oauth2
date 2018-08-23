package it.eng.areas.ems.mobileagent.message;

import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class MessageService_Factory implements Factory<MessageService> {
  private final Provider<String> messageServiceUrlProvider;

  private final Provider<String> defaultMessageTimeoutINMillisProvider;

  private final Provider<JsonMapper> jsonMapperProvider;

  private final Provider<OkHttpClient> clientFactoryProvider;

  private final Provider<DocumentStore> documentStoreProvider;

  public MessageService_Factory(
      Provider<String> messageServiceUrlProvider,
      Provider<String> defaultMessageTimeoutINMillisProvider,
      Provider<JsonMapper> jsonMapperProvider,
      Provider<OkHttpClient> clientFactoryProvider,
      Provider<DocumentStore> documentStoreProvider) {
    this.messageServiceUrlProvider = messageServiceUrlProvider;
    this.defaultMessageTimeoutINMillisProvider = defaultMessageTimeoutINMillisProvider;
    this.jsonMapperProvider = jsonMapperProvider;
    this.clientFactoryProvider = clientFactoryProvider;
    this.documentStoreProvider = documentStoreProvider;
  }

  @Override
  public MessageService get() {
    return new MessageService(
        messageServiceUrlProvider.get(),
        defaultMessageTimeoutINMillisProvider.get(),
        jsonMapperProvider.get(),
        DoubleCheck.lazy(clientFactoryProvider),
        documentStoreProvider.get());
  }

  public static MessageService_Factory create(
      Provider<String> messageServiceUrlProvider,
      Provider<String> defaultMessageTimeoutINMillisProvider,
      Provider<JsonMapper> jsonMapperProvider,
      Provider<OkHttpClient> clientFactoryProvider,
      Provider<DocumentStore> documentStoreProvider) {
    return new MessageService_Factory(
        messageServiceUrlProvider,
        defaultMessageTimeoutINMillisProvider,
        jsonMapperProvider,
        clientFactoryProvider,
        documentStoreProvider);
  }
}
