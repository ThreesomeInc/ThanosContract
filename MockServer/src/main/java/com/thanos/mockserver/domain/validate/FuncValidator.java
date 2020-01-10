package com.thanos.mockserver.domain.validate;

import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.Function;

public class FuncValidator implements Validator {
    private Function<String, Boolean> function;
    private String name;

    public FuncValidator(String name, Function<String, Boolean> function) {
        this.function = function;
        this.name = name;
    }

    /**
     * @see FuncValidatorTest#jsEngineFunctionTest()
     */
    @SuppressWarnings("unchecked")
    public static Validator createJsFunctionValidator(String jsExpr) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        Function<String, Boolean> f = (Function<String, Boolean>) engine.eval(
                String.format("new java.util.function.Function(%s)", jsExpr));
        return new FuncValidator("js-" + jsExpr.hashCode(), f);
    }

    /**
     * @see FuncValidatorTest#testLambdaFromString()
     */
    public static Validator createSingleParamLambdaValidator(String lambdaExpr) throws LambdaCreationException {
        LambdaFactory factory = LambdaFactory.get(LambdaFactoryConfiguration.get());// no imports, seems rubbish? ^_^
        Function<String, Boolean> f = factory.createLambda(lambdaExpr, new TypeReference<Function<String, Boolean>>() {
        });
        return new FuncValidator("lambda-" + lambdaExpr.hashCode(), f);
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
