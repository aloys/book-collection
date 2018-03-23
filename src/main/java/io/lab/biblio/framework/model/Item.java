package io.lab.biblio.framework.model;

import java.io.Serializable;

/**
 * Created by amazimpaka on 2018-03-23
 */
public interface Item<ID extends Serializable>  extends Serializable{

    ID getId();

    void setId(ID id);
}
