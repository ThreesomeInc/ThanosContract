package com.thanos.mockserver.parser;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;

@Slf4j
public class ContractParser {

    Yaml yaml = new Yaml(new Constructor(Contract.class));

    public List<Contract> parse(String path, String fileName) {

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(path + fileName);

        final Iterable<Object> contractList = yaml.loadAll(inputStream);

        return Contract.buildFrom(contractList);
    }

}
