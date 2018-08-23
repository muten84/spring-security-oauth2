package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideHttpClientFactory implements Factory<OkHttpClient> {
  private final AgentModule module;

  private final Provider<String> connectTimeoutInMillisProvider;

  private final Provider<String> readTimeoutInMillisProvider;

  private final Provider<String> writeTimeoutInMillisProvider;

  public AgentModule_ProvideHttpClientFactory(
      AgentModule module,
      Provider<String> connectTimeoutInMillisProvider,
      Provider<String> readTimeoutInMillisProvider,
      Provider<String> writeTimeoutInMillisProvider) {
    this.module = module;
    this.connectTimeoutInMillisProvider = connectTimeoutInMillisProvider;
    this.readTimeoutInMillisProvider = readTimeoutInMillisProvider;
    this.writeTimeoutInMillisProvider = writeTimeoutInMillisProvider;
  }

  @Override
  public OkHttpClient get() {
    return Preconditions.checkNotNull(
        module.provideHttpClient(
            connectTimeoutInMillisProvider.get(),
            readTimeoutInMillisProvider.get(),
            writeTimeoutInMillisProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideHttpClientFactory create(
      AgentModule module,
      Provider<String> connectTimeoutInMillisProvider,
      Provider<String> readTimeoutInMillisProvider,
      Provider<String> writeTimeoutInMillisProvider) {
    return new AgentModule_ProvideHttpClientFactory(
        module,
        connectTimeoutInMillisProvider,
        readTimeoutInMillisProvider,
        writeTimeoutInMillisProvider);
  }

  public static OkHttpClient proxyProvideHttpClient(
      AgentModule instance,
      String connectTimeoutInMillis,
      String readTimeoutInMillis,
      String writeTimeoutInMillis) {
    return Preconditions.checkNotNull(
        instance.provideHttpClient(
            connectTimeoutInMillis, readTimeoutInMillis, writeTimeoutInMillis),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
