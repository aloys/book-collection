package io.lab.biblio.application.model;

import io.lab.biblio.framework.model.Item;

import java.util.Date;

/**
 * Created by amazimpaka on 2018-03-23
 */
public class Book implements Item<Long> {

    private Long id;

    private String title;

    private String author;

    private Date publishDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
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

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
