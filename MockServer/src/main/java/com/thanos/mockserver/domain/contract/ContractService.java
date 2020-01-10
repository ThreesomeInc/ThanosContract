package com.thanos.mockserver.domain.contract;

import com.thanos.mockserver.infrastructure.parser.FileParser;
import com.thanos.mockserver.infrastructure.parser.contract.NewContractParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ContractService {

    public List<NewContract> loadAllContracts() {
        final List<String> contractPaths = getAllContractPaths();
        final NewContractParser contractParser = new NewContractParser();

        return contractPaths.stream()
                .map(contractParser::parse)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    List<String> getAllContractPaths() {
        final URL resource = FileParser.class.getClassLoader().getResource("contracts/new");
        try {
            return Files.walk(Paths.get(resource.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("Fail to getAllContractPaths");
            return Collections.EMPTY_LIST;
        }
    }
}
