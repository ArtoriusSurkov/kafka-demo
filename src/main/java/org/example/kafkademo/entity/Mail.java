package org.example.kafkademo.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class Mail {
    private String to;
    private String subject;
    private String body;
}
