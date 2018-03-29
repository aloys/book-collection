package io.lab.biblio.application.service;

import io.lab.biblio.application.exception.IndexNotFoundException;
import io.lab.biblio.application.model.Item;
import io.lab.biblio.framework.util.ReflectionUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.http.HttpHost.DEFAULT_SCHEME_NAME;

/**
 * Created by amazimpaka on 2018-03-23
 */
@Service
public class ElasticsearchServiceImpl<E extends Item> implements ElasticsearchService<E> {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);

    @Value("${elasticsearch.server.host:localhost}")
    private String host;

    @Value("${elasticsearch.server.port:9200}")
    private int port;

    private RestClientBuilder restClientBuilder;


    @PostConstruct
    public void initialize() {
        final HttpHost httpHost = new HttpHost(host, port, DEFAULT_SCHEME_NAME);
        restClientBuilder = RestClient.builder(httpHost);

        logger.info("Connected to Elasticsearch - host: {} / port: {}", host, port);
    }


    @Override
    public Optional<E> findById(Class<E> entityClass, String index, String type, String indexId) throws IOException {
        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {

            final GetRequest request = new GetRequest(index,type,indexId);
            logger.debug("ES Find by ID Request: {}", request);

            final GetResponse response = client.get(request);
            logger.debug("ES Find by ID Response: {}", response);

            if(!response.isExists()
                    || response.getSourceAsMap() == null
                    || response.getSourceAsMap().isEmpty()){

                throw new IndexNotFoundException(index,type,indexId);
            }

            // Write each search hit results into a given entity instance
            final E entity = ReflectionUtil.newInstance(entityClass);
            final Map<String, Object> values = response.getSourceAsMap();
            ReflectionUtil.writeValues(entity, values);
            entity.setId(response.getId());

            return Optional.ofNullable(entity);

        } catch (IOException e) {
            logger.warn("Failed to get from index {} of type: {} with id:", index, type,indexId);
            throw e;
        }

    }

    @Override
    public List<E> search(Class<E> entityClass, String index, String type) throws IOException {

        final List<E> results = new ArrayList<>();

        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {

            // Create a search request for a given index and type
            final SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.types(type);

            // Create a MATCH-ALL query - no search key
            final SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(builder);

            final SearchResponse searchResponse = client.search(searchRequest);
            searchResponse.getHits().forEach(searchHit -> {

                // Write each search hit results into a given entity instance
                final E entity = ReflectionUtil.newInstance(entityClass);
                final Map<String, Object> values = searchHit.getSourceAsMap();
                ReflectionUtil.writeValues(entity, values);
                entity.setId(searchHit.getId());
                results.add(entity);

            });


        } catch (IOException e) {
            logger.warn("Failed to create index {} of type: {}", index, type);
            throw e;
        }


        return results;
    }

    @Override
    public String create(E entity, String index, String type) throws IOException {

        // Loop around all fields of a given entity and
        // and field name / field values as input to be indexed in ElasticSearch
        // Only non-null fields will be indexed

        final Map<String, Object> indexValues = ReflectionUtil.readValues(entity);
        indexValues.forEach((key, value) -> {
            if (value != null) {
                indexValues.put(key, value);
            }
        });

        String indexId = create(indexValues, index, type);
        entity.setId(indexId);

        return indexId;

    }

    @Override
    public String create(Map<String, Object> indexValues, String index, String type) throws IOException {

        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {

            //Step 1: Create ElasticSearch index request
            final String indexId = String.valueOf(System.currentTimeMillis());
            final IndexRequest request = new IndexRequest(index, type, indexId).source(indexValues);
            logger.debug("ES Create Request: {}", request);

            //Step 2: Invoke ElasticSearch and return index response
            final IndexResponse response = client.index(request);
            logger.debug("ES Create Response: {}", response);

            return indexId;

        } catch (IOException e) {
            logger.warn("Failed to create index {} of type: {} with values: {}", index, type, indexValues);
            throw e;
        }

    }

    @Override
    public void update(E entity, String index, String type) throws IOException {
        // Loop around all fields of a given entity and
        // and field name / field values as input to be indexed in ElasticSearch
        // Only non-null fields will be indexed

        final Map<String, Object> indexValues = ReflectionUtil.readValues(entity);
        indexValues.forEach((key, value) -> {
            if (value != null) {
                indexValues.put(key, value);
            }
        });
        update(indexValues, index, type,entity.getId());
    }


    @Override
    public void update(Map<String, Object> indexValues, String index, String type,String indexId) throws IOException {

        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {

            //Step 1: Create ElasticSearch index request
            final UpdateRequest request = new UpdateRequest(index, type, indexId).doc(indexValues);
            logger.debug("ES Update Request: {}", request);

            //Step 2: Invoke ElasticSearch and return index response
            final UpdateResponse response = client.update(request);
            logger.debug("ES Update Response: {}", response);

        } catch (IOException e) {
            logger.warn("Failed to create index {} of type: {} with values: {}", index, type, indexValues);
            throw e;
        }

    }

    @Override
    public void delete(E entity, String index, String type) throws IOException {
        delete(entity.getId(),index,type);
    }

    @Override
    public void delete(String indexId, String index, String type) throws IOException {
        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {
            final DeleteRequest request = new DeleteRequest(index,type,indexId);
            logger.debug("ES Delete Request: {}", request);

            final DeleteResponse response = client.delete(request);
            logger.debug("ES Delete Response: {}", response);

        } catch (IOException e) {
            logger.warn("Failed to create index {} of type: {} with id: {}", index, type, indexId);
            throw e;
        }

    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}



