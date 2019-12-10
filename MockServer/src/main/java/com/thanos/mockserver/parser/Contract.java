package com.thanos.mockserver.parser;

import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    String name;
    LinkedHashMap<String, Object> req;
    LinkedHashMap<String, Object> res;

    static List<Contract> buildFrom(Iterable<Object> ymlResult) {
        final List<Contract> result = new ArrayList<>();
        for (Object record : ymlResult) {
            if (record instanceof Contract) {
                result.add((Contract) record);
            }
        }
        return result;
    }

    // TODO: Only support fix value in contract now
    public boolean match(Msg msg) {
        for (String key : req.keySet()) {
            if (msg.getFields().containsKey(key)) {
                final Object requestContent = msg.getFields().get(key);
                if (!req.get(key).equals(requestContent)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    // Directly combine from contract without validation
    public String buildResponse() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object o : res.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            stringBuilder.append(entry.getValue());
        }

        return stringBuilder.toString();
    }
}
