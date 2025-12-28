package com.location.voitures.dao;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
public interface GenericDAO<T, ID extends Serializable> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    Optional<T> findOneByAttribute(String attributeName, Object value);
    List<T> findAllByAttribute(String attributeName, Object value);
}
