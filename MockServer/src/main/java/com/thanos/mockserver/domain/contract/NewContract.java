package com.thanos.mockserver.domain.contract;

import com.mifmif.common.regex.Generex;
import com.thanos.mockserver.domain.Message;
import com.thanos.mockserver.domain.schema.Field;
import com.thanos.mockserver.domain.schema.NewSchema;
import com.thanos.mockserver.domain.validate.PlainTextValidator;
import com.thanos.mockserver.domain.validate.RegexValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewContract {

    String name;
    String schemaName;
    String schemaVersion;
    String consumer;
    String provider;

    LinkedHashMap<String, Object> req;
    LinkedHashMap<String, Object> res;

    public String getIndex() {
        return provider + '-' + consumer;
    }

    /**
     * The field listed in contract should match the value in request
     */
    public Boolean matchRequest(Message msg) {
        for (String field : req.keySet()) {
            if (msg.getParseRequest().containsKey(field) &&
                    (!msg.getParseRequest().get(field).equals(req.get(field)))) {
                return false;
            }
        }
        return true;
    }

    public String buildResponse(NewSchema matchedSchema) {
        StringBuilder result = new StringBuilder();

        for (Field field : matchedSchema.getResponse()) {
            if (res.containsKey(field.getName())) {
                result.append(res.get(field.getName()));

            } else if (field.getValidator() instanceof RegexValidator) {
                Generex generex = new Generex(((RegexValidator) field.getValidator()).getRegexp());
                result.append(generex.random());

            } else if (field.getValidator() instanceof PlainTextValidator) {
                result.append(((PlainTextValidator) field.getValidator()).getExpectedValue());
            }
        }
        return result.toString();
    }
}
