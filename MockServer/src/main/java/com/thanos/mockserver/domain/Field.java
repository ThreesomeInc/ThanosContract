package com.thanos.mockserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Field {
    String name;
    String type;
    Integer length;
    String validation;
    Boolean matchingKey;

    public Field(String name, String type, Integer length, String validation, Boolean matchingKey) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.validation = validation;
        this.matchingKey = matchingKey;
    }
}
