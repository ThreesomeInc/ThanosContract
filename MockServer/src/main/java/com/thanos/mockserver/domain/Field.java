package com.thanos.mockserver.domain;

import com.thanos.mockserver.domain.validate.Validator;
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
    Validator validator;

    public Field(String name, String type, Integer length, Validator validator) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.validator = validator;
    }

    public boolean match(String extractedContent) {
        return validator.validate(extractedContent);
    }
}
