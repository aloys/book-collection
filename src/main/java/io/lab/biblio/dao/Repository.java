package io.lab.biblio.dao;

import java.io.Serializable;

/**
 * Created by amazimpaka on 2018-03-23
 */
public interface Repository<E,ID extends Serializable> {

    E find(ID id);

    E save(E entity);

    E update(E entity);

    E saveOrUpdate(E entity);

    void delete(E entity);

    void delete(ID id);
}
