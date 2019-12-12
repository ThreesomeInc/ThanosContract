package com.thanos.mockserver.validate;

import java.util.Objects;

public class PlainTextValidator implements Validator {
    private String expectedValue;

    public PlainTextValidator(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public boolean validate(String actualValue) {
        return Objects.equals(expectedValue, actualValue);
    }

    public String getExpectedValue() {
        return this.expectedValue;
    }
}
