package com.thanos.mockserver.parser;

import com.mifmif.common.regex.Generex;
import com.thanos.mockserver.validate.PlainTextValidator;
import com.thanos.mockserver.validate.RegexValidator;
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
    public Msg buildResponse(List<Schema> responseSchema) {

        LinkedHashMap<String, Object> responseMsg = new LinkedHashMap<>();

        Collections.sort(responseSchema);

        for (Schema resSchema : responseSchema) {
            if (res.containsKey(resSchema.getName())) {
                responseMsg.put(resSchema.getName(), res.get(resSchema.getName()));
            } else if (resSchema.getValidator() instanceof RegexValidator) {
                Generex generex = new Generex(((RegexValidator) resSchema.getValidator()).getRegexp());
                responseMsg.put(resSchema.getName(), generex.random());
            } else if (resSchema.getValidator() instanceof PlainTextValidator) {
                responseMsg.put(resSchema.getName(),
                        ((PlainTextValidator) resSchema.getValidator()).getExpectedValue());
            }
        }

        return new Msg(responseMsg);
    }
}
