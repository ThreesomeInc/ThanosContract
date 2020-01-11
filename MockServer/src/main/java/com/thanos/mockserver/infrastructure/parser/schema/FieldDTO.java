package com.thanos.mockserver.infrastructure.parser.schema;

import com.thanos.mockserver.domain.SchemaField;
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

    public SchemaField toField() {
        return new SchemaField(name, type, length, validation, new Lexer().Lex(validation));
    }
}
