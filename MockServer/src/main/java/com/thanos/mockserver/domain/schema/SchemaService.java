package com.thanos.mockserver.domain.schema;

import com.thanos.mockserver.infrastructure.parser.schema.SchemaYamlParser;
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
public class SchemaService {

    public List<Schema> loadAllSchemas() {
        final List<String> schemaPaths = getAllSchemaPaths();
        final SchemaYamlParser schemaYamlParser = new SchemaYamlParser();
        return schemaPaths.stream()
                .map(schemaYamlParser::parse)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    List<String> getAllSchemaPaths() {
        final URL resource = SchemaService.class.getClassLoader().getResource("schemas");
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
