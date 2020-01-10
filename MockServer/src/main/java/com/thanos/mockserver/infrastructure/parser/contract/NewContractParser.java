package com.thanos.mockserver.infrastructure.parser.contract;

import com.thanos.mockserver.domain.contract.NewContract;
import com.thanos.mockserver.exception.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class NewContractParser {

    Yaml yaml = new Yaml(new Constructor(ContractDTO.class));

    public List<NewContract> parse(String path, String fileName) {
        return parse(path + fileName);
    }

    public List<NewContract> parse(String fullPath) {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(fullPath));
            final Iterable<Object> contractList = yaml.loadAll(inputStream);
            return ContractDTO.buildFrom(contractList);
        } catch (IOException e) {
            throw new ParseException("IO Exception when access contract resource", e.getCause());
        }
    }

}
