package io.lab.biblio.framework.dao.elasticsearch;

import org.elasticsearch.action.index.IndexRequest;

/**
 * Created by amazimpaka on 2018-03-26
 */
public class IndexRequestBuilder<T> implements IndexBuilder<T,IndexRequest> {

    @Override
    public IndexRequest build(T entity) {
        return null;
    }
}
