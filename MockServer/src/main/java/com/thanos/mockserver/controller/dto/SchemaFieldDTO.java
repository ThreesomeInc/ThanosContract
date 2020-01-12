package com.thanos.mockserver.controller.dto;

import com.thanos.mockserver.domain.SchemaField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SchemaFieldDTO {

    String name;
    String type;
    Integer length;
    String content;

    public SchemaFieldDTO(SchemaField schemaField) {
        this.name = schemaField.getName();
        this.type = schemaField.getType();
        this.length = schemaField.getLength();
        this.content = schemaField.getContent();
    }
}
