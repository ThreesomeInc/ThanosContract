package com.thanos.mockserver.infrastructure.parser.schema;

import com.thanos.mockserver.domain.Field;
import com.thanos.mockserver.domain.lex.Lexer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FieldDTO {
    String name;
    String type;
    Integer length;
    String validation;

    public Field toField() {
        return new Field(name, type, length, new Lexer().Lex(validation));
    }
}
