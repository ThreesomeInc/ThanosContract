package com.thanos.mockserver.infrastructure.parser;

import com.thanos.mockserver.exception.ParseException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileParser {

    public static List<Path> listDirectory(String path) {
        try {
            final URL resource = FileParser.class.getClassLoader().getResource(path);
            if (resource != null) {
                return Files.list(Paths.get(resource.toURI())).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            throw new ParseException("IO Exception when list files", e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new ParseException("URI Syntax Exception when get path to URI", e);
        }

    }

    public static boolean isDiretory(String path) {
        try {
            final URL resource = FileParser.class.getClassLoader().getResource(path);
            if (resource != null) {
                return Files.isDirectory(Paths.get(resource.toURI()));
            } else {
                throw new ParseException("Fail to get URL for specify path.");
            }
        } catch (URISyntaxException e) {
            throw new ParseException("URI Syntax Exception when get path to URI", e);
        }
    }
}
