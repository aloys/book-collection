package io.lab.biblio.application.service;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import static org.apache.http.HttpHost.DEFAULT_SCHEME_NAME;

/**
 * Created by amazimpaka on 2018-03-30
 */
public class ElasticsearchClient {

    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${elasticsearch.server.host:localhost}")
    private String host;

    @Value("${elasticsearch.server.port:9200}")
    private int port;

    protected RestClientBuilder restClientBuilder;


    @PostConstruct
    public void initialize() {
        final HttpHost httpHost = new HttpHost(host, port, DEFAULT_SCHEME_NAME);
        restClientBuilder = RestClient.builder(httpHost);

        logger.info("Connected to Elasticsearch - host: {} / port: {}", host, port);
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
