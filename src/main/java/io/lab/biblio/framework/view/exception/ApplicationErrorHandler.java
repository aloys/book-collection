package io.lab.biblio.framework.view.exception;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import io.lab.biblio.framework.view.notification.NotificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class ApplicationErrorHandler extends DefaultErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationErrorHandler.class);

    private static final NotificationManager NOTIFICATION_MANAGER = new NotificationManager();

    private static final ExceptionAnalyzer EXCEPTION_ANALYZER = new DefaultExceptionAnalyzer();

    private static final List<Class<?>> WARNING_EXCEPTIONS = new ArrayList<>();

    static {
        WARNING_EXCEPTIONS.add(IllegalArgumentException.class);
        WARNING_EXCEPTIONS.add(ConstraintViolationException.class);
    }

    private static class ApplicationErrorHandlerReference {
        private static final ApplicationErrorHandler INSTANCE = new ApplicationErrorHandler();
    }

    private ApplicationErrorHandler() {
    }

    public static final ApplicationErrorHandler getInstance(){
        return ApplicationErrorHandlerReference.INSTANCE;
    }


    @Override
    public void error(ErrorEvent event) {

        final ExceptionAnalyzer.ExceptionInfo info = EXCEPTION_ANALYZER .getExceptionInfo(event.getThrowable());
        final Throwable exception = info.getException();


        if(WARNING_EXCEPTIONS.contains(exception.getClass())) {
            NOTIFICATION_MANAGER.showWarnMessage(exception.getMessage());

            if (logger.isDebugEnabled()) {
                logger.debug(exception.getMessage(), exception);
            }

        }else{
            NOTIFICATION_MANAGER.showErrorMessage(exception.getMessage());

            logger.error(exception.getMessage(), exception);
        }


    }
}
