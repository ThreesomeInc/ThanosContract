package com.thanos.mockserver.eventbus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewMockEvent {

    int port;
    String consumerName;
    String providerName;

}
