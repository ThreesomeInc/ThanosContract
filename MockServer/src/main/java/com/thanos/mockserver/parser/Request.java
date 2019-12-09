package com.thanos.mockserver.parser;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    Map<String, Object> fields;
}
