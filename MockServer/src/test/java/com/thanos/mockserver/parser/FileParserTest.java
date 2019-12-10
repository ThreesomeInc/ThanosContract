package com.thanos.mockserver.parser;

import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FileParserTest {

    @Test
    public void listDirectory() {
        final List<Path> paths = FileParser.listDirectory("contracts");
        paths.forEach(System.out::println);
    }

    @Test
    public void isDiretory() {
        assertTrue(FileParser.isDiretory("contracts"));
    }
}