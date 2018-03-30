package io.lab.biblio.application.service;

import org.apache.http.HttpStatus;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by amazimpaka on 2018-03-30
 */
@Component
public class ElasticsearchManagerImpl extends ElasticsearchClient implements ElasticsearchManager {

    @Override
    public boolean createIndex(String index) throws IOException {

        if(index == null || index.trim().length() == 0){
            return false;
        }

        if (!indexExists(index)) {

            try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {
                final CreateIndexRequest request = new CreateIndexRequest(index);
                logger.debug("ES Create Index Request: {}", request);

                final CreateIndexResponse response = client.indices().create(request);
                logger.debug("ES Create Index Response: {}", response);

                // True if all Elasticsearch cluster nodes have acknowledged this index creation request
                return response.isAcknowledged();
            } catch (IOException e) {
                logger.warn("Failed to create index {}", index);
                throw e;
            }

        }

        return true;

    }

    @Override
    public boolean deleteIndex(String index) throws IOException {

        if(index == null || index.trim().length() == 0){
            return false;
        }


        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {

            final DeleteIndexRequest request = new DeleteIndexRequest(index);
            logger.debug("ES Delete Index Request: {}", request);

            final DeleteIndexResponse response = client.indices().delete(request);
            logger.debug("ES Delete Index Response: {}", response);

            return response.isAcknowledged();

        } catch (ElasticsearchException e) {

            if (e.status() == RestStatus.NOT_FOUND) {
                logger.info("Index: {} to be deleted does not exist", index);
                return true;
            } else {
                logger.warn("Failed to delete index: {}", index);
                throw e;
            }
        } catch (IOException e) {
            logger.warn("Failed to delete index: {}", index);
            throw e;
        }
    }

    @Override
    public boolean indexExists(String index) throws IOException {

        if(index == null || index.trim().length() == 0){
            return false;
        }


        // Use low-level client because this method no longer exists:
        //  client.indices().exists(request)
        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder)) {

            final Response response = client.getLowLevelClient().performRequest("HEAD", "/" + index);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;

        } catch (ResponseException e) {
            if (e.getResponse().getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return false;
            } else {
                logger.warn("Failed to check index {}", index);
                throw e;
            }
        } catch (IOException e) {
            logger.warn("Failed to check index {}", index);
            throw e;
        }
    }

}
