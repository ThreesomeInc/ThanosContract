package com.thanos.mockserver.infrastructure.parser.schema;

import com.thanos.mockserver.domain.schema.Field;
import com.thanos.mockserver.lex.Lexer;
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
        Lexer lexer = new Lexer();

        return new Field(name, type, length,
                Optional.ofNullable(matchingKey).orElse(Boolean.FALSE),
                lexer.Lex(validation));
    }
}
