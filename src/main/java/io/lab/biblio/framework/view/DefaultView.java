package io.lab.biblio.framework.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by amazimpaka on 2018-03-23
 */
public class DefaultView extends VerticalLayout implements View {


    public DefaultView() {
        super();
        setSizeFull();
        addComponent(new Label(""));
    }

}