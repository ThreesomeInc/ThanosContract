package com.thanos.mockserver.parser;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Schema {

    private Integer id;
    private String name;
    private String type;
    private Integer length;
    private String regex;
}
