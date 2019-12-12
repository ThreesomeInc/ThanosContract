package com.thanos.mockserver.parser;

import com.mifmif.common.regex.Generex;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
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
    public String buildResponse(List<Schema> responseSchema) {
        StringBuilder stringBuilder = new StringBuilder();

        Collections.sort(responseSchema);

        for (Schema response : responseSchema) {
            if (res.containsKey(response.getName())) {
                stringBuilder.append(res.get(response.getName()));
            } else {
//                Generex generex = new Generex(response.getRegex());
//                stringBuilder.append(generex.random());
                //TODO: to confirm this logic
            }
        }

        log.info("Response : " + stringBuilder.toString());
        return stringBuilder.toString();
    }
}
