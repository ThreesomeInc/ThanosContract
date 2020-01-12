package com.thanos.contract.infrastructure.parser.schema;

import com.thanos.contract.domain.SchemaField;
import com.thanos.contract.domain.lex.Lexer;
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
