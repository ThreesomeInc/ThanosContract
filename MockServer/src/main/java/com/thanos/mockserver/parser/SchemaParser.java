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
                .withFirstRecordAsHeader().withIgnoreEmptyLines().parse(in);

        List<Schema> result = new ArrayList<>();

        for (CSVRecord record : records) {
            Integer id = Integer.valueOf(record.get("id"));
            String name = record.get("name");
            String type = record.get("type");
            Integer length = Integer.valueOf(record.get("length"));
            String regex = record.get("regex");

            result.add(new Schema(id, name, type, length, regex));
        }

        return result;
    }
}
