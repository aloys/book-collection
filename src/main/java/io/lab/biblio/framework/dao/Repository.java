package io.lab.biblio.framework.dao;

import io.lab.biblio.framework.model.Item;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amazimpaka on 2018-03-23
 */
public interface Repository<E extends Item,ID extends Serializable> {

    List<E> findlAll();

    E find(ID id);

    E save(E entity);

    E update(E entity);

    E saveOrUpdate(E entity);

    void delete(E entity);

    void delete(ID id);
}
