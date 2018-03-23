package io.lab.biblio.framework.view.exception;

import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;

public class DefaultExceptionAnalyzer implements ExceptionAnalyzer {


    @Override
    public Throwable findRootCause(Throwable throwable) {
        Assert.notNull(throwable, "Exception parameter is required");

        //@see: java.lang.reflect.InvocationTargetException
        if (throwable instanceof InvocationTargetException) {
            final Throwable targetException = ((InvocationTargetException) throwable).getTargetException();
            if (targetException != null) {
                throwable = targetException;
            }
        }

        final Throwable cause = throwable.getCause();
        if (cause != null) {
            return findRootCause(cause);
        }

        return throwable;
    }

    @Override
    public ExceptionInfo getExceptionInfo(Throwable throwable) {
        final Throwable cause = findRootCause(throwable);
        return new ExceptionInfo(cause);
    }


}
