package io.lab.biblio.application.service;

import java.io.IOException;

/**
 * Created by amazimpaka on 2018-03-30
 */
public interface ElasticsearchManager {

    /**
     *
     * Create a given Elasticsearch index
     *
     * @param index Elasticsearch index to be created
     * @return true if index was successfully created, false otherwise
     * @throws IOException IOException I/O Exception while calling Elasticsearch
     */
    boolean createIndex(String index) throws IOException;

    /**
     *
     * Delete a given Elasticsearch index
     *
     * @param index Elasticsearch index to be deleted
     * @return true if index was successfully deleted or does not exists, false otherwise
     * @throws IOException IOException I/O Exception while calling Elasticsearch
     */
    boolean deleteIndex(String index) throws IOException;

    /**
     *  Check if a given Elasticsearch index exists
     *
     * @param index Elasticsearch index to be found
     * @return true if index exists, false otherwise
     * @throws IOException IOException IOException I/O Exception while calling Elasticsearch
     */
    boolean indexExists(String index) throws IOException;

}
