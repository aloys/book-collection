package io.lab.biblio;

import io.lab.biblio.application.exception.IndexNotFoundException;
import io.lab.biblio.application.model.Book;
import io.lab.biblio.application.service.ElasticsearchServiceImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by amazimpaka on 2018-03-28
 */
public class ElasticsearchServiceImplIntegrationTest {

    public static final String INDEX = "library-test";

    public static final String TYPE = "book";

    private ElasticsearchServiceImpl elasticsearchService = new ElasticsearchServiceImpl();

    private Book book;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //Collects or indexes created in each test, and clean them after test execution
    private final List<String> removableIndexes = new ArrayList<>();

    @Before
    public void setup() {
        elasticsearchService.setHost("localhost");
        elasticsearchService.setPort(9200);
        elasticsearchService.initialize();

        book = new Book();
        book.setTitle("Title 01");
        book.setAuthor("Author 01");
    }

    @After
    public void cleanup() {
        //Clean all created indexes in test
        removableIndexes.forEach(indexId -> {
            try {
                elasticsearchService.delete(indexId, INDEX, TYPE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    public void create() throws Exception {

        String id = elasticsearchService.create(book, INDEX, TYPE);
        removableIndexes.add(id);

        final Optional<Book> foundBook = elasticsearchService.findById(Book.class, INDEX, TYPE, id);
        Assert.assertTrue(foundBook.isPresent());

        Assert.assertEquals(book.getId(), foundBook.get().getId());
        Assert.assertEquals(book.getAuthor(), foundBook.get().getAuthor());
        Assert.assertEquals(book.getTitle(), foundBook.get().getTitle());
    }


    @Test
    public void update() throws Exception {

        String id = elasticsearchService.create(book, INDEX, TYPE);
        removableIndexes.add(id);

        book.setAuthor("Author 2");
        book.setTitle("Title 2");

        elasticsearchService.update(book, INDEX, TYPE);
        final Optional<Book> foundBook = elasticsearchService.findById(Book.class, INDEX, TYPE, id);
        Assert.assertTrue(foundBook.isPresent());

        Assert.assertEquals(book.getId(), foundBook.get().getId());
        Assert.assertEquals(book.getAuthor(), foundBook.get().getAuthor());
        Assert.assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void findAll() throws Exception {

        //Clean all indexes
        elasticsearchService.search(Book.class, INDEX, TYPE)
                .forEach(item -> {
                    try {
                        elasticsearchService.delete((Book) item, INDEX, TYPE);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });


        final List<String> identifiers = new ArrayList<>();
        identifiers.add(elasticsearchService.create(new Book("Book 101"), INDEX, TYPE));
        identifiers.add(elasticsearchService.create(new Book("Book 202"), INDEX, TYPE));
        identifiers.add(elasticsearchService.create(new Book("Book 303"), INDEX, TYPE));
        identifiers.add(elasticsearchService.create(new Book("Book 404"), INDEX, TYPE));
        identifiers.add(elasticsearchService.create(new Book("Book 505"), INDEX, TYPE));

        removableIndexes.addAll(identifiers);

        //Wait for deletion and creation
        Thread.sleep(3000);

        final List<Book> books = elasticsearchService.search(Book.class, INDEX, TYPE);
        Assert.assertEquals(5, books.size());
        books.forEach(book -> Assert.assertTrue(identifiers.contains(book.getId())));
    }


    @Test
    public void delete() throws Exception {

        //Expected exception to be thrown when trying to find
        // a deleted index
        expectedException.expect(IndexNotFoundException.class);

        String id = elasticsearchService.create(book, INDEX, TYPE);

        Optional<Book> foundBook = elasticsearchService.findById(Book.class, INDEX, TYPE, id);
        Assert.assertTrue(foundBook.isPresent());

        elasticsearchService.delete(book, INDEX, TYPE);

        //Wait for deletion
        Thread.sleep(3000);

        // Try to find deleted index -> expected: IndexNotFoundException
        elasticsearchService.findById(Book.class, INDEX, TYPE, id);

    }


}
