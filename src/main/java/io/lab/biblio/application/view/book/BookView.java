package io.lab.biblio.application.view.book;

import com.vaadin.spring.annotation.SpringView;
import io.lab.biblio.framework.view.AbstractCrudView;
import io.lab.biblio.application.model.Book;

/**
 * Created by amazimpaka on 2018-03-23
 */
@SpringView(name = BookView.VIEW_NAME)
public class BookView extends AbstractCrudView<Book> {

    public static final String VIEW_NAME = "book-view";

}
