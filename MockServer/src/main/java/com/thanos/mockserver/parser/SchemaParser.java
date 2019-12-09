package com.thanos.mockserver.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SchemaParser {

    public List<Schema> parse(String path) throws IOException {

        Reader in = new FileReader(path);

        Iterable<CSVRecord> records = CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .withIgnoreEmptyLines().withIgnoreSurroundingSpaces().withIgnoreHeaderCase()
                .parse(in);

        List<Schema> result = new ArrayList<>();

        for (CSVRecord record : records) {
            Integer id = Integer.valueOf(record.get("id").trim());
            String name = record.get("name").trim();
            String type = record.get("type").trim();
            Integer length = Integer.valueOf(record.get("length").trim());
            String regex = record.get("regex").trim();

            result.add(new Schema(id, name, type, length, regex));
        }

        return result;
    }
}
