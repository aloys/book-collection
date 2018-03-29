
# Elasticsearch Book Collection

## 1. Main Logic

This application is simple book collection with CRUD operations on a Book entity, using Elasticsearch for persistence.
The main service [**ElasticsearchServiceImpl**] (https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/service/ElasticsearchServiceImpl.java) is the implementation of this interface [**ElasticsearchService**](https://github.com/aloys/book-collection/blob/master/src/main/java/io/lab/biblio/application/service/ElasticsearchServiceImpl.java):

```python
public interface ElasticsearchService<E extends Item> {

    Optional<E> findById(Class<E> entityClass, String index, String type, String indexId) throws IOException;

    List<E> search(Class<E> entityClass, String index,String type) throws IOException;

    String create(E entity, String index, String type) throws IOException;

    String create(Map<String, Object> indexValues, String index, String type) throws IOException;

    void update(E entity, String index, String type) throws IOException;

    void update(Map<String, Object> indexValues, String index, String type,String indexId) throws IOException;

    void delete(E entity,String index,String type) throws IOException;

    void delete(String indexId,String index,String type) throws IOException;
}
```


| Operation | Implementation Method|
|-------------|---------------------------------------|
| List all indexes | **search**  method in _ReviewService_ class|
| Create a customer review after performing these checks:<br />  (1) Check if Customer’s comment does not contain any of these curse words<br />  (2) Check if the rating is not out of range, mimimum rating > 0| **save**  method in _ReviewService_ class|

For details see:<br />
(https://github.com/aloys/customer-review/blob/master/src/main/java/customer/review/application/review/ReviewService.java)

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