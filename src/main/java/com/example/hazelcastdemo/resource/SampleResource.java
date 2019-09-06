package com.example.hazelcastdemo.resource;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleResource implements Serializable {
    private Long id;
    private String message;
}
