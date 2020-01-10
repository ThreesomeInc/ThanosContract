package com.thanos.mockserver.infrastructure.parser.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FragmentDTO {

    String name;
    String version;
    LinkedList<FieldDTO> fields;
}
