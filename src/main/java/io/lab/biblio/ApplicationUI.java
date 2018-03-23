package io.lab.biblio;

import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import java.util.Date;

/**
 * Created by amazimpaka on 2018-03-23
 */


@SpringUI
public class ApplicationUI extends UI {


    public static final String MENU_STYLE_NAME = "valo-menu";


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //UI.getCurrent().setErrorHandler(ApplicationErrorHandler.getInstance());

        setContent(new Label(new Date().toString()));
        Responsive.makeResponsive(this);
    }


}