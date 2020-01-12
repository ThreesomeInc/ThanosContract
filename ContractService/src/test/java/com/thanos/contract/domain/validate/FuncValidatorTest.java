package com.thanos.contract.domain.validate;

import com.thanos.contract.domain.lex.Lexer;
import org.junit.Before;
import org.junit.Test;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FuncValidatorTest {
    @Before
    public void init() {

    }

    @Test
    public void testLambdaFromString() throws LambdaCreationException {
        LambdaFactory factory = LambdaFactory.get(
                LambdaFactoryConfiguration.get().withImports(BigDecimal.class));
        BiFunction<BigDecimal, BigDecimal, BigDecimal> lambda = factory.createLambda(
                "(a,b) -> a.add(b)",
                new TypeReference<BiFunction<BigDecimal, BigDecimal, BigDecimal>>() {
                });
        assertEquals(new BigDecimal("11"), lambda.apply(BigDecimal.ONE, BigDecimal.TEN));


        boolean[] result = new boolean[]{false, false, true};
        Function<String, Boolean> f = factory.createLambda("(a) -> a.length() > 3", new TypeReference<Function<String, Boolean>>() {
        });

        FuncValidator funcValidator = new FuncValidator("customValidator0", f);
        String[] params = new String[]{"2", "133", "1733"};
        for (int i = 0; i < params.length; i++) {
            assertEquals(result[i], funcValidator.validate(params[i]));
        }
    }

    @Test
    public void jsEngineFunctionTest() throws ScriptException {

        boolean[] result = new boolean[]{false, true, true};

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        @SuppressWarnings("unchecked")
        Function<String, Boolean> f = (Function<String, Boolean>) engine.eval(
                String.format("new java.util.function.Function(%s)",
                        "function(x) {print(x);var y = 3 * parseFloat(x) + 1; return y>15}"));
        FuncValidator funcValidator = new FuncValidator("customValidator1", f);
        String[] params = new String[]{"2", "13", "17"};
        for (int i = 0; i < params.length; i++) {
            assertEquals(result[i], funcValidator.validate(params[i]));
        }
    }

    @Test
    public void parseValidatorTest() {
        Lexer lexer = new Lexer();
        Validator validator = lexer.Lex("regex(.*)");
        assertTrue(validator instanceof RegexValidator);
        assertEquals(".*", ((RegexValidator) validator).getRegexp());
        assertEquals("regex", validator.name());
    }

    @Test
    public void parseValidatorTest2() {
        Lexer lexer = new Lexer();
        Validator validator = lexer.Lex("isNumber(.*)");
        assertTrue(validator instanceof FuncValidator);
        assertEquals("isNumber", validator.name());
    }
}
