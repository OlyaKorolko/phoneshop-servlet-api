package com.es.phoneshop.dao;

import com.es.phoneshop.exception.BaseEntityNotFoundException;
import com.es.phoneshop.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractDao<T extends BaseEntity> {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final List<T> objects;
    private long id;

    public List<T> getObjects() {
        return objects;
    }

    public AbstractDao() {
        this.objects = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getItem(Long id) {
        readWriteLock.readLock().lock();
        try {
            return objects.stream()
                    .filter((o -> o.getId().equals(id)))
                    .findAny()
                    .orElseThrow(() -> new BaseEntityNotFoundException("Base entity with id: " + id + " was not found"));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void save(T object) {
        readWriteLock.writeLock().lock();
        try {
            if (object != null) {
                object.setId(id++);
                objects.add(object);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
