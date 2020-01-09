package com.thanos.mockserver.infrastructure.parser.dto;

import com.thanos.mockserver.domain.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FieldDTO {
    String name;
    String type;
    Integer length;
    String validation;
    Boolean matchingKey;

    public Field toField() {
        return new Field(name, type, length, validation,
                Optional.ofNullable(matchingKey).orElse(Boolean.FALSE));
    }
}
