package com.thanos.mockserver.validate;

public interface Validator {
    String name();

    boolean validate(String actualValue);

}
