package it.eng.areas.ems.mobileagent.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AgentModule_ProvideMessageReceiverFactory implements Factory<MessageReceiver> {
  private final AgentModule module;

  private final Provider<String> mqttURlProvider;

  private final Provider<String> qosLevelProvider;

  private final Provider<String> mqttKeepAliveProvider;

  private final Provider<String> connectAttemptMaxProvider;

  private final Provider<String> reconnectAttemptsMaxProvider;

  private final Provider<String> reconnectDelayProvider;

  private final Provider<String> reconnectDelayMaxProvider;

  private final Provider<String> receiveFreqInMillisProvider;

  public AgentModule_ProvideMessageReceiverFactory(
      AgentModule module,
      Provider<String> mqttURlProvider,
      Provider<String> qosLevelProvider,
      Provider<String> mqttKeepAliveProvider,
      Provider<String> connectAttemptMaxProvider,
      Provider<String> reconnectAttemptsMaxProvider,
      Provider<String> reconnectDelayProvider,
      Provider<String> reconnectDelayMaxProvider,
      Provider<String> receiveFreqInMillisProvider) {
    this.module = module;
    this.mqttURlProvider = mqttURlProvider;
    this.qosLevelProvider = qosLevelProvider;
    this.mqttKeepAliveProvider = mqttKeepAliveProvider;
    this.connectAttemptMaxProvider = connectAttemptMaxProvider;
    this.reconnectAttemptsMaxProvider = reconnectAttemptsMaxProvider;
    this.reconnectDelayProvider = reconnectDelayProvider;
    this.reconnectDelayMaxProvider = reconnectDelayMaxProvider;
    this.receiveFreqInMillisProvider = receiveFreqInMillisProvider;
  }

  @Override
  public MessageReceiver get() {
    return Preconditions.checkNotNull(
        module.provideMessageReceiver(
            mqttURlProvider.get(),
            qosLevelProvider.get(),
            mqttKeepAliveProvider.get(),
            connectAttemptMaxProvider.get(),
            reconnectAttemptsMaxProvider.get(),
            reconnectDelayProvider.get(),
            reconnectDelayMaxProvider.get(),
            receiveFreqInMillisProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static AgentModule_ProvideMessageReceiverFactory create(
      AgentModule module,
      Provider<String> mqttURlProvider,
      Provider<String> qosLevelProvider,
      Provider<String> mqttKeepAliveProvider,
      Provider<String> connectAttemptMaxProvider,
      Provider<String> reconnectAttemptsMaxProvider,
      Provider<String> reconnectDelayProvider,
      Provider<String> reconnectDelayMaxProvider,
      Provider<String> receiveFreqInMillisProvider) {
    return new AgentModule_ProvideMessageReceiverFactory(
        module,
        mqttURlProvider,
        qosLevelProvider,
        mqttKeepAliveProvider,
        connectAttemptMaxProvider,
        reconnectAttemptsMaxProvider,
        reconnectDelayProvider,
        reconnectDelayMaxProvider,
        receiveFreqInMillisProvider);
  }

  public static MessageReceiver proxyProvideMessageReceiver(
      AgentModule instance,
      String mqttURl,
      String qosLevel,
      String mqttKeepAlive,
      String connectAttemptMax,
      String reconnectAttemptsMax,
      String reconnectDelay,
      String reconnectDelayMax,
      String receiveFreqInMillis) {
    return Preconditions.checkNotNull(
        instance.provideMessageReceiver(
            mqttURl,
            qosLevel,
            mqttKeepAlive,
            connectAttemptMax,
            reconnectAttemptsMax,
            reconnectDelay,
            reconnectDelayMax,
            receiveFreqInMillis),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
