package com.thanos.mockserver.parser;

import com.thanos.mockserver.exception.ParseException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Msg {

    private Map<String, Object> fields;

    public Boolean validateByRegex(List<Schema> schemaList) {
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
}
