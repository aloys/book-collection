
# Elasticsearch Book Collection

## 1. Main Logic

This application is simple book collection with CRUD operations on a [**Book**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/model/Book.java) entity, using document based search engine Elasticsearch for persistence.
The main service [**ElasticsearchServiceImpl**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/service/ElasticsearchServiceImpl.java) is the implementation of this interface: [**ElasticsearchService**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/service/ElasticsearchServiceImpl.java).
This is a very generic component that is designed to handle any entity type.

For this Book Collection application, all methods will operate on index named: **library** with type **book**.

UI is built using Vaadin Framework, UI view class for CRUD operations is: [**BookView**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/view/BookView.java).

### 1.1. Create Operation

To create an index the following two methods are implemented.


```java
public interface ElasticsearchService<E extends Item> {

    ...
    String create(E entity, String index, String type) throws IOException;

    String create(Map<String, Object> indexValues, String index, String type) throws IOException;
    ...

}
```

For a given entity E, the first method will call the second method with indexValues map as the values of the fields
of an entity E resolved by reflection. Any non-null field value will be indexed.

A new *Elasticsearch index and type* will be created, if they do not exist.

Index id is calculated based on system current time (in milliseconds).

### 1.2. Read Operation

There are two methods one to read all index (match all criteria) and another one to read
one unique index by its ID.


```java
public interface ElasticsearchService<E extends Item> {

    ...
    Optional<E> findById(Class<E> entityClass, String index, String type, String indexId) throws IOException;

    List<E> search(Class<E> entityClass, String index,String type) throws IOException;
    ...

}
```

The _findByID_ method will throw an [_IndexNotFoundException_](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/exception/IndexNotFoundException.java) exception if no result can be found, and search method will return
empty list in case there is no index yet created.

### 1.3. Update Operation

To update an index the following two methods are implemented.


```java
public interface ElasticsearchService<E extends Item> {

    ...
     void update(E entity, String index, String type) throws IOException;

     void update(Map<String, Object> indexValues, String index, String type,String indexId) throws IOException;
    ...

}
```

For a given entity E, the first method will call the second method with indexValues map as the values of the fields
of an entity E resolved by reflection. Any non-null field value will be updated.

### 1.4. Delete Operation

To delete an index the following two methods are implemented.


```java
public interface ElasticsearchService<E extends Item> {

    ...
    void delete(E entity,String index,String type) throws IOException;

    void delete(String indexId,String index,String type) throws IOException;
    ...

}
```


## 2. Screenshots

These are sample view screenshots for the indexing (Create) operation.

- Create index: (https://github.com/aloys/book-collection/blob/master/doc/01_Create_Index.png)
- Refresh indexes: (https://github.com/aloys/book-collection/blob/master/doc/02_Refresh_Indexes.png)
- Check in Kibana: (https://github.com/aloys/book-collection/blob/master/doc/03_Check_Kibana.png)


## 3. Execution

### 3.1 Start UI Web Application

- Run the following command:

```console
java -jar target/book-collection.war
```
- Open this URL in the browser:
http://localhost:8080/

This will execute main class: [**MainApplication**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/framework/MainApplication.java)

That [**book-collection.war**](https://github.com/aloys/book-collection/blob/master/target/book-collection.war) is an uber-jar including all dependencies with an embedded **Tomcat web server**.

**Requirements**: Java Runtime Enviroment (JRE) version 8 or higher.

### 3.2 Start Elasticsearch

**Case 1: Using Docker Container**

If you have docker client (or toolbox) on your machine, then run this command to create a elk docker container:

```console
docker-compose up elk;
```

This will starts Elasticsearch, Logastah, and kibana docker container.

To access Kibana UI open this URL:
http://localhost:5601/

To acceess Elasticsearch open this URL
http://localhost:9200/

**Case 2: Using External Elasticsearch**

If you wish to use another Elasticsearch installation, run the application with these two runtime parameters _elasticsearch.server.host_ and
_elasticsearch.server.port_ for the host and port respectively


```console
java -jar target/book-collection.war -Delasticsearch.server.host=${HOST} -D=elasticsearch.server.port=${PORT}
```


## 4. Compiling

Run the following command:
```console
mvn clean install;
```
**Requirements**: Java Development Kit(JDK) version 8 or higher.

## 5. Testing

Unit tests provided is an integration test which requires Elasticsearch server to be up.
Those unit tests are in the class: [**ElasticsearchServiceImplIntegrationTest**](https://github.com/aloys/book-collection/blob/master/src/test/java/io/lab/biblio/ElasticsearchServiceImplIntegrationTest.java)

## 6. Framework

| Library | Version | Usage |
|---------|---------|---------|
| Vaadin | 8.1.7 | Web Application UI |
| Spring | 4.3.13 | DI, Web application components, Service transaction |
| Elasticsearch  | 6.2.3 | Document based search engine |

Built with spring-boot version 1.5.9

## 7. Known Issues
UI does not immediately refresh after an index is added,updated or removed to/in/from Elasticsearch

## 8. Improvements

These features may be added to the application for improvement:
- Keyword based search
- Pagination query
- Expose connection pool configuration for perfomance tuning
- Converter to handle all datatypes
- Securing application functionalities access
- Healt checks


## 9.References
Install Docker
https://docs.docker.com/toolbox/overview/

Docket Image
https://hub.docker.com/r/sebp/elk/builds/bghnala4hczqkuqunvfykec/

Elastic Search API
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high.html

Vaadin - UI Framework
https://vaadin.com/
