package io.lab.biblio.framework.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.lab.biblio.application.model.Item;
import io.lab.biblio.application.service.ElasticsearchService;
import io.lab.biblio.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;


/**
 * Created by amazimpaka on 2018-03-23
 */
public abstract class AbstractCrudView<E extends Item> extends AbstractView<E> {

    protected transient final Logger logger = LoggerFactory.getLogger(getClass());


    protected Grid grid;

    protected Panel formPanel;

    protected final HorizontalSplitPanel mainContent = new HorizontalSplitPanel();

    protected final HorizontalLayout toolbar = new HorizontalLayout();

    private boolean readOnlyMode;

    @Autowired
    protected ElasticsearchService<E> service;


    @Resource
    private transient Validator validator;

    @PostConstruct
    public void initialize() {
        super.initialize();

        grid = new Grid(entityClass);
        grid.setSizeFull();
        grid.setStyleName(ValoTheme.TABLE_SMALL);

        toolbar.addComponent(createButton("Refresh", VaadinIcons.REFRESH, (event) -> refresh()));

        if(!readOnlyMode){
            toolbar.addComponent(createButton("Add", VaadinIcons.PLUS_CIRCLE, (event) -> create()));
            toolbar.addComponent(createButton("Edit", VaadinIcons.EDIT, (event) -> update()));
            toolbar.addComponent(createButton("Delete", VaadinIcons.MINUS_CIRCLE, (event) -> delete()));
        }


        mainContent.setSizeFull();


        Optional<FormLayout> form = createForm();
        if (form.isPresent()) {
            formPanel = new Panel("Edit Form");
            formPanel.setSizeFull();
            formPanel.setContent(form.get());
        }

        addComponent(toolbar);
        addComponentsAndExpand(mainContent);

        refresh();
    }

    protected abstract void refresh();


    protected void create() {
        logger.debug("Executing create item");

        if (formPanel != null) {
            E instance = ReflectionUtil.newInstance(entityClass);
            getBinder().setBean(instance);
            showFrom();
        } else {
            NOTIFICATION_MANAGER.showWarnMessage(String.format("%s is not editable", entityClass.getSimpleName()));
        }
    }

    protected abstract void delete() ;


    protected void update() {
        logger.debug("Executing delete item");

        if (formPanel == null) {
            NOTIFICATION_MANAGER.showWarnMessage(String.format("%s is not editable", entityClass.getSimpleName()));
            return;
        }

        final E selected = (E) grid.asSingleSelect().getValue();
        if (selected == null) {
            NOTIFICATION_MANAGER.showWarnMessage(String.format("Please select a %s to delete", entityClass.getSimpleName()));
            return;
        }

        if (formPanel != null) {
            getBinder().setBean(selected);
            showFrom();
        }
    }


    protected abstract void save();

    protected void showFrom() {
        if (formPanel != null) {
            mainContent.setSplitPosition(100.0f / (float) GOLDEN_RATIO, Sizeable.Unit.PERCENTAGE);
            mainContent.setFirstComponent(grid);
            mainContent.setSecondComponent(formPanel);

            formPanel.setHeight(grid.getHeight(), grid.getHeightUnits());
        } else {
            mainContent.removeAllComponents();
            mainContent.setSplitPosition(100, Sizeable.Unit.PERCENTAGE);
            mainContent.setFirstComponent(grid);
        }

    }

    protected void hideFrom() {
        mainContent.removeAllComponents();
        mainContent.setSplitPosition(100, Sizeable.Unit.PERCENTAGE);
        mainContent.setFirstComponent(grid);
    }


    public void setItems(Collection<E> items) {
        grid.setItems(items);
    }


    public void setService(ElasticsearchService<E> service) {
        this.service = service;
    }

    private Optional<FormLayout> createForm() {
        return createForm(createFormFields());
    }


    protected List<Component> createFormFields() {
        return Collections.emptyList();
    }

    protected Optional<FormLayout> createForm(final List<Component> fields) {
        if (!fields.isEmpty()) {
            final FormLayout form = new FormLayout();
            form.setSizeFull();

            fields.forEach(field -> form.addComponent(field));

            Button saveButton = createButton("Save", null, (event) -> save());
            saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
            form.addComponent(saveButton);

            return Optional.of(form);
        }
        return Optional.empty();
    }

    protected final void setHidable(String columnName) {
        final Grid.Column column = grid.getColumn(columnName);
        if (column != null) {
            //Allow user to hide this column
            column.setHidable(true);

        } else {
            logger.error("No column with id:{} for grid in view of entity: {}", columnName, entityClass.getName());
        }
    }

    protected final void validate(E entity) {
        final Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }


    public boolean isReadOnlyMode() {
        return readOnlyMode;
    }

    public void setReadOnlyMode(boolean readOnlyMode) {
        this.readOnlyMode = readOnlyMode;
    }
}
