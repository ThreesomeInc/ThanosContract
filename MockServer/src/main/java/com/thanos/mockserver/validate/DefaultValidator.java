package com.thanos.mockserver.validate;

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
