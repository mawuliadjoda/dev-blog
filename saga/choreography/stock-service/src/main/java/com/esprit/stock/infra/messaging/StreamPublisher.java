package com.esprit.stock.infra.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class StreamPublisher {
  private final StreamBridge streamBridge;

  public StreamPublisher(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void send(String bindingName, Object payload) {
    streamBridge.send(bindingName, payload);
  }
}
