package com.thanos.mockserver.infrastructure.parser;

import com.thanos.mockserver.domain.NewSchema;
import com.thanos.mockserver.infrastructure.parser.dto.SchemaDTO;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;

@Slf4j
public class SchemaYamlParser {

    Yaml yaml = new Yaml(new Constructor(SchemaDTO.class));

    public List<NewSchema> parse(String path, String fileName) {
        return parse(path + fileName);
    }

    public List<NewSchema> parse(String fullPath) {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fullPath);

        final Iterable<Object> objects = yaml.loadAll(inputStream);

        return SchemaDTO.buildFrom(objects);
    }
}
