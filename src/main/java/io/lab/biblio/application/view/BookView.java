package io.lab.biblio.application.view;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import io.lab.biblio.application.model.Book;
import io.lab.biblio.application.service.ElasticsearchManager;
import io.lab.biblio.framework.view.AbstractCrudView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amazimpaka on 2018-03-23
 */
@SpringView(name = BookView.VIEW_NAME)
public class BookView extends AbstractCrudView<Book> {

    public static final String VIEW_NAME = "book-view";

    public static final String ELASTICSEARCH_INDEX = "library";

    public static final String ELASTICSEARCH_TYPE = "book";


    @Resource
    private ElasticsearchManager elasticsearchManager;


    @PostConstruct
    public void initialize() {

        try {
            //Try to create Elasticsearch index
            elasticsearchManager.createIndex(ELASTICSEARCH_INDEX);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }

        super.initialize();
        //Table columns in sorted order
        grid.setColumns("id","title","author");
    }

    @Override
    protected void refresh() {
        try {
            List<Book> search = service.search(Book.class, ELASTICSEARCH_INDEX, ELASTICSEARCH_TYPE);
            grid.setItems(search);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            NOTIFICATION_MANAGER.showErrorMessage("Failed to search books");
        }
        hideFrom();
    }

    protected void delete() {
        final Book selected = (Book) grid.asSingleSelect().getValue();
        try {
            if (selected != null) {
                service.delete(selected.getId(), ELASTICSEARCH_INDEX, ELASTICSEARCH_TYPE);
                refresh();
                NOTIFICATION_MANAGER.showTrayMessage(String.format("%s with id: %s was deleted", entityClass.getSimpleName(), selected.getId()));
            } else {
                NOTIFICATION_MANAGER.showWarnMessage(String.format("Please select a %s to delete", entityClass.getSimpleName()));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            NOTIFICATION_MANAGER.showErrorMessage("Failed to search books");
        }
    }

    @Override
    protected void save() {
        logger.debug("Executing create item");

        final Book book = getBinder().getBean();
        try {

            validate(book);

            if(book.getId() == null || book.getId().trim().length() == 0){
                service.create(book, ELASTICSEARCH_INDEX, ELASTICSEARCH_TYPE);
            }else{
                service.update(book, ELASTICSEARCH_INDEX, ELASTICSEARCH_TYPE);
            }


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            NOTIFICATION_MANAGER.showErrorMessage(String.format("Failed to create book: %s", book.getTitle()));
        }

        refresh();

        NOTIFICATION_MANAGER.showTrayMessage(String.format("%s with id: %s was saved", entityClass.getSimpleName(), book.getAuthor()));
    }

    @Override
    protected List<Component> createFormFields() {
        final List<Component> components = new ArrayList<>();

        final TextField idField = new TextField("Id");
        idField.setReadOnly(true);
        bindField(idField, "id", String.class, String.class);
        components.add(idField);

        final TextField titleField = new TextField("Title");
        bindField(titleField, "title", String.class, String.class);
        components.add(titleField);

        final TextField authorField = new TextField("Author");
        bindField(authorField, "author", String.class, String.class);
        components.add(authorField);

        return components;
    }

}
