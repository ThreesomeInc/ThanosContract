package com.thanos.mockserver.domain;

import com.google.common.collect.Maps;
import com.thanos.mockserver.exception.ParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Schema {

    private String provider;
    private String version;
    private String name;
    private LinkedList<SchemaField> request;
    private LinkedList<SchemaField> response;


    public Message parseRequest(String reqMsg) {
        try {
            LinkedHashMap<String, String> parseResult = Maps.newLinkedHashMap();
            int startIndex = 0;
            for (SchemaField schemaField : request) {
                final String extractedContent =
                        reqMsg.substring(startIndex, startIndex + schemaField.getLength());

                if (!schemaField.match(extractedContent)) {
                    throw new ParseException("Request does not match this schema: " + this.name);
                } else {
                    parseResult.put(schemaField.getName(), extractedContent);
                    startIndex += schemaField.getLength();
                }
            }
            return new Message(this, parseResult);

        } catch (StringIndexOutOfBoundsException stringIndexEx) {
            throw new ParseException("Input request is shorter then schema expected!", stringIndexEx.getCause());
        }
    }
}
