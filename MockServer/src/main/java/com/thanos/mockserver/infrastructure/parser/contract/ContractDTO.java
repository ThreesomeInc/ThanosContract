package com.thanos.mockserver.infrastructure.parser.contract;

import com.thanos.mockserver.domain.contract.NewContract;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ContractDTO {

    String name;
    Map<String, String> schema;
    LinkedHashMap<String, Object> req;
    LinkedHashMap<String, Object> res;

    public static List<NewContract> buildFrom(Iterable<Object> ymlResult) {
        final List<NewContract> result = new ArrayList<>();
        for (Object record : ymlResult) {
            if (record instanceof ContractDTO) {
                result.add(((ContractDTO) record).toNewContract());
            }
        }
        return result;
    }

    public NewContract toNewContract() {
        return new NewContract(name.trim().toUpperCase(),
                schema.get("name").trim().toUpperCase(),
                schema.get("version").trim().toUpperCase(),
                schema.get("consumer").trim().toUpperCase(),
                schema.get("provider").trim().toUpperCase(),
                req, res);
    }

}
