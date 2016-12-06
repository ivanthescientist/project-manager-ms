package com.isc.project.manager.persistence.repository;

import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseRepository <T, ID extends Serializable> {
    Optional<T> findOne(ID id);
    List<T> findAll();
    List<T> findAll(Iterable<ID> ids);
    void delete(ID id);
    void delete(T entity);
    void deleteAll();
    T save(T entity);
    void flush();
}
