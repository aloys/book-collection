package io.lab.biblio.framework.service;

import io.lab.biblio.framework.model.Item;

import java.util.List;

/**
 * Created by amazimpaka on 2018-03-23
 */
public interface ItemManagerService<E extends Item> {

    List<E> findlAll();

    E save(E entity);

    void delete(E entity);
}
