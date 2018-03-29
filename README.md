
# Elasticsearch Book Collection

## 1. Main Logic

This application is simple book collection with CRUD operations on a Book entity, using Elasticsearch for persistence.
The main service [**ElasticsearchServiceImpl**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/service/ElasticsearchServiceImpl.java) is the implementation of this interface [**ElasticsearchService**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/service/ElasticsearchServiceImpl.java):

Foe this bookcollection application, all methods will operate on index names: *library* with type *book*.

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
of entity E found by reflection. Any non-null field value will be indexed.

A new *Elasticsearch index and type* will be created, if not existing.

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

The _findByID_ method will throw an _IndexNotFoundException_ exception if no result can be found, and search method will return
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

For a given entity E, the first method will call the second method with indexValues map as the values of the fields
of entity E found by reflection. Any non-null field value will be updated.

## 2. Screenshots


- Review Management:( https://github.com/aloys/customer-review/blob/master/doc/Screen_Shot_04_Reviews.png )
- Users Master Data: (https://github.com/aloys/customer-review/blob/master/doc/Screen_Shot_03_Users.png)
- Products Master Data: (https://github.com/aloys/customer-review/blob/master/doc/Screen_Shot_02_Products.png)
- Configuration - rating range and restricted words: (https://github.com/aloys/customer-review/blob/master/doc/Screen_Shot_01_Configurations.png)

## 3. Execution

- Download the **customer-review.jar** in *dist* directory
- Run the following command:

```console
java -jar customer-review.jar
```
- Open this URL in the browser:
http://localhost:8080/

This will execute main class: customer.review.framework.MainApplication
(https://github.com/aloys/customer-review/blob/master/src/main/java/customer/review/framework/MainApplication.java)

That jar is an uber-jar including all dependencies with an embedded **H2 database server**, and **Tomcat web server**.

The application will initialize _5 users_ and _5 products_ test data for convinience.

**Requirements**: Java Runtime Enviroment (JRE) version 8 or higher.

## 4. Compile

Run the following command:
```console
mvn clean install;
```
**Requirements**: Java Development Kit(JDK) version 8 or higher.

## 5. Framework

| Library | Version | Usage |
|---------|---------|---------|
| Vaadin | 8.1.7 | Web Application UI |
| Spring | 4.3.13 | DI, Web application components, Service transaction |
| Hibernate  | 5.0.12 | ORM / JPA |

Built with spring-boot version 1.5.9






# book-collection


mvn install;

Run integration test
mvn install -DskipTests=false;

Install Docker
https://docs.docker.com/toolbox/overview/


Start ELK Stack
docker-compose up elk;


Kibana
http://localhost:5601/

Elastic Search
http://localhost:9200/

Version: 6.2.3

https://hub.docker.com/r/sebp/elk/builds/bghnala4hczqkuqunvfykec/