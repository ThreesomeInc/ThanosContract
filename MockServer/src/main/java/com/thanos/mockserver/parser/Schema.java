package com.thanos.mockserver.parser;

import com.thanos.mockserver.validate.Validator;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"validator"})
public class Schema implements Comparable<Schema> {

    private Integer id;
    private String name;
    private String type;
    private Integer length;
    private Validator validator;

    @Override
    public int compareTo(Schema anotherSchema) {
        return this.getId().compareTo(anotherSchema.getId());
    }
}
