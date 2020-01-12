package com.thanos.contract.infrastructure.parser.contract;

import com.thanos.contract.domain.Contract;
import com.thanos.contract.domain.ContractField;
import com.thanos.contract.domain.lex.Lexer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ContractDTO {

    String name;
    Map<String, String> schema;
    LinkedHashMap<String, String> req;
    LinkedHashMap<String, String> res;

    public static List<Contract> buildFrom(Iterable<Object> ymlResult) {
        final List<Contract> result = new ArrayList<>();
        for (Object record : ymlResult) {
            if (record instanceof ContractDTO) {
                result.add(((ContractDTO) record).toNewContract());
            }
        }
        return result;
    }

    public Contract toNewContract() {

        LinkedList<ContractField> request = buildContractFieldList(req);
        LinkedList<ContractField> response = buildContractFieldList(res);
        return new Contract(name.trim().toUpperCase(),
                schema.get("name").trim().toUpperCase(),
                schema.get("version").trim().toUpperCase(),
                schema.get("consumer").trim().toUpperCase(),
                schema.get("provider").trim().toUpperCase(),
                request, response);
    }

    LinkedList<ContractField> buildContractFieldList(LinkedHashMap<String, String> originDtoMap) {
        LinkedList<ContractField> result = new LinkedList<>();
        for (String key : originDtoMap.keySet()) {
            String content = originDtoMap.get(key);
            result.add(new ContractField(key, content, new Lexer().Lex(content)));
        }
        return result;
    }

}
