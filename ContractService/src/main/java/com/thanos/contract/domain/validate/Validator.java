package com.thanos.contract.domain.validate;

public interface Validator {
    String name();

    boolean validate(String actualValue);

    String getExpectedValue();

}
