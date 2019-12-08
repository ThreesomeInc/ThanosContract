package com.thanos.mockserver.parser;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Schema {

    Integer id;
    String name;
    String type;
    Integer length;
    String regex;
}
