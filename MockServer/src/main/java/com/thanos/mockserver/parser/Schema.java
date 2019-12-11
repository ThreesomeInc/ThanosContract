package com.thanos.mockserver.parser;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Schema implements Comparable<Schema> {

    private Integer id;
    private String name;
    private String type;
    private Integer length;
    private String regex;

    @Override
    public int compareTo(Schema anotherSchema) {
        return this.getId().compareTo(anotherSchema.getId());
    }
}
