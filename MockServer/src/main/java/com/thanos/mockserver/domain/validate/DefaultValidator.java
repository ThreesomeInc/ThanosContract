package com.thanos.mockserver.domain.validate;

public class DefaultValidator implements Validator {
    @Override
    public String name() {
        return "DefaultValidator";
    }

    @Override
    public boolean validate(String actualValue) {
        return true;
    }

    @Override
    public String getExpectedValue() {
        return "";
    }

    @Override
    public String toString() {
        return this.name();
    }
}
