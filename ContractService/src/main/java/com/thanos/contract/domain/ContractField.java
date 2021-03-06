package com.thanos.contract.domain;

import com.thanos.contract.domain.validate.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContractField {

    private String name;
    private String content;
    private Validator validator;

    public boolean match(String actualContent) {
        return validator.validate(actualContent);
    }
}
