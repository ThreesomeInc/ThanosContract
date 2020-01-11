package com.thanos.mockserver.domain;

import com.thanos.mockserver.domain.validate.Validator;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SchemaField {
    String name;
    String type;
    Integer length;
    String content;
    Validator validator;

    public SchemaField(String name, String type, Integer length, String content, Validator validator) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.content = content;
        this.validator = validator;
    }

    public boolean match(String extractedContent) {
        return validator.validate(extractedContent);
    }

    @Override
    public String toString() {
        return "SchemaField{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", content='" + content + '\'' +
                '}';
    }
}
