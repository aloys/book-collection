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

    /**
     * Find a given index result from Elasticsearch by its id.
     *
     * @param entityClass result entity type
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @param indexId index id
     * @return a entity of type <E>
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    Optional<E> findById(Class<E> entityClass, String index, String type, String indexId) throws IOException;

    /**
     * Find all indexes currently saved in Elasticsearch
     *
     * @param entityClass result entity type
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @return list of entities of type <E>
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    List<E> search(Class<E> entityClass, String index,String type) throws IOException;


    /**
     *  Create a index in Elasticsearch. All fields of this entity will be indexed
     *  with entity field name as index field name and entity field value as index field value
     *
     *  field_name: field_value
     *
     *  Only non-null value fields will be considered.
     *
     * @param entity entity whose content is to be indexed
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @return newly created index ID
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    String create(E entity, String index, String type) throws IOException;


    /**
     *  Create a index in Elasticsearch from map value using map key  as index field name and
     *  map value as index field value
     *
     *  field_name (key): field_value (value)
     *
     *  Only non-null value fields will be considered.
     *
     * @param indexValues key value map content to be indexed
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @return newly created index ID
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    String create(Map<String, Object> indexValues, String index, String type) throws IOException;

    /**
     *  Update a given a index in Elasticsearch. All fields of this entity will be indexed
     *  with entity field name as index field name and entity field value as index field value
     *
     *  field_name: field_value
     *
     *  Only non-null value fields will be considered.
     *
     * @param entity entity whose content is to be updated
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    void update(E entity, String index, String type) throws IOException;

    /**
     *  Create a index in Elasticsearch from map value using map key  as index field name and
     *  map value as index field value
     *
     *  field_name (key): field_value (value)
     *
     *  Only non-null value fields will be considered.
     *
     * @param indexValues key value map content to be updated
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @param indexId Elasticsearch index ID
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    void update(Map<String, Object> indexValues, String index, String type,String indexId) throws IOException;

    /**
     * Create a given entity index in Elasticsearch
     *
     * @param entity entity whose content is to be deleted
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    void delete(E entity,String index,String type) throws IOException;

    /**
     * Create an entity with a given index ID in Elasticsearch
     *
     * @param indexId  ID of an index to be deleted
     * @param index Elasticsearch index
     * @param type Elasticsearch type
     * @throws IOException I/O Exception while calling Elasticsearch
     */
    void delete(String indexId,String index,String type) throws IOException;
}
