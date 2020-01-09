package com.thanos.mockserver.infrastructure.parser.dto;

import com.thanos.mockserver.domain.Field;
import com.thanos.mockserver.domain.NewSchema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SchemaDTO {

    String version;
    String name;
    List<FragmentDTO> header;
    List<FragmentDTO> request;
    List<FragmentDTO> response;

    public static List<NewSchema> buildFrom(Iterable<Object> ymlResult) {
        final List<NewSchema> result = new LinkedList<>();
        for (Object record : ymlResult) {
            if (record instanceof SchemaDTO) {
                result.add(((SchemaDTO) record).toSchema());
            }
        }
        return result;
    }

    public NewSchema toSchema() {

        final LinkedList<Field> headerFields = header.stream()
                .flatMap(fragmentDTO -> fragmentDTO.getFields().stream().map(FieldDTO::toField))
                .collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<Field> requestFields = request.stream()
                .flatMap(fragmentDTO -> fragmentDTO.getFields().stream().map(FieldDTO::toField))
                .collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<Field> responseFields = response.stream()
                .flatMap(fragmentDTO -> fragmentDTO.getFields().stream().map(FieldDTO::toField))
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<Field> mergedRequestFields = new LinkedList<>(headerFields);
        mergedRequestFields.addAll(requestFields);

        LinkedList<Field> mergedResponseFields = new LinkedList<>(headerFields);
        mergedResponseFields.addAll(responseFields);

        return new NewSchema(
                UUID.randomUUID().toString(), this.version, this.name,
                mergedRequestFields, mergedResponseFields);
    }
}
