package com.semenov.core.data.accessobjects;

import java.math.BigDecimal;
import java.util.List;

public interface CRUD<T extends Object> {
    public List<T> list();
    public T find(BigDecimal id);

    public void add(T entity);
    public void update(T entity);
    public void delete(T entity);
}
