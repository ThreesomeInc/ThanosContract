package com.thanos.mockserver.infrastructure.parser;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FileParserTest {

    @Test
    public void listDirectory() throws IOException {
        final List<Path> paths = FileParser.listDirectory("schemas/new");
        paths.forEach(System.out::println);
    }

    @Test
    public void isDiretory() {
        assertTrue(FileParser.isDiretory("contracts"));
    }
}