package com.thanos.mockserver.parser;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    String name;
    Map<String, Object> req;
    Map<String, Object> res;

    static List<Contract> buildFrom(Iterable<Object> ymlResult) {
        final List<Contract> result = new ArrayList<>();
        for (Object record : ymlResult) {
            if (record instanceof Contract) {
                result.add((Contract) record);
            }
        }
        return result;
    }
}
