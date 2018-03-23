package io.lab.biblio.framework.view.exception;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public interface ExceptionAnalyzer {

    Throwable findRootCause(Throwable throwable);


    ExceptionInfo getExceptionInfo(Throwable throwable);


    class ExceptionInfo implements Serializable {

        private final Throwable exception;
        private final String detailMessage;
        private final String exceptionClass;
        private final StackTraceElement stackTraceElement;
        private final StackTraceElement[] stackTrace;

        public ExceptionInfo(Throwable throwable) {
            this.exception = throwable;
            this.detailMessage = throwable.getMessage();
            this.exceptionClass = throwable.getClass().getName();
            this.stackTraceElement = throwable.getStackTrace()[0];
            this.stackTrace = throwable.getStackTrace();
        }

        public Throwable getException() {
            return exception;
        }

        public String getDetailMessage() {
            return detailMessage;
        }

        public String getExceptionClass() {
            return exceptionClass;
        }

        public StackTraceElement getStackTraceElement() {
            return stackTraceElement;
        }

        public StackTraceElement[] getStackTrace() {
            return stackTrace;
        }

        public String getStackTraceAsString() {
            StringWriter writer = new StringWriter();
            exception.printStackTrace(new PrintWriter(writer));
            return writer.toString();
        }

        @Override
        public String toString() {
            return "ExceptionDetail{" +
                    "detailMessage='" + detailMessage + '\'' +
                    ", exceptionClass='" + exceptionClass + '\'' +
                    ", stackTraceElement=" + stackTraceElement +
                    '}';
        }
    }
}
