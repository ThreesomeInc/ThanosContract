package com.thanos.mockserver.handler;

import com.thanos.mockserver.exception.ParseException;
import com.thanos.mockserver.parser.Contract;
import com.thanos.mockserver.parser.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class RequestHandler {

    private List<Schema> requestSchemaList;
    private List<Contract> contractList;

    public RequestHandler(List<Schema> requestSchemaList, List<Contract> contractList) {
        this.requestSchemaList = requestSchemaList;
        this.contractList = contractList;
    }

    public String process(String inputRequest) {
        final Request request = parseByTypeAndLength(inputRequest);
        if (request.validateByRegex(requestSchemaList)) {
            for (Contract contract : contractList) {
                if (contract.match(request)) {
                    return contract.buildResponse();
                }
            }
        }
        throw new ParseException("Incoming request does not match any existing contract");
    }

    Request parseByTypeAndLength(String inputRequest) {
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

}
