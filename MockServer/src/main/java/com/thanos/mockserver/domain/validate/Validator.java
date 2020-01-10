package com.thanos.mockserver.domain.validate;

public interface Validator {
    String name();

    boolean validate(String actualValue);

}
