package com.thanos.mockserver.parser;

import com.thanos.mockserver.lex.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class SchemaParser {

    private static final String BASE_PATH = "/schemas/";

    public List<Schema> parseReq(String schema) throws IOException {
        return parse(BASE_PATH + schema + "_req.csv");
    }

    public List<Schema> parseRes(String schema) throws IOException {
        return parse(BASE_PATH + schema + "_res.csv");
    }

    List<Schema> parse(String path) throws IOException {

        Reader in = new InputStreamReader(getClass().getResourceAsStream(path));

        Iterable<CSVRecord> records = CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .withIgnoreEmptyLines().withIgnoreSurroundingSpaces().withIgnoreHeaderCase()
                .parse(in);

        List<Schema> result = new LinkedList<>();
        Lexer lexer = new Lexer();

        for (CSVRecord record : records) {
            Integer id = Integer.valueOf(record.get("id").trim());
            String name = record.get("name").trim();
            String type = record.get("type").trim();
            Integer length = Integer.valueOf(record.get("length").trim());
            String validation = record.get("validation").trim();

            result.add(new Schema(id, name, type, length, lexer.Lex(validation)));
        }

        return result;
    }
}
