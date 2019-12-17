package com.thanos.mockserver.parser;

import com.thanos.mockserver.exception.ParseException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Msg {

    private LinkedHashMap<String, Object> fields;

    public Boolean validate(List<Schema> schemaList) {
        for (Schema schema : schemaList) {
            if (fields.containsKey(schema.getName())) {
                Object content = fields.get(schema.getName());
                if (!schema.getValidator().validate(content.toString())) {
                    log.warn("Field {} mismatch with schema {}", content.toString(), schema);
                    return false;
                }
            } else {
                throw new ParseException("Field missing in request: " + schema.toString());
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "fields=" + fields +
                '}';
    }
}
