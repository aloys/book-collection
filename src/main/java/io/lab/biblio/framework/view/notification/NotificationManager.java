package io.lab.biblio.framework.view.notification;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Created by amazimpaka on 2018-03-23
 */
public class NotificationManager {

    private static final String INFO_TITLE = "Information";

    private static final String WARNING_TITLE = "Warning";

    private static final String ERROR_TITLE = "Error";

    private final boolean htmlContentAllowed = true;


    public void showInfoMessage(String message) {
        show(INFO_TITLE, message, Notification.Type.HUMANIZED_MESSAGE);
    }

    public void showInfoMessage(String title, String message) {
        show(title, message, Notification.Type.HUMANIZED_MESSAGE);
    }

    public void showTrayMessage(String message) {
        show(INFO_TITLE, message, Notification.Type.TRAY_NOTIFICATION);
    }

    public void showTrayMessage(String title, String message) {
        show(title, message, Notification.Type.TRAY_NOTIFICATION);
    }

    public void showWarnMessage(String message) {
        show(WARNING_TITLE, message, Notification.Type.WARNING_MESSAGE);
    }

    public void showWarnMessage(String title, String message) {
        show(title, message, Notification.Type.WARNING_MESSAGE);
    }

    public void showErrorMessage(String message) {
        show(ERROR_TITLE, message, Notification.Type.ERROR_MESSAGE);
    }

    public void showErrorMessage(String title, String message) {
        show(title, message, Notification.Type.ERROR_MESSAGE);
    }

    private void show(String caption, String description, Notification.Type type) {
        new Notification(caption, description, type,htmlContentAllowed).show(Page.getCurrent());
    }
}
