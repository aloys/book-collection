package io.lab.biblio.framework.dao.elasticsearch;

/**
 * Created by amazimpaka on 2018-03-26
 */
public interface IndexBuilder<E,R> {

    R build(E entity);
}
