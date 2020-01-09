package com.thanos.mockserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewSchema {

    String id;
    String version;
    String name;
    LinkedList<Field> request;
    LinkedList<Field> response;
}
