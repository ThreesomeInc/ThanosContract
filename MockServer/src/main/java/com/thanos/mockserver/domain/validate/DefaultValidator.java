package com.thanos.mockserver.domain.validate;

public class DefaultValidator implements Validator {
    @Override
    public String name() {
        return "default";
    }

    @Override
    public boolean validate(String actualValue) {
        return true;
    }
}
