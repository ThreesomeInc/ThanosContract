package com.thanos.mockserver.handler;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Map<String, Object> fields;
}
