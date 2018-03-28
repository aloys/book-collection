package io.lab.biblio.application.model;

import io.lab.biblio.framework.model.Item;

import java.util.Date;

/**
 * Created by amazimpaka on 2018-03-23
 */
public class Book implements Item {

    private String id;

    private String title;

    private String author;

    private int publishYear;

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
