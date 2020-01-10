package com.thanos.mockserver.domain.schema;

import com.google.common.collect.Maps;
import com.thanos.mockserver.domain.Message;
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
    private LinkedList<Field> request;
    private LinkedList<Field> response;


    public Message parseRequest(String reqMsg) {
        try {
            LinkedHashMap<String, Object> parseResult = Maps.newLinkedHashMap();
            int startIndex = 0;
            for (Field field : request) {
                final String extractedContent =
                        reqMsg.substring(startIndex, startIndex + field.getLength());

                if (!field.match(extractedContent)) {
                    throw new ParseException("Request does not match this schema: " + this.name);
                } else {
                    parseResult.put(field.getName(), extractedContent);
                    startIndex += field.getLength();
                }
            }
            return new Message(this, parseResult);

        } catch (StringIndexOutOfBoundsException stringIndexEx) {
            throw new ParseException("Input request is shorter then schema expected!", stringIndexEx.getCause());
        }
    }
}
