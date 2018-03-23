package io.lab.biblio.framework.view;

import com.vaadin.data.*;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Setter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.lab.biblio.framework.util.ReflectionUtil;
import io.lab.biblio.framework.view.converter.ConverterCache;
import io.lab.biblio.framework.view.notification.NotificationManager;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by amazimpaka on 2018-03-23
 */
public class AbstractView<E>  extends VerticalLayout implements View {

    public  static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;

    private final boolean htmlContentAllowed = true;

    private static final Map<Class<?>, PropertySet<?>> PROPERTIES_SET_CACHE = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Binder<?>> BINDERS_CACHE = new ConcurrentHashMap<>();

    protected Class<E> entityClass;

    protected static final NotificationManager NOTIFICATION_MANAGER = new NotificationManager();

    public AbstractView() {
        super();
        //setSizeFull();
    }

    @PostConstruct
    public void initialize() {
        final Optional<Class<?>> genericType = ReflectionUtil.resolveGeneric(getClass(), 0);
        if (!genericType.isPresent()) {
            NOTIFICATION_MANAGER.showErrorMessage(String.format("Cannot resolve entity class of view: %s", getClass().getName()));
        }
        entityClass = (Class<E>) genericType.get();
    }



    protected <FV, BV> void bindField(HasValue<FV> field,
                                      String propertyName,
                                      Class<FV> fieldvalueClass,
                                      Class<BV> beanValueClass) {
        if (propertyName != null) {
            final PropertySet<E> propertySet = getBeanPropertySet();
            final Optional<PropertyDefinition<E, ?>> wrapper = propertySet.getProperty(propertyName);
            if (wrapper.isPresent()) {

                final PropertyDefinition<E, BV> propertyDefinition = (PropertyDefinition<E, BV>) wrapper.get();
                final Optional<Converter<?, BV>> optionalConverter = ConverterCache.getConverter(propertyDefinition.getType());

                if (optionalConverter.isPresent()) {
                    final Converter<FV, BV> converter = ((Converter<FV, BV>) optionalConverter.get());

                    Binder.BindingBuilder<E, BV> bindingBuilder = getBinder()
                            .forField(field)
                            .withValidator(new BeanValidator(entityClass, propertyName))
                            .withConverter(converter);

                    final ValueProvider<E, BV> getter = propertyDefinition.getGetter();
                    final Optional<Setter<E, BV>> setter = propertyDefinition.getSetter();
                    bindingBuilder.bind(getter, setter.get());

                }else if (fieldvalueClass == beanValueClass){
                    //No converter is needed
                    Binder.BindingBuilder<E, FV> bindingBuilder = getBinder()
                            .forField(field)
                            .withValidator(new BeanValidator(entityClass, propertyName));

                    final ValueProvider<E, BV> getter = propertyDefinition.getGetter();
                    final Optional<Setter<E, BV>> setter = propertyDefinition.getSetter();
                    bindingBuilder.bind((ValueProvider<E, FV>) getter, (Setter<E, FV>)setter.get());
                }
            }
        }
    }


    private void show(String caption, String description, Notification.Type type) {
        new Notification(caption, description, type,htmlContentAllowed).show(Page.getCurrent());
    }


    protected static Button createButton(String caption, VaadinIcons icon, Button.ClickListener clickListener){
        final Button button = new Button();

        if(caption != null){
            button.setCaption(caption);
        }

        if(icon != null){
            button.setIcon(icon);
            button.setDescription(caption);
            button.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        }

        if(clickListener != null){
            button.addClickListener(clickListener);
        }
        return button;
    }

    protected PropertySet<E> getBeanPropertySet() {
        return (PropertySet<E>) PROPERTIES_SET_CACHE.computeIfAbsent(entityClass,
                (key) -> BeanPropertySet.get(key));
    }

    protected Binder<E> getBinder() {
        return (Binder<E>) BINDERS_CACHE.computeIfAbsent(entityClass,
                (key) -> new BeanValidationBinder<>(key));
    }


}
