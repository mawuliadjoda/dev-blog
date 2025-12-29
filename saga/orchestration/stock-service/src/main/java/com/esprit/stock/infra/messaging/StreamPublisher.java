package com.esprit.stock.infra.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StreamPublisher {
  private final StreamBridge streamBridge;

  public void send(String bindingName, Object payload) {
    streamBridge.send(bindingName, payload);
  }
}
