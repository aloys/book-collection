package io.lab.biblio.application.service;

import io.lab.biblio.application.model.Item;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by amazimpaka on 2018-03-23
 */
public interface ElasticsearchService<E extends Item> {

    Optional<E> findById(Class<E> entityClass, String index, String type, String indexId) throws IOException;

    List<E> search(Class<E> entityClass, String index,String type) throws IOException;

    String create(E entity, String index, String type) throws IOException;

    String create(Map<String, Object> indexValues, String index, String type) throws IOException;

    void update(E entity, String index, String type) throws IOException;

    void update(Map<String, Object> indexValues, String index, String type,String indexId) throws IOException;

    void delete(E entity,String index,String type) throws IOException;

    void delete(String indexId,String index,String type) throws IOException;
}
