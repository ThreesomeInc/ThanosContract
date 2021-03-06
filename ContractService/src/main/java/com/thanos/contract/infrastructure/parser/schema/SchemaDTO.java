package com.thanos.contract.infrastructure.parser.schema;

import com.thanos.contract.domain.Schema;
import com.thanos.contract.domain.SchemaField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SchemaDTO {

    String version;
    String name;
    String provider;
    List<FragmentDTO> header;
    List<FragmentDTO> request;
    List<FragmentDTO> response;

    public static List<Schema> buildFrom(Iterable<Object> ymlResult) {
        final List<Schema> result = new LinkedList<>();
        for (Object record : ymlResult) {
            if (record instanceof SchemaDTO) {
                result.add(((SchemaDTO) record).toSchema());
            }
        }
        return result;
    }

    public Schema toSchema() {

        final LinkedList<SchemaField> headerFields = header.stream()
                .flatMap(fragmentDTO -> fragmentDTO.getFields().stream().map(FieldDTO::toField))
                .collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<SchemaField> requestFields = request.stream()
                .flatMap(fragmentDTO -> fragmentDTO.getFields().stream().map(FieldDTO::toField))
                .collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<SchemaField> responseFields = response.stream()
                .flatMap(fragmentDTO -> fragmentDTO.getFields().stream().map(FieldDTO::toField))
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<SchemaField> mergedRequestFields = new LinkedList<>(headerFields);
        mergedRequestFields.addAll(requestFields);

        LinkedList<SchemaField> mergedResponseFields = new LinkedList<>(headerFields);
        mergedResponseFields.addAll(responseFields);

        return new Schema(this.provider.trim().toUpperCase(),
                this.version.trim().toUpperCase(), this.name.trim().toUpperCase(),
                mergedRequestFields, mergedResponseFields);
    }
}
