package com.es.phoneshop.model;

public class BaseEntity {
    private Long id;

    public BaseEntity(Long id) { }

    public BaseEntity() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
