package io.lab.biblio.application.exception;

/**
 * Created by amazimpaka on 2018-03-28
 */
public class IndexNotFoundException extends RuntimeException{

    private final String index;
    private final String type;
    private final String indexId;

    public IndexNotFoundException(String index, String type, String indexId) {
        super();
        this.index = index;
        this.type = type;
        this.indexId = indexId;
    }

    @Override
    public String getMessage() {
        return String.format("Index: %s / Type: %s / IndexId: %s",index,type,indexId);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
