package io.lab.biblio.framework.service;

import io.lab.biblio.framework.model.Item;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by amazimpaka on 2018-03-23
 */
public interface ItemManagerService<E extends Item> {

    List<E> search(Class<E> entityClass, String index,String type) throws IOException;

    String create(E entity, String index, String type) throws IOException;

    String create(Map<String, Object> indexValues, String index, String type) throws IOException;

    void update(E entity, String index, String type) throws IOException;

    void update(Map<String, Object> indexValues, String index, String type,String indexId) throws IOException;

    void delete(E entity,String index,String type) throws IOException;

    void delete(String indexId,String index,String type) throws IOException;
}
