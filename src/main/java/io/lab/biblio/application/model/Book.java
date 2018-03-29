package io.lab.biblio.application.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by amazimpaka on 2018-03-23
 */
public class Book implements Item {

    private String id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @Min(0)
    private int publishYear;

    public Book() {
        super();
    }


    public Book(@NotNull String author) {
        this();
        this.author = author;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }
}
