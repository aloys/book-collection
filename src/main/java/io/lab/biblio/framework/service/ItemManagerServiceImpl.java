package io.lab.biblio.framework.service;

import io.lab.biblio.framework.model.Item;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by amazimpaka on 2018-03-23
 */
@Service
public class ItemManagerServiceImpl<E extends Item> implements ItemManagerService<E>{

    @Override
    public List<E> findlAll() {
        return Collections.emptyList();
    }

    @Override
    public E save(E entity) {
        return null;
    }

    @Override
    public void delete(E entity) {

    }
}
