package com.thanos.mockserver.infrastructure.parser.contract;

import com.thanos.mockserver.domain.contract.NewContract;
import com.thanos.mockserver.infrastructure.parser.FileParser;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class NewContractParserTest {

    @Test
    public void parse() {

        final NewContractParser newContractParser = new NewContractParser();

        final List<NewContract> result =
                newContractParser.parse("contracts/", "10400_test.yml");

        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("10400", result.get(0).getSchemaName());
        assertEquals("20200101", result.get(0).getSchemaVersion());
    }

    @Test
    public void parse_with_full_path() {

        final NewContractParser newContractParser = new NewContractParser();

        final List<String> contractPaths = getAllContractPaths();
        System.out.println(contractPaths);

        final List<NewContract> result =
                newContractParser.parse(contractPaths.get(0));

        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("10400", result.get(0).getSchemaName());
        assertEquals("20200101", result.get(0).getSchemaVersion());
    }

    List<String> getAllContractPaths() {
        final URL resource = FileParser.class.getClassLoader().getResource("contracts/new");
        try {
            return Files.walk(Paths.get(resource.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }

    }
}