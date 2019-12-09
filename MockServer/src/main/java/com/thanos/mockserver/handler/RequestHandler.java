package com.thanos.mockserver.handler;

import com.thanos.mockserver.exception.ParseException;
import com.thanos.mockserver.parser.Schema;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class RequestHandler {

    public Request parseByTypeAndLength(String inputRequest, List<Schema> requestSchemaList) {
        Map<String, Object> fields = new HashMap<>();

        int startIndex = 0;
        try {
            for (Schema requestSchema : requestSchemaList) {

                if (requestSchema.getType().equals("CHAR")) {
                    fields.put(requestSchema.getName(),
                            inputRequest.substring(startIndex, startIndex + requestSchema.getLength()));
                    startIndex += requestSchema.getLength();

                } else if (requestSchema.getType().equals("NUM")) {
                    fields.put(requestSchema.getName(),
                            Integer.valueOf(inputRequest.substring(startIndex, startIndex + requestSchema.getLength())));
                    startIndex += requestSchema.getLength();

                } else {
                    throw new ParseException("Invalid Request Schema");
                }
            }
        } catch (StringIndexOutOfBoundsException stringIndexEx) {
            throw new ParseException("Input Request is shorter then schema expected!", stringIndexEx.getCause());
        } catch (NumberFormatException numFormatEx) {
            throw new ParseException("Expected to be NUM but turn out to be no!", numFormatEx.getCause());
        }


        return new Request(fields);
    }

    public Boolean validateByRegex(Request request, List<Schema> requestSchemaList) {
        for (Schema schema : requestSchemaList) {
            if (request.getFields().containsKey(schema.getName())) {
                Object content = request.getFields().get(schema.getName());
                final boolean matches = Pattern.matches(schema.getRegex(), content.toString());
                if (!matches) {
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
