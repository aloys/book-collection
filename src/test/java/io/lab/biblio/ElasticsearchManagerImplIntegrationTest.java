package io.lab.biblio;

import io.lab.biblio.application.model.Book;
import io.lab.biblio.application.service.ElasticsearchManagerImpl;
import io.lab.biblio.application.service.ElasticsearchServiceImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by amazimpaka on 2018-03-30
 */
public class ElasticsearchManagerImplIntegrationTest {

    public static final String INDEX = "library-test";

    private ElasticsearchManagerImpl elasticsearchManager = new ElasticsearchManagerImpl();


    @Before
    public void setup() throws IOException {
        elasticsearchManager.setHost("localhost");
        elasticsearchManager.setPort(9200);
        elasticsearchManager.initialize();

    }

    @After
    public void cleanup() throws IOException {
        elasticsearchManager.deleteIndex(INDEX);
    }


    @Test
    public void createIndex() throws IOException {
        elasticsearchManager.deleteIndex(INDEX);

        // Create a new index
        boolean result = elasticsearchManager.createIndex(INDEX);
        Assert.assertTrue(result);
        Assert.assertTrue(elasticsearchManager.indexExists(INDEX));

        //Recreate an existing index
        Assert.assertTrue(elasticsearchManager.createIndex(INDEX));

        //Create a null or empty index
        Assert.assertFalse(elasticsearchManager.createIndex(null));
        Assert.assertFalse(elasticsearchManager.createIndex(""));
        Assert.assertFalse(elasticsearchManager.createIndex("  "));
    }

    @Test
    public void deleteIndex() throws IOException {
        elasticsearchManager.createIndex(INDEX);

        boolean result =  elasticsearchManager.deleteIndex(INDEX);
        Assert.assertTrue(result);
        Assert.assertFalse(elasticsearchManager.indexExists(INDEX));

        //Delete an unexisting index
        Assert.assertTrue(elasticsearchManager.deleteIndex(INDEX));

        //Delete a null or empty index
        Assert.assertFalse(elasticsearchManager.deleteIndex(null));
        Assert.assertFalse(elasticsearchManager.deleteIndex(""));
        Assert.assertFalse(elasticsearchManager.deleteIndex("  "));
    }


    @Test
    public void indexExists() throws IOException {

        // Check before creation
        Assert.assertFalse(elasticsearchManager.indexExists(INDEX));

        // Check after creation
        elasticsearchManager.createIndex(INDEX);
        Assert.assertTrue(elasticsearchManager.indexExists(INDEX));


        // Check after deletion
        elasticsearchManager.deleteIndex(INDEX);
        Assert.assertFalse(elasticsearchManager.indexExists(INDEX));

        //Check a null or empty index
        Assert.assertFalse(elasticsearchManager.indexExists(null));
        Assert.assertFalse(elasticsearchManager.indexExists(""));
        Assert.assertFalse(elasticsearchManager.indexExists("  "));


    }


}
