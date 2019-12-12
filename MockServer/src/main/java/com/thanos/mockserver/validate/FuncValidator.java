package com.thanos.mockserver.validate;

import java.util.function.Function;

public class FuncValidator implements Validator {
    private Function<String, Boolean> function;
    private String name;

    public FuncValidator(String name, Function<String, Boolean> function) {
        this.function = function;
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public boolean validate(String actualValue) {
        return function.apply(actualValue);
    }
}
