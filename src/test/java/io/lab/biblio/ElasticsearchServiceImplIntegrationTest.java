package io.lab.biblio;

import io.lab.biblio.application.exception.IndexNotFoundException;
import io.lab.biblio.application.model.Book;
import io.lab.biblio.application.service.ElasticsearchManagerImpl;
import io.lab.biblio.application.service.ElasticsearchServiceImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.*;

/**
 * Created by amazimpaka on 2018-03-28
 */
public class ElasticsearchServiceImplIntegrationTest {

    public static final String INDEX = "library-test";

    public static final String TYPE = "book";

    private static final String INDEX_ID = UUID.randomUUID().toString();

    private ElasticsearchServiceImpl elasticsearchService = new ElasticsearchServiceImpl();

    private ElasticsearchManagerImpl elasticsearchManager = new ElasticsearchManagerImpl();

    private Book book;
    private final Book nullBook = null;
    private final Map<String, Object> nullMap = null;
    private final Map<String, Object> emptyMap = new HashMap<>();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //Collects or indexes created in each test, and clean them after test execution
    private final List<String> removableIndexes = new ArrayList<>();

    @Before
    public void setup() throws IOException {
        elasticsearchManager.setHost("localhost");
        elasticsearchManager.setPort(9200);
        elasticsearchManager.initialize();


        elasticsearchService.setHost("localhost");
        elasticsearchService.setPort(9200);
        elasticsearchService.initialize();

        book = new Book();
        book.setTitle("Title 01");
        book.setAuthor("Author 01");

        elasticsearchManager.deleteIndex(INDEX);
    }

    @After
    public void cleanup() throws IOException {
        //Clean all created indexes in test
        removableIndexes.forEach(indexId -> {
            try {
                elasticsearchService.delete(indexId, INDEX, TYPE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        elasticsearchManager.deleteIndex(INDEX);
    }


    @Test
    public void create() throws Exception {

        final String id = elasticsearchService.create(book, INDEX, TYPE);
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
    public void search() throws Exception {

        elasticsearchManager.createIndex(INDEX);

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


        // Try to find deleted index -> expected: IndexNotFoundException
        elasticsearchService.findById(Book.class, INDEX, TYPE, id);

    }


    @Test
    public void createInvalidValues() throws Exception {

        // Case 1: valid book
        testInvalidArgument(() -> elasticsearchService.create(book, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.create(book, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.create(book, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.create(book, " ", TYPE));

        // Case 2: null book
        testInvalidArgument(() -> elasticsearchService.create(nullBook, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.create(nullBook, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.create(nullBook, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.create(nullBook, " ", TYPE));

        // Case 3: null map
        testInvalidArgument(() -> elasticsearchService.create(nullMap, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.create(nullMap, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.create(nullMap, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.create(nullMap, " ", TYPE));

        // Case 4: empty map
        testInvalidArgument(() -> elasticsearchService.create(emptyMap, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.create(emptyMap, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.create(emptyMap, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.create(emptyMap, " ", TYPE));
    }

    @Test
    public void updateInvalidValues() throws Exception {
        // Case 1: valid book
        testInvalidArgument(() -> elasticsearchService.update(book, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.update(book, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.update(book, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.update(book, " ", TYPE));

        // Case 2: null book
        testInvalidArgument(() -> elasticsearchService.update(nullBook, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.update(nullBook, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.update(nullBook, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.update(nullBook, " ", TYPE));

        // Case 3: null map
        testInvalidArgument(() -> elasticsearchService.update(nullMap, INDEX, null, INDEX_ID));
        testInvalidArgument(() -> elasticsearchService.update(nullMap, INDEX, "", INDEX_ID));
        testInvalidArgument(() -> elasticsearchService.update(nullMap, null, TYPE, INDEX_ID));
        testInvalidArgument(() -> elasticsearchService.update(nullMap, " ", TYPE, INDEX_ID));

        // Case 4: empty map
        testInvalidArgument(() -> elasticsearchService.update(emptyMap, INDEX, null, INDEX_ID));
        testInvalidArgument(() -> elasticsearchService.update(emptyMap, INDEX, "", INDEX_ID));
        testInvalidArgument(() -> elasticsearchService.update(emptyMap, null, TYPE, INDEX_ID));
        testInvalidArgument(() -> elasticsearchService.update(emptyMap, " ", TYPE, INDEX_ID));

        // Case 5: Invalid index id
        testInvalidArgument(() -> elasticsearchService.update(Collections.singletonMap("key01", "value01")
                , INDEX, TYPE, null));
        testInvalidArgument(() -> elasticsearchService.update(Collections.singletonMap("key01", "value01")
                , INDEX, TYPE, " "));
    }


    @Test
    public void deleteInvalidValues() throws Exception {

        // Case 1: valid book
        testInvalidArgument(() -> elasticsearchService.delete(book, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.delete(book, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.delete(book, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.delete(book, " ", TYPE));

        // Case 2: null book
        testInvalidArgument(() -> elasticsearchService.delete(nullBook, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.delete(nullBook, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.delete(nullBook, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.delete(nullBook, " ", TYPE));

        // Case 3: valid index
        testInvalidArgument(() -> elasticsearchService.delete(INDEX_ID, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.delete(INDEX_ID, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.delete(INDEX_ID, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.delete(INDEX_ID, " ", TYPE));

        // Case 4: invalid index
        String nullIndexId = null;
        testInvalidArgument(() -> elasticsearchService.delete(nullIndexId, INDEX, null));
        testInvalidArgument(() -> elasticsearchService.delete(nullIndexId, INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.delete(nullIndexId, null, TYPE));
        testInvalidArgument(() -> elasticsearchService.delete(nullIndexId, " ", TYPE));

        testInvalidArgument(() -> elasticsearchService.delete(" ", INDEX, null));
        testInvalidArgument(() -> elasticsearchService.delete(" ", INDEX, ""));
        testInvalidArgument(() -> elasticsearchService.delete(" ", null, TYPE));
        testInvalidArgument(() -> elasticsearchService.delete(" ", " ", TYPE));
    }

    @Test
    public void searchInvalidValues() throws Exception {
        testInvalidArgument(() -> elasticsearchService.search(Book.class, null, null));
        testInvalidArgument(() -> elasticsearchService.search(Book.class, null, " "));
        testInvalidArgument(() -> elasticsearchService.search(Book.class, " ", null));
        testInvalidArgument(() -> elasticsearchService.search(Book.class, " ", " "));

        testInvalidArgument(() -> elasticsearchService.search(null, null, null));
        testInvalidArgument(() -> elasticsearchService.search(null, null, " "));
        testInvalidArgument(() -> elasticsearchService.search(null, " ", null));
        testInvalidArgument(() -> elasticsearchService.search(null, " ", " "));
    }

    @Test
    public void findByIdInvalidValues() throws Exception{
        testInvalidArgument(() -> elasticsearchService.findById(null, null, null, null));

        testInvalidArgument(() -> elasticsearchService.findById(Book.class, null, null, null));
        testInvalidArgument(() -> elasticsearchService.findById(Book.class, null, null, " "));
        testInvalidArgument(() -> elasticsearchService.findById(Book.class, null, null, book.getId()));

        testInvalidArgument(() -> elasticsearchService.findById(Book.class, null, " ", null));
        testInvalidArgument(() -> elasticsearchService.findById(Book.class, null, " ", " "));
        testInvalidArgument(() -> elasticsearchService.findById(Book.class, null, " ", book.getId()));

        testInvalidArgument(() -> elasticsearchService.findById(Book.class, " ", " ", null));
        testInvalidArgument(() -> elasticsearchService.findById(Book.class, " ", " ", " "));
        testInvalidArgument(() -> elasticsearchService.findById(Book.class, " ", " ", book.getId()));

    }


    private void testInvalidArgument(InvalidArgumentInvoker invoker) throws Exception {
        Assert.assertNotNull(invoker);

        try {
            invoker.invoke();
            Assert.fail("Unreachable line");
        } catch (IllegalArgumentException e) {

        }
    }

    private interface InvalidArgumentInvoker {

        void invoke() throws Exception;

    }


}
