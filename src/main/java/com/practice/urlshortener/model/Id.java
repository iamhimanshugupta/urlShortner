package com.practice.urlshortener.model;

import lombok.Data;

@Data
public class Id {

    private String id;

    public Id() {
    }

    public Id(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Id{" +
                "id='" + id + '\'' +
                '}';
    }
}
