package com.thanos.mockserver.domain.schema;

import com.thanos.mockserver.validate.Validator;
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
    Boolean matchingKey;
    Validator validator;

    public Field(String name, String type, Integer length, Boolean matchingKey, Validator validator) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.matchingKey = matchingKey;
        this.validator = validator;
    }

    public boolean match(String extractedContent) {
        return validator.validate(extractedContent);
    }
}
