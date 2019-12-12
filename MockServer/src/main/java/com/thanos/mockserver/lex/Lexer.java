package com.thanos.mockserver.lex;

import com.google.common.collect.ImmutableMap;
import com.thanos.mockserver.validate.DefaultValidator;
import com.thanos.mockserver.validate.FuncValidator;
import com.thanos.mockserver.validate.PlainTextValidator;
import com.thanos.mockserver.validate.RegexValidator;
import com.thanos.mockserver.validate.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import sun.misc.FloatingDecimal;

import java.text.ParseException;
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
            })
            .put("isDatetime", (actual) -> {
                final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
                try {
                    FastDateFormat.getInstance(YMD_HMS).parse(actual);
                } catch (ParseException e) {
                    return false;
                }
                return true;
            })
            .build();

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
        } else if ("".equals(StringUtils.trim(value))) {
            return new DefaultValidator();
        } else {
            return new PlainTextValidator(value);
        }
        throw new RuntimeException("parse validator failed.");
    }
}
