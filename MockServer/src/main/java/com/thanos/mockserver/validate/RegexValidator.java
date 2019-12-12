package com.thanos.mockserver.validate;

public class RegexValidator implements Validator {
    public final static String NAME = "regex";
    private String regexp;

    public RegexValidator(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean validate(String actualValue) {
        return actualValue != null && actualValue.matches(regexp);
    }

    public String getRegexp() {
        return this.regexp;
    }
}
