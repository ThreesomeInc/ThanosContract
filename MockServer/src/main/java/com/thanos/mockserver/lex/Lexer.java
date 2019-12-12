package com.thanos.mockserver.lex;

import com.google.common.collect.ImmutableMap;
import com.thanos.mockserver.validate.FuncValidator;
import com.thanos.mockserver.validate.PlainTextValidator;
import com.thanos.mockserver.validate.RegexValidator;
import com.thanos.mockserver.validate.Validator;
import sun.misc.FloatingDecimal;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: This lexer support ONLY one kind of validator, requires AST parsing for composite validator.
public class Lexer {

    private String PATTERN_IN_STRING = "^\\s?(.*)\\((.*)\\)\\s?$";

    private Pattern FUNCTION_PATTERN = Pattern.compile(PATTERN_IN_STRING);

    private Map<String, Function<String, Boolean>> PREDEFINED_FUNCTIONS = ImmutableMap.<String, Function<String, Boolean>>builder()
            .put("isNumber", (actual) -> {
                try {
                    FloatingDecimal.parseDouble(actual);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }).build();

    public Validator Lex(String value) {
        if (value.matches(PATTERN_IN_STRING)) {
            Matcher matcher = FUNCTION_PATTERN.matcher(value);
            if (matcher.find()) {
                String functionName = matcher.group(1);
                if (RegexValidator.NAME.equals(functionName)) {
                    String regexpValue = matcher.group(2);
                    return new RegexValidator(regexpValue);
                } else if (PREDEFINED_FUNCTIONS.containsKey(functionName)) {
                    return new FuncValidator(functionName, PREDEFINED_FUNCTIONS.get(functionName));
                }
            }
        } else {
            return new PlainTextValidator(value);
        }
        throw new RuntimeException("parse validator failed.");
    }
}
